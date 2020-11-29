package com.example.kiwi.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kiwi.Logics.Post
import com.example.kiwi.Repos.TheAppRepo
import kotlinx.coroutines.launch

class HomeViewModel @ViewModelInject constructor(private val theAppRepoforall: TheAppRepo): ViewModel()  {

val theLiveData:MutableLiveData<MutableList<Post>> =MutableLiveData<MutableList<Post>>()


    fun getThePosts()=viewModelScope.launch {
        theAppRepoforall.getThePostsForMe(theLiveData)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    val isOnline:Boolean= theAppRepoforall. isOnline()
}