package com.example.kiwi.ui.logIn

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.kiwi.NoFCMService.FirebaseService
import com.example.kiwi.Repos.TheAppRepo
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.messaging.FirebaseMessaging

class LoginFragmentModel@ViewModelInject constructor(private val theAppRepoforall: TheAppRepo): ViewModel() {

    var theEmail: TextInputLayout?=null
    var thepassword: TextInputLayout? =null

    fun getTheEamailModel(Email: TextInputLayout)
    {
        theEmail= Email
    }
    fun getPasswordforViewmodel(password: TextInputLayout)
    {
        thepassword=password
    }



    fun signInForFragment()=theAppRepoforall.singIn(thepassword!!,theEmail!! )

}