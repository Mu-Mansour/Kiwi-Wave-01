package com.example.kiwi.ui.logIn

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kiwi.Logics.Post
import com.example.kiwi.Repos.TheAppRepo
import com.example.kiwi.RoomDataBase.ThePostForRoom
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginFragmentModel@ViewModelInject constructor(private val theAppRepoforall: TheAppRepo): ViewModel() {

    var theEmail: String?=null
    var thepassword: String? =null
    val loginResult :MutableLiveData<Boolean> = MutableLiveData()
    val thereArePostsForThisUser :MutableLiveData<Boolean> = MutableLiveData()
    val loginEroor :MutableLiveData<String> = MutableLiveData()
    val netWorkFound:MutableLiveData<Boolean> = MutableLiveData()



    fun signInForFragment(){
        if (theEmail!= null && thepassword!= null)
        {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(theEmail!! ,thepassword!!).addOnCompleteListener {
                if (it.isSuccessful)
                {
                    loginResult.value=true
                    loginEroor.value=null
                }
                else
                {
                    loginResult.value=false
                    loginEroor.value= it.exception!!.message
                }
            }

        }
    }

    fun findIfThereArePostsForThisUser() {
        viewModelScope.launch(Dispatchers.IO) {
            FirebaseDatabase.getInstance().reference.child("Posts").orderByChild("Publisher")
                .equalTo(
                    FirebaseAuth.getInstance()
                        .currentUser!!.uid
                ).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {

                        for (i in snapshot.children) {
                            var posTFromFireBase = i.getValue(Post::class.java)
                            var theRoomPost = ThePostForRoom(
                                posTFromFireBase!!.postURL,
                                posTFromFireBase.content,
                                posTFromFireBase.PostID,
                                posTFromFireBase.Publisher
                            )
                            viewModelScope.launch(Dispatchers.IO) {
                                theAppRepoforall.theFinalUpsert(theRoomPost)
                            }
                        }
                        viewModelScope.launch(Dispatchers.Main) {
                            thereArePostsForThisUser.value = true
                        }
                        FirebaseDatabase.getInstance().reference.child("Posts")
                            .orderByChild("Publisher").equalTo(
                            FirebaseAuth.getInstance()
                                .currentUser!!.uid
                        ).removeEventListener(this)
                    } else {
                        viewModelScope.launch(Dispatchers.Main) {
                            thereArePostsForThisUser.value = false                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })

        }
    }

}