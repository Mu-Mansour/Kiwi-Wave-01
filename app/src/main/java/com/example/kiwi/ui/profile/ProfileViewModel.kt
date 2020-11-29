package com.example.kiwi.ui.profile

import android.os.Build
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kiwi.NoFCMService.FirebaseService
import com.example.kiwi.Repos.TheAppRepo
import com.example.kiwi.RoomDataBase.ThePostForRoom
import com.example.kiwi.RoomDataBase.ThePostsDataBase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch

class ProfileViewModel  @ViewModelInject constructor(private val theAppRepoforall: TheAppRepo): ViewModel()  {
var thefolloing:TextView?=null
var thefollower:TextView?=null
var theuserName:TextView?=null
var theBio:TextView?=null
var circleImageView:ImageView?=null

    fun getTheViewFollowing(thefolloing1:TextView)
    {
        thefolloing=thefolloing1
    }
    fun getTheViewFollowers(thefollower1:TextView)
    {
        thefollower=thefollower1
    }

    fun getfollowing()=theAppRepoforall.getfollowing(thefolloing!!)
    fun getfolloers()=theAppRepoforall.getfollowers(thefollower!!)
    fun getTheUserDetails()=theAppRepoforall.getTheuserInfo(theuserName!!,circleImageView!!,theBio!!)



  fun  getTheuserInfo(ProfileUserName1:TextView,circleImageView1:ImageView,BioINProfile1:TextView)
    {
        theuserName=ProfileUserName1
        theBio=BioINProfile1
        circleImageView=circleImageView1

    }
    fun getThePosts()=theAppRepoforall.theFinalGetEmAll()

    fun subsToMyNots() = viewModelScope.launch {  theAppRepoforall.subsToMyNots()}


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    val isOnline:Boolean= theAppRepoforall. isOnline()




}