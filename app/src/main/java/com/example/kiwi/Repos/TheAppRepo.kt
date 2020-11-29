package com.example.kiwi.Repos



import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.text.Editable
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import coil.load
import coil.transform.CircleCropTransformation
import com.example.kiwi.Logics.ChatMessage
import com.example.kiwi.Logics.Notification
import com.example.kiwi.Logics.Post
import com.example.kiwi.Logics.User
import com.example.kiwi.NoFCMService.FirebaseService
import com.example.kiwi.NoFCMService.NotificationData
import com.example.kiwi.NoFCMService.PushNotification
import com.example.kiwi.NoFCMService.RetrofitInstance

import com.example.kiwi.R
import com.example.kiwi.RoomDataBase.ThePostForRoom
import com.example.kiwi.RoomDataBase.TheRoomDao
import com.example.kiwi.adapters.UserAdapter
import com.example.kiwi.modules.FireBaseModule

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

class TheAppRepo @Inject constructor(private val thePostsDaw : TheRoomDao) {




    @Inject
    lateinit var thecontext: Context

    @Inject
    lateinit var theFireBaseAuth: FirebaseAuth

    @Inject
    lateinit var theFireBaseRefrence: DatabaseReference

    @Inject
    @FireBaseModule.FireBaseRefOfUsers
    lateinit var theFireBaseInstanceUsers: DatabaseReference

    @Inject
    @FireBaseModule.FireBaseReStrorageRefPP
    lateinit var thePrfileImage: StorageReference

    @Inject
    @FireBaseModule.FireBaseRefOfPosts
    lateinit var theFireBaseInstancePosts: DatabaseReference

    @Inject
    @FireBaseModule.FireBaseReStrorageRefPPosts
    lateinit var thePostImage: StorageReference

    ///checking

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun isOnline(): Boolean {
        val connectivityManager: ConnectivityManager? =
            thecontext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {

                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {

                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {

                        return true
                    }
                }
            }
        }
        return false
    }











//fcm not



    fun makeEmAll(title:String,message:String,toUser:String) {

        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            PushNotification(
                NotificationData(title, message),
                toUser
            ).also {it1->
                sendNotification(it1)
            }
        }
    }
    private fun sendNotification(notification: PushNotification) =CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitInstance.api.postNotification(notification)
        }








    // for notifications
    fun getTheNotifications(liveData: MutableLiveData<MutableList<Notification>>)
    {
        var theNotiList = mutableListOf<Notification>()
        val thePostsIdentfier= FirebaseDatabase.getInstance().reference.child("Notifications").child(FirebaseAuth.getInstance().currentUser!!.uid).child("FromPosts")
        thePostsIdentfier.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                theNotiList.clear()
                if (snapshot.exists()) {
                    for (NOTS in snapshot.children) {
                        val theNotiFromPosts = NOTS.getValue(Notification::class.java)
                        theNotiList.add(theNotiFromPosts!!)


                    }
                    liveData.value=theNotiList
                }
            }




            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun removeAllNotis()
    {
        theFireBaseRefrence.child("Notifications").child(theFireBaseAuth.currentUser!!.uid).removeValue()
    }





    //for home kiwi message

    fun makeTheFriendList (liveData: MutableLiveData<MutableList<String>>)
    {
         val theFriendsIdentfier = FirebaseDatabase.getInstance().reference.child("Friends").child(theFireBaseAuth.currentUser!!.uid)
            var theFriends= mutableListOf<String>()

            theFriendsIdentfier.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        theFriends.clear()
                        for (snap in snapshot.children) {
                            theFriends.add((snap.key.toString()))

                        }
                        liveData.value=theFriends
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }







// for home Posts

    fun getThePostsForMe (liveData: MutableLiveData<MutableList<Post>>)
    {
        var theFollowing= mutableListOf<String>()
        var thePosts =mutableListOf<Post>()

        //to get users


            val theFollowingIdentfier =
                FirebaseDatabase.getInstance().reference.child("Follow").child(FirebaseAuth.getInstance().currentUser!!.uid)
                    .child("Following")

            theFollowingIdentfier.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        theFollowing.clear()

                        for (snap in snapshot.children) {
                            theFollowing.add((snap.key.toString()))
                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

//for add thePosts from following

            val thePostsIdentfier = FirebaseDatabase.getInstance().reference.child("Posts")
            thePostsIdentfier.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    thePosts.clear()
                    if (snapshot.exists()) {
                        for (interrr in snapshot.children) {
                            val thePost = interrr.getValue(Post::class.java)

                            for (postId in theFollowing) {
                                if (thePost!!.Publisher == postId) {
                                    thePosts.add(thePost)
                                }
                            }
                        }
            liveData.value=thePosts
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })







    }



    //for messageFragment

    fun getTheMessages(user:String ,data:MutableLiveData<MutableList<ChatMessage>>)
    {

        var theMessages=mutableListOf<ChatMessage>()

       var theSource =  theFireBaseRefrence.child("Chats").child(FirebaseAuth.getInstance().currentUser!!.uid).child(user)
        theSource.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                theMessages.clear()
                if (snapshot.exists()) {
                    for (themessage in snapshot.children) {
                        var theMessageToBeAdded = themessage.getValue(ChatMessage::class.java)
                        theMessages.add(theMessageToBeAdded!!)

                    }

                    data.value=theMessages
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
    fun SendTheMessage(theTextView1:EditText,toUser:String )
    {
        var theMessage =if(theTextView1.editableText.isEmpty())"null" else theTextView1.editableText.toString()


        if (theMessage!= "null")
        {
           var theKey= theFireBaseRefrence.push().key
            var theMessageMap =HashMap<String,Any>()
            theMessageMap["Sender"] = theFireBaseAuth.currentUser!!.uid
            theMessageMap["Message"] = theMessage
            theMessageMap["Receiver"] = toUser
            theMessageMap["MSGID"] = theKey.toString()


            var ifExist =theFireBaseRefrence.child("Friends").child(theFireBaseAuth.currentUser!!.uid).child (toUser)


            ifExist.addValueEventListener(object : ValueEventListener {

                @SuppressLint("SetTextI18n")
                @RequiresApi(Build.VERSION_CODES.M)
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {

                        theFireBaseRefrence.child("Chats").child(toUser)
                            .child(theFireBaseAuth.currentUser!!.uid).child(theKey.toString())
                            .setValue(theMessageMap)
                        val theTask = theFireBaseRefrence.child("Chats")
                            .child(theFireBaseAuth.currentUser!!.uid).child(toUser)
                            .child(theKey.toString()).setValue(theMessageMap)
                        theTask.addOnCompleteListener {
                            if (it.isSuccessful) {
                                theFireBaseRefrence.child("UsersTokens").child(toUser).addValueEventListener(object :ValueEventListener{
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                          if (snapshot.exists())
                                            {
                                                        val toBeNotified= snapshot.value .toString()
                                                theFireBaseRefrence.child("Users").child(theFireBaseAuth.currentUser!!.uid).child("username")
                                                    .addValueEventListener(object : ValueEventListener {
                                                        override fun onDataChange(snapshot: DataSnapshot) {
                                                                                if (snapshot.exists())
                                                                                {
                                                                                    val theSender= snapshot.value.toString()
                                                                                    if (theMessageMap["Message"] != null)
                                                                                    makeEmAll("Your Friend $theSender Sent : ","${theMessageMap["Message"]}",toBeNotified)
                                                                                }
                                                        }

                                                        override fun onCancelled(error: DatabaseError) {
                                                            TODO("Not yet implemented")
                                                        }

                                                    })

                                            }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        TODO("Not yet implemented")
                                    }

                                })

                            } else {
                                Snackbar.make(
                                    theTextView1,
                                    "Couldn't send $theMessage ",
                                    Snackbar.LENGTH_SHORT
                                ).setBackgroundTint(
                                    thecontext.getColor(
                                        R.color.colorAccent
                                    )
                                )
                                    .show()
                            }

                        }


                    } else {
                      theMessageMap.values.clear()
                        theTextView1.setText("you cant Reply ToThis Anymore")
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
        else{
            Toast.makeText(thecontext, "blank ", Toast.LENGTH_SHORT).show()
        }

    }


    fun makeTheFriendDetails(theUserId:String ,friendImage:ImageView,firendName:TextView)
    { val theUser =theFireBaseInstanceUsers.child(theUserId)
        theUser.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                {
                    val theuser = snapshot.getValue(User::class.java)
                    friendImage.load(theuser!!.image){
                        transformations(CircleCropTransformation())
                    }
                    firendName.text=theuser.username
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }
    //splaaaaaaaash
    fun subsToMyNots()
    {
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            FirebaseDatabase.getInstance().reference.child("UsersTokens").child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(it.token)
            val TOPIC = "/topics/${it.token}"
            FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        }
    }


    // for sign up
    @RequiresApi(Build.VERSION_CODES.M)
    fun createAccount(passwordfromSignIn:TextInputLayout, theEmaileinputinsignIn:TextInputLayout, confirmpassword:TextInputLayout, userNamefirsttime:TextInputLayout): Task<AuthResult>? {
        var thePassword=if (passwordfromSignIn.editText?.text!!.isEmpty())"null" else passwordfromSignIn.editText?.text.toString()

        var theEmaileinput=if(theEmaileinputinsignIn.editText?.text==null) "null" else theEmaileinputinsignIn.editText?.text.toString()

        var confirmpassword=if(confirmpassword.editText?.text==null)"null" else confirmpassword.editText?.text.toString()

        var userNamefirsttime=if(userNamefirsttime.editText?.text==null)"null" else userNamefirsttime.editText?.text.toString()


        return if (thePassword!= confirmpassword ||theEmaileinput=="null"|| confirmpassword=="null"||thePassword=="null"||userNamefirsttime=="null" ) {

            Snackbar.make(passwordfromSignIn, "check entities", Snackbar.LENGTH_SHORT).setBackgroundTint(thecontext.getColor(
                R.color.colorAccent))
                .show()
            null
        } else{

            theFireBaseAuth.createUserWithEmailAndPassword(theEmaileinput,thePassword)

        }



    }
    fun safeUserInfo(fullName:TextInputLayout,emial:TextInputLayout): Task<Void> ?
    {

        var thename=if(fullName.editText?.text==null) "null" else fullName.editText?.text.toString()
        var theEmail=if(emial.editText?.text==null) "null" else emial.editText?.text.toString()

        if (thename=="null" ||theEmail =="null" )
        {
            return null
        }
        else
        {
            val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
            val thefireBaseDataBaseRefrence= FirebaseDatabase.getInstance().reference.child("Users")


            val theUserMap= HashMap<String,Any>()
            theUserMap["uid"]=currentUserId
            theUserMap["username"]=thename
            theUserMap["email"]=theEmail
            theUserMap["bio"]="Iam a Kiwi"
            theUserMap["image"]="https://firebasestorage.googleapis.com/v0/b/kiwi-5e98e.appspot.com/o/Default%20Images%2Fprofile.png?alt=media&token=716b5e45-4c33-48fc-b42c-36afc9c4ca57"
            return  thefireBaseDataBaseRefrence.child(currentUserId).setValue(theUserMap)
        }



    }




    //for search fragment
    fun doTheWatchSearch(chars: Editable?, adapter: UserAdapter)
    {
        if (chars.toString()!="")
        {

            val theFireBaseDB=theFireBaseInstanceUsers.orderByChild("username").startAt(chars.toString()).endAt((chars.toString())+"\uf8ff")

            //here we create the value change listner object for it and update the Recycle view also

            val theValueChangerListner = object :ValueEventListener{

                override fun onDataChange(snapshot: DataSnapshot) {
                    //to clear the RV adapter list before we add any users to it first
                    adapter.clearTheList()
                    // to add items from the data base
                    if (snapshot.exists())
                    {
                        for (snamPShot in snapshot.children)
                        {
                            val user = snamPShot.getValue(User::class.java)
                            if (user!= null && user.uid != FirebaseAuth.getInstance().currentUser?.uid){
                                adapter.addToTheAdapterList(user)
                            }


                        }

                        adapter.notifyDataSetChanged()
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                }

            }
            //  here we add the change value listener to the fire base
            theFireBaseDB.addValueEventListener(theValueChangerListner)
        }
        else if (chars.toString()=="") {
            adapter.clearTheList()
            adapter.notifyDataSetChanged()
        }




    }


    //for Post fragment


    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.M)
    fun uploadPostDetails(imageURI:Uri?, textField:TextInputLayout?)
    {
        val theProgress= ProgressDialog(textField!!.context)
        theProgress.setMessage("Posting")
        theProgress.setCancelable(false)
        theProgress.setCanceledOnTouchOutside(false)
        theProgress.show()
        val thekey=theFireBaseInstancePosts.push().key
        if (imageURI==null )
        {

            Toast.makeText(thecontext, "Don't play Games", Toast.LENGTH_SHORT).show()
        }
        else
        {
            var thetextForStory = if(textField.editText!!.text.isEmpty())"Kiwi" else textField.editText!!.text.toString()
            val fileRef = thePostImage.child("${System.currentTimeMillis()}.png")

            var uploadTask: StorageTask<*>
            uploadTask = fileRef.putFile(imageURI)
            uploadTask.continueWithTask(com.google.android.gms.tasks.Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        Toast.makeText(thecontext, it.message, Toast.LENGTH_SHORT).show()

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
                theProgress.dismiss()
                Snackbar.make(textField, "Your Post Is Successfully Added", Snackbar.LENGTH_SHORT).setBackgroundTint(thecontext.getColor(
                    R.color.colorAccent))
                    .show()

            }
        }

    }
    private fun addtheRoomPost(theimage:String,theContent:String,theID:String)
    {


        val thePost= ThePostForRoom(theimage,theContent,theID,theFireBaseAuth.currentUser!!.uid)
       CoroutineScope(Dispatchers.IO).launch {
           theFinalUpsert(thePost)
       }

    }

    // for profile fragment
    fun getfollowing(textView: TextView)
    {
    val theidentity= theFireBaseRefrence.child("Follow").child(FirebaseAuth.getInstance().currentUser!!.uid).child("Following")
    theidentity.addValueEventListener(object :ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists())
            {

                textView.text=snapshot.childrenCount.toString()
            }

        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }
    })

    }


    fun getfollowers(textView: TextView)
    {
        val theidentity= theFireBaseRefrence.child("Follow").child(FirebaseAuth.getInstance().currentUser!!.uid).child("Followers")
        theidentity.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                {
                    textView.text=snapshot.childrenCount.toString()
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }


     fun getTheuserInfo(profiletext: TextView, imageView: ImageView, BIO:TextView) {
         val theFollowingIdentfier = theFireBaseRefrence.child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
         theFollowingIdentfier.addValueEventListener(object : ValueEventListener {
             @SuppressLint("SetTextI18n")
             override fun onDataChange(snapshot: DataSnapshot) {
                 if (snapshot.exists()) {
                     val user = snapshot.getValue(User::class.java)
                     imageView.load(user!!.image)
                     {


                         transformations(CircleCropTransformation())

                     }
                     profiletext.text = user.username
                     BIO.text = user.bio
                 }

             }

             override fun onCancelled(error: DatabaseError) {
             }
         })

     }


    //for account settings
    fun signOut(){
        FirebaseDatabase.getInstance().reference.child("UsersTokens").child(theFireBaseAuth.currentUser!!.uid).removeValue()
        theFireBaseAuth.signOut()

    }


      fun UploadImage(imageURI:Uri?)
         {
        if (imageURI==null)
            Toast.makeText(thecontext, "Empty Image", Toast.LENGTH_SHORT).show()

        else {
            //firstjob

            val fileRef = thePrfileImage.child("${FirebaseAuth.getInstance().currentUser!!.uid}.png")
            var uploadTask: StorageTask<*>
            uploadTask = fileRef.putFile(imageURI)

            //secondjob
            uploadTask.continueWithTask(com.google.android.gms.tasks.Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation fileRef.downloadUrl
            }).addOnCompleteListener(OnCompleteListener<Uri> {
                if (it.isSuccessful) {
                    val downloadUrl = it.result

                    val themap = HashMap<String, Any>()
                    themap["image"] = downloadUrl.toString()
                    theFireBaseInstanceUsers.child(FirebaseAuth.getInstance().currentUser!!.uid).updateChildren(themap)
                }

            })

        }
    }

     fun getTheuserInfo(profiletext: EditText, BIO: EditText)
    {

        val theFollowingIdentfier=theFireBaseInstanceUsers.child(FirebaseAuth.getInstance().currentUser!!.uid)

        theFollowingIdentfier.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                {
                    val user=snapshot.getValue(User::class.java)

                    profiletext.setText(user!!.username)
                    BIO.setText(user.bio)
                }

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

    }

     fun updateTheUserInfro(theEmaileinputinsignIn: TextInputLayout,Bio:TextInputLayout)
    {
        if (theEmaileinputinsignIn.editText?.text!!.isEmpty() || Bio.editText?.text!!.isEmpty())
        {
            Toast.makeText(thecontext, "Empty Fields", Toast.LENGTH_SHORT).show()
        }
        else
        {
            val UserIdentifier= theFireBaseRefrence.child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
            val theUserMap= HashMap<String,Any>()
            theUserMap["username"] = theEmaileinputinsignIn.editText!!.text.toString().toLowerCase(
                Locale.ROOT)
            theUserMap["bio"]=Bio.editText!!.text.toString()
            UserIdentifier.updateChildren(theUserMap)
        }

    }

    //for Login fragment


     fun singIn (thepassword:TextInputLayout, theEmail:TextInputLayout) =theFireBaseAuth.signInWithEmailAndPassword( theEmail.editText?.text!!.toString(),thepassword.editText?.text!!.toString())


    //for room
    private suspend fun theFinalUpsert(post : ThePostForRoom)=thePostsDaw.upSert(post)

    suspend fun theFinalDelete(post :ThePostForRoom){
        thePostsDaw.deletethePost(post.postId)
        theFireBaseRefrence.child("Posts").child(post.postId).removeValue()
        val storageUrl = post.theImage
        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(storageUrl)
        storageReference.delete()
        }


fun deleteTheAccount()
{

}


    suspend fun theFinalDeleteAll()=thePostsDaw.deleteallThePosts()

    fun theFinalGetEmAll()=thePostsDaw.getThePosts(FirebaseAuth.getInstance().currentUser!!.uid)



}
