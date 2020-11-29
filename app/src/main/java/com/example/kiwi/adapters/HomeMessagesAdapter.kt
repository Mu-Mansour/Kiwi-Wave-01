package com.example.kiwi.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.kiwi.Logics.User
import com.example.kiwi.R
import com.example.kiwi.ui.HomeKiwiMessages.HomeMessagesFragmentDirections
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.messanger_for_user.view.*

class HomeMessagesAdapter( ): RecyclerView.Adapter<HomeMessagesAdapter.TheViewHolderHome>() {


    private var theFriends: ArrayList<String> =  ArrayList<String>()


    inner class TheViewHolderHome(itemView: View) :RecyclerView.ViewHolder(itemView)
    {
        val theFriendImage = itemView.imageViewforhomeMessages
        val theFriendname= itemView.theFriendNameHomeMessages
        init {


            itemView.setOnClickListener {
                var theUserToBesSent:String =theFriends[adapterPosition]

                itemView.findNavController().navigate((HomeMessagesFragmentDirections.actionHomeMessagesFragmentToChatFragment(theUserToBesSent)))

            }




        }
    }
    fun submitTheFriendList(list:MutableList<String>)
    {
        theFriends = list as ArrayList<String>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TheViewHolderHome {
        return TheViewHolderHome(LayoutInflater.from(parent.context).inflate(R.layout.messanger_for_user,parent,false))
    }

    override fun onBindViewHolder(holder: TheViewHolderHome, position: Int) {
        makeTheFriend(holder.theFriendImage,holder.theFriendname,theFriends[position])

        holder.setIsRecyclable(false)

    }

    override fun getItemCount(): Int {
        return theFriends.size
    }
    private fun makeTheFriend(imageview:ImageView,text:TextView ,user:String)
    {
         var theuserIN =FirebaseDatabase.getInstance().reference.child("Users").child(user)





                theuserIN.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {

                                var theUserDetails = snapshot.getValue(User::class.java)
                                imageview.load(theUserDetails!!.image) {
                                    placeholder(R.color.colorPrimaryDark)
                                    transformations(CircleCropTransformation())
                                }
                                text.text = theUserDetails.username
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })

    }
}