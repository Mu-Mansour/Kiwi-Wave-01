package com.example.kiwi.ui.accountSettings

import android.net.Uri
import android.os.Build
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.kiwi.Repos.TheAppRepo
import com.google.android.material.textfield.TextInputLayout

class AccountModel@ViewModelInject constructor(private val theAppRepoforall: TheAppRepo): ViewModel() {


    private var imageURI: Uri?=null
     var isChecked="Null"
    private var profiletext: EditText?=null
    private var BIO: EditText?=null
    private var theEmaileinputinsignIn: TextInputLayout?=null
    private var Bio: TextInputLayout?=null


    fun gettheViewModelTheimageURI(uri: Uri)
    { imageURI =uri }
    fun gettheViewModelTheisChecked(ischeckedorNot:String)
    {isChecked= ischeckedorNot}
    fun gettheViewModelTheprofiletext(profileeditText:EditText)
    {profiletext= profileeditText }
    fun gettheViewModelTheBIO(thebio1:EditText)
    {BIO=thebio1 }
    fun gettheViewModelThetheEmaileinputinsignIn(theEmaileinputinsignIn1:TextInputLayout)
    {theEmaileinputinsignIn= theEmaileinputinsignIn1 }
    fun gettheViewModelTheBio(thebio2:TextInputLayout)
    { Bio= thebio2 }

    fun UploadImage()= theAppRepoforall.UploadImage(imageURI)
    fun getTheuserInfo()=theAppRepoforall.getTheuserInfo(profiletext!!,BIO!!)
    fun updateTheUserInfro()=theAppRepoforall.updateTheUserInfro(theEmaileinputinsignIn!!,Bio!!)

}