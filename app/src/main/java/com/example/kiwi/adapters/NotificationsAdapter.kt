package com.example.kiwi.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.example.kiwi.Logics.Notification
import com.example.kiwi.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.notification_layout_fornotifications.view.*


class NotificationsAdapter(): RecyclerView.Adapter<NotificationsAdapter.NotificatiosViewHolder>() {

    private var theList:ArrayList<Notification> = ArrayList<Notification>()



    inner class NotificatiosViewHolder (itemView:View):RecyclerView.ViewHolder(itemView) {
        var theNotifierImage=itemView.notifierImage
        var theNotifierName=itemView.NotifierName
        var theNotifierMsg=itemView.NotificationContent
        var theNotiimage=itemView.thePostImage

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificatiosViewHolder {
        return NotificatiosViewHolder( LayoutInflater.from(parent.context).inflate(R.layout.notification_layout_fornotifications,parent,false))
    }

    override fun onBindViewHolder(holder: NotificatiosViewHolder, position: Int) {

            //for TheLiftImage
          getTheNotifierName(theList[position]).addValueEventListener(
              object : ValueEventListener {
                  override fun onDataChange(snapshot: DataSnapshot) {
                      if (snapshot.exists()) {
                          holder.  theNotifierName.text = snapshot.value.toString()
                      }
                  }

                  override fun onCancelled(error: DatabaseError) {
                      TODO("Not yet implemented")
                  }
              })
            // for The Nmae
          getTheNotifierimage(theList[position]).addValueEventListener(
              object : ValueEventListener {
                  override fun onDataChange(snapshot: DataSnapshot) {
                      if (snapshot.exists()) {
                          holder. theNotifierImage.load(snapshot.value.toString()) {
                              transformations(CircleCropTransformation())
                          }
                      }
                  }

                  override fun onCancelled(error: DatabaseError) {
                      TODO("Not yet implemented")
                  }
              })
            // forThePostImage
          getpostImage(theList[position]).addValueEventListener(
              object : ValueEventListener {
              override fun onDataChange(snapshot: DataSnapshot) {
                  if (snapshot.exists()) {
                      holder.  theNotiimage.load(snapshot.value.toString()) {
                          transformations(RoundedCornersTransformation(10f,10f,10f,10f))
                      }
                  }
              }

              override fun onCancelled(error: DatabaseError) {
                  TODO("Not yet implemented")
              }
          })


          holder.theNotifierMsg.text=theList[position].text

    }
    fun submitTheList(list:MutableList<Notification>)
    {
        theList = list as ArrayList<Notification>
        notifyDataSetChanged()

    }

    override fun getItemCount(): Int {
        return theList.size
    }

    fun getTheNotifierName(noti:Notification): DatabaseReference
    {
    return FirebaseDatabase.getInstance().reference.child("Users").child("${noti.userId}").child("username")
    }
    fun getTheNotifierimage(noti:Notification): DatabaseReference
    {
        return FirebaseDatabase.getInstance().reference.child("Users").child("${noti.userId}").child("image")
    }

    fun getpostImage(noti:Notification): DatabaseReference
    {

        return FirebaseDatabase.getInstance().reference.child("Posts").child("${noti.postID}").child("postURL")


    }

}