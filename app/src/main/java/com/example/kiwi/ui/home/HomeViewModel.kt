package com.example.kiwi.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kiwi.Logics.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel @ViewModelInject constructor(): ViewModel()  {

val theLiveDataOfPosts:MutableLiveData<MutableList<Post>> =MutableLiveData<MutableList<Post>>()
    val netWorkFound:MutableLiveData<Boolean> = MutableLiveData()


    fun getThePosts()=viewModelScope.launch {
        var theFollowing= mutableListOf<String>()
        var thePosts =mutableListOf<Post>()

        val theFollowingIdentfier =
            FirebaseDatabase.getInstance().reference.child("Follow").child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child("Following")

        theFollowingIdentfier.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    theFollowing.clear()

                    for (snap in snapshot.children) {
                        theFollowing.add((snap.key.toString()))
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        val thePostsIdentfier = FirebaseDatabase.getInstance().reference.child("Posts")
        thePostsIdentfier.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                thePosts.clear()
                if (snapshot.exists()) {
                    for (interrr in snapshot.children) {
                        val thePost = interrr.getValue(Post::class.java)

                        for (postId in theFollowing) {
                            if (thePost!!.Publisher == postId) {
                                thePosts.add(thePost)
                            }
                        }
                    }
                    viewModelScope.launch(Dispatchers.Main) {
                        theLiveDataOfPosts.value=thePosts

                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })



    }

}