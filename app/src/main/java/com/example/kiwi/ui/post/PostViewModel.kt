package com.example.kiwi.ui.post

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kiwi.Repos.TheAppRepo
import com.example.kiwi.RoomDataBase.ThePostForRoom
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PostViewModel @ViewModelInject constructor(private val theAppRepoforall: TheAppRepo) : ViewModel() {

     var imageURI: Uri?=null
     var contentToBeAdeed: String?=null
     val posted:MutableLiveData<Boolean> = MutableLiveData()
    val netWorkFound:MutableLiveData<Boolean> = MutableLiveData()



    fun uploadPostDetails()
    {

        val thekey=FirebaseDatabase.getInstance().reference.push().key.toString()
        if (imageURI!=null &&contentToBeAdeed!= null )
        {

            viewModelScope.launch(Dispatchers.IO) {
                val imageFileRefrence = FirebaseStorage.getInstance().reference.child("Posts Picture").child("${thekey}.png")
                imageFileRefrence.putFile(imageURI!!).addOnCompleteListener {
                    if (it.isSuccessful) {
                        imageFileRefrence.downloadUrl.addOnSuccessListener { theLinkCreated ->
                              val thePostMap = HashMap<String, Any>()
                                     thePostMap["PostID"] = thekey
                                     thePostMap["postURL"]= theLinkCreated.toString()
                                     thePostMap["Publisher"]= FirebaseAuth.getInstance().currentUser!!.uid
                                    thePostMap["content"] =contentToBeAdeed!!
                            FirebaseDatabase.getInstance().reference.child("Posts").child(thekey).updateChildren(thePostMap).addOnSuccessListener {
                                val postForRoom=ThePostForRoom(theLinkCreated.toString(),contentToBeAdeed!!,thekey,FirebaseAuth.getInstance().currentUser!!.uid)
                                viewModelScope.launch {
                                    theAppRepoforall.theFinalUpsert(postForRoom)
                                    withContext(Dispatchers.Main){
                                        posted.value=true
                                    }
                                }



                                    }
                                }




                            }
                        }
                    }


                }





            //ur logic

      /*  else
        {
            var thetextForStory = if(textField?.editText!!.text.isEmpty())"Kiwi" else textField.editText!!.text.toString()
            val fileRef = thePostImage.child("${System.currentTimeMillis()}.png")

            var uploadTask: StorageTask<*>
            uploadTask = fileRef.putFile(imageURI)
            uploadTask.continueWithTask(com.google.android.gms.tasks.Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        //ur logic
                    }
                }
                return@Continuation fileRef.downloadUrl
            }).addOnCompleteListener {
                if (it.isSuccessful) {
                    val downloadUrl = it.result

                    addtheRoomPost(downloadUrl.toString(),thetextForStory,thekey.toString())
                    val thePostMap = HashMap<String, Any>()
                    thePostMap["PostID"] =thekey.toString()
                    thePostMap["postURL"]= (downloadUrl.toString())
                    thePostMap["Publisher"]= FirebaseAuth.getInstance().currentUser!!.uid
                    thePostMap["content"] =thetextForStory


                    theFireBaseInstancePosts.child(thekey.toString()).updateChildren(thePostMap)



                }
                *//* theProgress.dismiss()
                 Snackbar.make(textField, "Your Post Is Successfully Added", Snackbar.LENGTH_SHORT).setBackgroundTint(thecontext.getColor(
                     R.color.colorAccent))
                     .show()*//*

            }
        }
*/
    }

}