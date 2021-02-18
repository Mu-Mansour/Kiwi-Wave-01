package com.example.kiwi.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.kiwi.Logics.Post
import com.example.kiwi.Logics.User
import com.example.kiwi.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.post_layout_for_homerv.view.*


class PostAdapter (): RecyclerView.Adapter<PostAdapter.PostAdapterViewHolder>() {


    private var postsList:ArrayList<Post> =ArrayList<Post>()
    private var theimageurl:String?=null

    inner class PostAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var thePosterimage = itemView.PosterProfileImage
        var thePosterthecontenet = itemView.thecontenet
        var thePosterUserName = itemView.posterUserName
        var thePostimage = itemView.postImage
        var thePostmessage = itemView.sendAmessageText
        var thePostmessageimage = itemView.likeImage
        init {

                    thePostmessageimage.setOnClickListener {
                        if (adapterPosition!= RecyclerView.NO_POSITION) {
                        YoYo.with(Techniques.Wobble).duration(500).playOn(thePostimage)
                        var theMSGtoBeSent =
                            if (thePostmessage.text.isEmpty()) "I like it" else thePostmessage.text.toString()
                        sendTheNotificationToTheOwner(theMSGtoBeSent,adapterPosition)
                        YoYo.with(Techniques.Wave).duration(500).playOn(thePostmessage)
                        YoYo.with(Techniques.Wave).duration(500).playOn(thePosterimage)

                        }


                    }

        }

        private fun sendTheNotificationToTheOwner(theMSGtoBeSent: String,theposition:Int) {
            if (postsList.size>0)
            {
                val thePostOwnerFirebase= FirebaseDatabase.getInstance().reference.child("Posts").child(postsList[theposition].PostID).child("Publisher")

                thePostOwnerFirebase.addValueEventListener(object : ValueEventListener {
                    @RequiresApi(Build.VERSION_CODES.M)
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val thehh = snapshot.value.toString()
                            val theNotificationItSelf = HashMap<String, Any>()
                            theNotificationItSelf["userId"] =
                                FirebaseAuth.getInstance().currentUser!!.uid
                            theNotificationItSelf["text"] = theMSGtoBeSent
                            theNotificationItSelf["postID"] = postsList[adapterPosition].PostID
                            FirebaseDatabase.getInstance().reference.child("Notifications").child(thehh)
                                .child("FromPosts")
                                .child(postsList[adapterPosition].PostID + FirebaseAuth.getInstance().currentUser!!.uid)
                                .updateChildren(theNotificationItSelf)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })

            }

        }


    }
    fun submitTheList(list:MutableList<Post>)
    {
        postsList =list as ArrayList<Post>
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdapterViewHolder {
        return PostAdapterViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.post_layout_for_homerv, parent, false)
        )

    }

    override fun onBindViewHolder(holder: PostAdapterViewHolder, position: Int) {


    val theposttoshow = postsList[position]
        theimageurl=theposttoshow.postURL
    holder.setIsRecyclable(false)
    holder.thePostimage.load(theposttoshow.postURL){
        scale(Scale.FILL)
        crossfade(true)
        crossfade(300)
        transformations(RoundedCornersTransformation(30f))
    }
    holder.thePosterthecontenet.text = theposttoshow.content
    makeThePostDetails(
        holder.thePosterimage,
        holder.thePosterUserName,
        theposttoshow.Publisher
    )


    }

    override fun getItemCount(): Int {
        return postsList.size
    }


    private fun makeThePostDetails(
        profileImagefromPost: ImageView,
        theUsernameFromThePost: TextView,
        poster: String
    ) {
        var thePosterFromUsers =
            FirebaseDatabase.getInstance().reference.child("Users").child(poster)

        thePosterFromUsers.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val theUser = snapshot.getValue(User::class.java)
                    profileImagefromPost.load(theUser!!.image)
                    {
                        transformations(CircleCropTransformation())
                    }
                    theUsernameFromThePost.text = theUser.username
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

    }
}



