package com.example.kiwi.Repos



import com.example.kiwi.NoFCMService.PushNotification
import com.example.kiwi.NoFCMService.RetrofitInstance
import com.example.kiwi.RoomDataBase.ThePostForRoom
import com.example.kiwi.RoomDataBase.TheRoomDao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TheAppRepo @Inject constructor(private val thePostsDaw : TheRoomDao) {

     fun sendNotification(notification: PushNotification) =CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitInstance.api.postNotification(notification)
         //more logic
        }




     suspend fun theFinalUpsert(post : ThePostForRoom)=thePostsDaw.upSert(post)
    suspend fun theFinalDelete(post :ThePostForRoom){
        thePostsDaw.deletethePost(post.postId)
        FirebaseDatabase.getInstance().reference.child("Posts").child(post.postId).removeValue()
        val storageUrl = post.theImage
        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(storageUrl)
        storageReference.delete()
        }
    fun theFinalGetEmAll()=thePostsDaw.getThePosts(FirebaseAuth.getInstance().currentUser!!.uid)



}
