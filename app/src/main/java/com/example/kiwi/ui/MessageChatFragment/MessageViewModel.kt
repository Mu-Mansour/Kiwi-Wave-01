package com.example.kiwi.ui.MessageChatFragment

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kiwi.Logics.ChatMessage
import com.example.kiwi.Logics.User
import com.example.kiwi.NoFCMService.NotificationData
import com.example.kiwi.NoFCMService.PushNotification
import com.example.kiwi.Repos.TheAppRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId

class MessageViewModel @ViewModelInject constructor(private val theAppRepoforall: TheAppRepo): ViewModel() {
     var theLiveDataChat:MutableLiveData<MutableList<ChatMessage>> = MutableLiveData<MutableList<ChatMessage>>()
    val theCurrentFriend:MutableLiveData<User> = MutableLiveData()
    val unfriended:MutableLiveData<Boolean> = MutableLiveData()
    val netWorkFound:MutableLiveData<Boolean> = MutableLiveData()

    fun makeEmAll(title:String,message:String,toUser:String) {

        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            PushNotification(
                NotificationData(title, message),
                toUser
            ).also { it1->
                theAppRepoforall.sendNotification(it1)
            }
        }
    }
    fun sendTheMessage(theMessage: String, toUser:String )
    {

            var theKey= FirebaseDatabase.getInstance().reference.push().key
            var theMessageMap =HashMap<String,Any>()
            theMessageMap["Sender"] = FirebaseAuth.getInstance().currentUser!!.uid
            theMessageMap["Message"] = theMessage
            theMessageMap["Receiver"] = toUser
            theMessageMap["MSGID"] = theKey.toString()


            FirebaseDatabase.getInstance().reference.child("Friends").child(FirebaseAuth.getInstance().currentUser!!.uid).child (toUser)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        unfriended.value=false
                        FirebaseDatabase.getInstance().reference.child("Chats").child(toUser)
                            .child(FirebaseAuth.getInstance().currentUser!!.uid).child(theKey.toString())
                            .setValue(theMessageMap)
                        val theTask =  FirebaseDatabase.getInstance().reference.child("Chats")
                            .child(FirebaseAuth.getInstance().currentUser!!.uid).child(toUser)
                            .child(theKey.toString()).setValue(theMessageMap)
                        theTask.addOnCompleteListener {
                            if (it.isSuccessful) {
                                FirebaseDatabase.getInstance().reference.child("UsersTokens").child(toUser).addValueEventListener(object :ValueEventListener{
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot.exists())
                                        {
                                            val toBeNotified= snapshot.value .toString()
                                            FirebaseDatabase.getInstance().reference.child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid).child("username")
                                                .addValueEventListener(object : ValueEventListener {
                                                    override fun onDataChange(snapshot: DataSnapshot) {
                                                        if (snapshot.exists())
                                                        {
                                                            val theSender= snapshot.value.toString()
                                                            if (theMessageMap["Message"] != null)
                                                                makeEmAll("Your Friend $theSender Sent : ","${theMessageMap["Message"]}",toBeNotified)
                                                        }
                                                    }
                                                    override fun onCancelled(error: DatabaseError) {
                                                    }
                                                })
                                        }
                                    }
                                    override fun onCancelled(error: DatabaseError) {
                                    }

                                })

                            } else {
                                //logic
                            }
                        }

                    } else {
                        theMessageMap.values.clear()
                        unfriended.value=true
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                }
            })



    }


    fun getTheMessages(user:String )
    {

        val theMessages=mutableListOf<ChatMessage>()

         FirebaseDatabase.getInstance().reference.child("Chats").child(FirebaseAuth.getInstance().currentUser!!.uid).child(user)
        .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                theMessages.clear()
                if (snapshot.exists()) {
                    for (themessage in snapshot.children) {
                        var theMessageToBeAdded = themessage.getValue(ChatMessage::class.java)
                        theMessages.add(theMessageToBeAdded!!)

                    }

                    theLiveDataChat.value=theMessages
                }

            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

    }
    fun makeTheFriendDetails(theUserId:String )
    { FirebaseDatabase.getInstance().reference.child("Users").child(theUserId).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                {
                    theCurrentFriend.value=snapshot.getValue(User::class.java)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

    }



}