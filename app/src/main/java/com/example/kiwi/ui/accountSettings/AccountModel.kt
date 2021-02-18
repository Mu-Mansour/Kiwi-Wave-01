package com.example.kiwi.ui.accountSettings

import android.net.Uri
import android.os.Build
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kiwi.Logics.User
import com.example.kiwi.Repos.TheAppRepo
import com.example.kiwi.RoomDataBase.ThePostForRoom
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AccountModel@ViewModelInject constructor(): ViewModel() {


    private var imageURI: Uri?=null
    var isChecked=false
    val theUserData:MutableLiveData<User>  = MutableLiveData()
    val updated:MutableLiveData<Boolean> = MutableLiveData()
    val netWorkFound:MutableLiveData<Boolean> = MutableLiveData()


    fun getTheUserData()
    {
        FirebaseDatabase.getInstance().reference.child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
            .addValueEventListener(object :ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists())
                    {
                        theUserData.value=snapshot.getValue(User::class.java)
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }

    fun updateTheImage()
    {
        imageURI?.let {


        viewModelScope.launch(Dispatchers.IO) {
            val imageFileRefrence = FirebaseStorage.getInstance().reference.child("Profile Picture").child("${FirebaseAuth.getInstance().currentUser!!.uid}.png")
            imageFileRefrence.putFile(imageURI!!).addOnCompleteListener {
                if (it.isSuccessful) {
                    imageFileRefrence.downloadUrl.addOnSuccessListener { theLinkCreated ->
                        FirebaseDatabase.getInstance().reference.child("Users")
                            .child(FirebaseAuth.getInstance().currentUser!!.uid).child("image")
                            .setValue(theLinkCreated.toString()).addOnSuccessListener {
                                updated.value=true
                        }


                    }
                }


            }

                }
            }
        }

    fun updateTheUserData(userNmae:String,Bio:String)
    {
        FirebaseDatabase.getInstance().reference.child("Users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child("username").setValue(userNmae).addOnSuccessListener {
                FirebaseDatabase.getInstance().reference.child("Users")
                    .child(FirebaseAuth.getInstance().currentUser!!.uid).child("bio").setValue(Bio).addOnSuccessListener {
                        updated.value=true
                    }
            }
    }

    fun gettheViewModelTheimageURI(uri: Uri)
    {
        imageURI =uri
    }
    fun gettheViewModelTheisChecked(ischeckedorNot:Boolean)
    {
        isChecked= ischeckedorNot
    }




}