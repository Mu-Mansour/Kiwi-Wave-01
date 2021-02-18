package com.example.kiwi.ui.notifications

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kiwi.Logics.Notification
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class NotificationsViewModel  @ViewModelInject constructor(): ViewModel()  {

val theNotifications :MutableLiveData<MutableList<Notification>> = MutableLiveData<MutableList<Notification>> ()
    val netWorkFound:MutableLiveData<Boolean> = MutableLiveData()


    fun removeTheNotifcations()=viewModelScope.launch {    FirebaseDatabase.getInstance().reference.child("Notifications").child(FirebaseAuth.getInstance().currentUser!!.uid).removeValue()}
    fun makeTheNotifications() = viewModelScope.launch {
           var theNotiList = mutableListOf<Notification>()
           val thePostsIdentfier= FirebaseDatabase.getInstance().reference.child("Notifications").child(
               FirebaseAuth.getInstance().currentUser!!.uid).child("FromPosts")
           thePostsIdentfier.addValueEventListener(object : ValueEventListener {
               override fun onDataChange(snapshot: DataSnapshot) {
                   theNotiList.clear()
                   if (snapshot.exists()) {
                       for (NOTS in snapshot.children) {
                           val theNotiFromPosts = NOTS.getValue(Notification::class.java)
                           theNotiList.add(theNotiFromPosts!!)


                       }
                       theNotifications.value=theNotiList
                   }
               }
               override fun onCancelled(error: DatabaseError) {
               }
           })

    }







}