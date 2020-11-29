package com.example.kiwi.ui.searchFragment

import android.os.Build
import android.text.Editable
import androidx.annotation.RequiresApi
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kiwi.Repos.TheAppRepo
import com.example.kiwi.adapters.UserAdapter

class SearchViewModel  @ViewModelInject constructor(private val theAppRepoforall: TheAppRepo): ViewModel(){

    var adapter:UserAdapter?=null
    var theTextEditor:Editable?=null
    fun getTheItems(inputADP:UserAdapter,editText:Editable)
    {
        theTextEditor=editText
        adapter =inputADP
    }

    fun doTheWatch()=theAppRepoforall.doTheWatchSearch(theTextEditor!!,adapter!!)


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    val isOnline:Boolean= theAppRepoforall. isOnline()
}