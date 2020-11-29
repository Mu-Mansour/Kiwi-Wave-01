package com.example.kiwi.ui.notifications

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kiwi.Logics.Notification
import com.example.kiwi.Repos.TheAppRepo
import kotlinx.coroutines.launch

class NotificationsViewModel  @ViewModelInject constructor(private val theAppRepoforall: TheAppRepo): ViewModel()  {

val theNotifications :MutableLiveData<MutableList<Notification>> = MutableLiveData<MutableList<Notification>> ()


    fun removeTheNotifcations()=viewModelScope.launch { theAppRepoforall.removeAllNotis() }
    fun makeTheNotifications() = viewModelScope.launch {
        theAppRepoforall.getTheNotifications(theNotifications)
    }





    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    val isOnline:Boolean= theAppRepoforall. isOnline()




}