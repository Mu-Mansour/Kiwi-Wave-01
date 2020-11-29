package com.example.kiwi.ui.signUpFragment

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kiwi.Repos.TheAppRepo
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.launch

class SignUpModel @ViewModelInject constructor(private val theAppRepoforall: TheAppRepo): ViewModel() {
    var passwordfromSignIn: TextInputLayout?= null
    var theEmaileinputinsignIn: TextInputLayout?= null
    var confirmpassword: TextInputLayout?= null
    var userNamefirsttime: TextInputLayout?= null

    fun getTheItemsForFiewModel(passwordfromSignIn1: TextInputLayout,theEmaileinputinsignIn1: TextInputLayout,confirmpassword1: TextInputLayout?,userNamefirsttime1: TextInputLayout)
    {
        passwordfromSignIn=passwordfromSignIn1
        theEmaileinputinsignIn=theEmaileinputinsignIn1
        confirmpassword=confirmpassword1
        userNamefirsttime=userNamefirsttime1

    }
    fun signOut()=theAppRepoforall.signOut()
    fun safeTheUserInDB():Task<Void>?= theAppRepoforall.safeUserInfo(userNamefirsttime!!,theEmaileinputinsignIn!!)


    @RequiresApi(Build.VERSION_CODES.M)
    fun createTheAuth(): Task<AuthResult>?=theAppRepoforall.createAccount(passwordfromSignIn!!,theEmaileinputinsignIn!!,confirmpassword!!,userNamefirsttime!!)





}