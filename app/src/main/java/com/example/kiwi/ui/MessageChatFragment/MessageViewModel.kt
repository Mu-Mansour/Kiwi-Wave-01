package com.example.kiwi.ui.MessageChatFragment

import android.media.MediaPlayer
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kiwi.Logics.ChatMessage
import com.example.kiwi.R
import com.example.kiwi.Repos.TheAppRepo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class MessageViewModel @ViewModelInject constructor(private val theAppRepoforall: TheAppRepo): ViewModel() {



    var theuser:String? = null
     var theLiveDatat:MutableLiveData<MutableList<ChatMessage>> = MutableLiveData<MutableList<ChatMessage>>()


    private var theTextView:EditText?=null
    private var userNameFromMessages: TextView?=null
    private var userProfileFromMessages:ImageView?=null
    private var sendImageFromMessages:ImageView?=null




    fun provideTheFriend(user:String)
    {
        theuser = user
    }
    fun provideEmAll(theTextView1:EditText , userNameFromMessages1: TextView ,userProfileFromMessages1:ImageView,sendImageFromMessages1:ImageView)
    {
        theTextView =theTextView1
        userNameFromMessages=userNameFromMessages1
        userProfileFromMessages= userProfileFromMessages1
        sendImageFromMessages = sendImageFromMessages1
    }
    fun makeTheFriend()=theAppRepoforall.makeTheFriendDetails(theuser!!,userProfileFromMessages!!,userNameFromMessages!!)

    fun sendTheMessage() = theAppRepoforall.SendTheMessage(theTextView!!,theuser!!)

    fun getTheMessagesForThis() = viewModelScope.launch {
        theAppRepoforall.getTheMessages(theuser!!,theLiveDatat)

    }



}