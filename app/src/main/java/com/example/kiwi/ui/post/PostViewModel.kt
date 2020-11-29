package com.example.kiwi.ui.post

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kiwi.Repos.TheAppRepo
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch

class PostViewModel @ViewModelInject constructor(private val theAppRepoforall: TheAppRepo) : ViewModel() {

     var imageURI: Uri?=null

    private var textField: TextInputLayout?= null
    fun getTheTextForStatus(textField1: TextInputLayout)
    {
        textField=textField1
    }

    fun getTheUri(uri: Uri)
    {
        imageURI=uri
    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun uploadPostDetails()=viewModelScope.launch { theAppRepoforall.uploadPostDetails(imageURI,textField) }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    val isOnline:Boolean= theAppRepoforall. isOnline()
}