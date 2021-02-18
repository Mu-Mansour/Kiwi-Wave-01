package com.example.kiwi.ui.profile

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kiwi.Logics.User
import com.example.kiwi.Repos.TheAppRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel  @ViewModelInject constructor(private val theAppRepoforall: TheAppRepo): ViewModel()  {
val thefollowing:MutableLiveData<String> = MutableLiveData()
val theUserDetails:MutableLiveData<User> = MutableLiveData()
val thefollower:MutableLiveData<String> =MutableLiveData()
val haveItems:MutableLiveData<Boolean> =MutableLiveData()
    val netWorkFound:MutableLiveData<Boolean> = MutableLiveData()



    fun getfollowing(){
        FirebaseDatabase.getInstance().reference.child("Follow").child(FirebaseAuth.getInstance().currentUser!!.uid).child("Following")
            .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                {
                    viewModelScope.launch(Dispatchers.Main) {
                        thefollowing.value=snapshot.childrenCount.toString()

                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun getfollowers(){
      FirebaseDatabase.getInstance().reference.child("Follow").child(FirebaseAuth.getInstance().currentUser!!.uid).child("Followers")
        .addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                {
                    viewModelScope.launch(Dispatchers.Main) {

                        thefollower.value = snapshot.childrenCount.toString()
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    fun getTheUserDetails(){
        val theFollowingIdentfier =   FirebaseDatabase.getInstance().reference.child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
        theFollowingIdentfier.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    viewModelScope.launch(Dispatchers.Main) {

                        theUserDetails.value = snapshot.getValue(User::class.java)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }




    fun getThePosts()=theAppRepoforall.theFinalGetEmAll()
    fun subsToMyNots()
    {

            FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
                FirebaseDatabase.getInstance().reference.child("UsersTokens").child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(it.token)
                val TOPIC = "/topics/${it.token}"
                FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)


        }
    }







}