package com.example.kiwi.ui.signUpFragment

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUpModel @ViewModelInject constructor(): ViewModel() {
    var passwordfromSignIn: String?= null
    var theEmaileinputinsignIn: String?= null
    var userNamefirsttime: String?= null
    val theResultFromNewAccountCreation:MutableLiveData<Boolean> = MutableLiveData()
    val theError:MutableLiveData<String> = MutableLiveData()
    val netWorkFound:MutableLiveData<Boolean> = MutableLiveData()

    fun createAnewAccount()
    {
        if (theEmaileinputinsignIn!= null && passwordfromSignIn!= null )
        {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(theEmaileinputinsignIn!!,
                passwordfromSignIn!!
            ).addOnCompleteListener {
                if (it.isSuccessful)
                {
                    val theUserMap= HashMap<String,Any>()
                    theUserMap["uid"]=FirebaseAuth.getInstance().currentUser!!.uid
                    theUserMap["username"]=userNamefirsttime!!
                    theUserMap["email"]=theEmaileinputinsignIn!!
                    theUserMap["bio"]="Iam a Kiwi"
                    theUserMap["image"]="https://firebasestorage.googleapis.com/v0/b/kiwi-5e98e.appspot.com/o/Default%20Images%2Fprofile.png?alt=media&token=716b5e45-4c33-48fc-b42c-36afc9c4ca57"
                    FirebaseDatabase.getInstance().reference.child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(theUserMap).addOnCompleteListener {
                        FirebaseAuth.getInstance().signOut()
                        theError.value= null
                        theResultFromNewAccountCreation.value=true
                    }
                }
                else
                {
                    theResultFromNewAccountCreation.value=false
                    theError.value=it.exception!!.message
                }


            }
        }

    }




}