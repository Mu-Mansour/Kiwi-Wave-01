package com.example.kiwi.ui.HomeKiwiMessages

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kiwi.Repos.TheAppRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class HomeMessagesViewModel  @ViewModelInject constructor(): ViewModel(){

val theLiveDataOfFriends :MutableLiveData<MutableList<String>> =MutableLiveData<MutableList<String>> ()
    val netWorkFound:MutableLiveData<Boolean> = MutableLiveData()


fun makeTheFriendList()= viewModelScope.launch {
    val theFriendsIdentfier = FirebaseDatabase.getInstance().reference.child("Friends").child(FirebaseAuth.getInstance().currentUser!!.uid)
    val theFriends= mutableListOf<String>()

    theFriendsIdentfier.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()) {
                theFriends.clear()
                for (snap in snapshot.children) {
                    theFriends.add((snap.key.toString()))
                }
                theLiveDataOfFriends.value=theFriends
            }

        }

        override fun onCancelled(error: DatabaseError) {
        }
    })
}

}