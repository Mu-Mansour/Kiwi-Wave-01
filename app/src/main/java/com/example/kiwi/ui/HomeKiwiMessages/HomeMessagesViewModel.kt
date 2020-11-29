package com.example.kiwi.ui.HomeKiwiMessages

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kiwi.Repos.TheAppRepo
import kotlinx.coroutines.launch

class HomeMessagesViewModel  @ViewModelInject constructor(private val theAppRepoforall: TheAppRepo): ViewModel(){

val theLiveDataOfFriends :MutableLiveData<MutableList<String>> =MutableLiveData<MutableList<String>> ()


fun makeTheFriendList()= viewModelScope.launch {  theAppRepoforall .makeTheFriendList(theLiveDataOfFriends)}

}