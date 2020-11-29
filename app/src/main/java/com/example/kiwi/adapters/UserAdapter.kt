package com.example.kiwi.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.kiwi.Logics.User
import com.example.kiwi.R

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.useritemforsearch.view.*
import javax.inject.Inject


class UserAdapter(): RecyclerView.Adapter<UserAdapter.UserAdapterViewHolder>() {

    val userlList = ArrayList<User>()

     var theFireBaseUser: FirebaseUser = FirebaseAuth.getInstance().currentUser!!


    inner class UserAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //here we assign the views to use them as the holder object on bind
        var theTextView = itemView.textView2forsearchAdapter
        var theTextfollowers = itemView.followerssearchAdapter
        var theTextViewfollowing = itemView.followingsearchAdapter
        var theTextViewBio = itemView.textView2

        var theImageView = itemView.circleImageViewforAdaptersearch
        var theButton = itemView.button3foradapterSearch


        init {

            // checkFollowingStats(userlList[adapterPosition].uid,itemView.button3foradapterSearch )
            //here we declare the buttom follow job
            itemView.button3foradapterSearch.setOnClickListener { it ->
                if (adapterPosition !=RecyclerView.NO_POSITION)
                 {

                if (it.button3foradapterSearch.text.toString() == "Follow") {
                    theFireBaseUser.uid.let { it0 ->
                        FirebaseDatabase.getInstance()
                            .reference.child("Follow")
                            .child(it0.toString())
                            .child("Following")
                            .child(userlList[adapterPosition].uid).setValue(true)
                            .addOnCompleteListener { it1 ->
                                if (it1.isSuccessful) {
                                    theFireBaseUser.uid.let { it2 ->
                                        FirebaseDatabase.getInstance().reference
                                            .child("Follow")
                                            .child(userlList[adapterPosition].uid)
                                            .child("Followers").child(it2.toString())
                                            .setValue(true).addOnCompleteListener { it3 ->
                                                if (it3.isSuccessful) {


                                                  val updateTheFriends=  FirebaseDatabase.getInstance().reference
                                                        .child("Follow")
                                                        .child(userlList[adapterPosition].uid)
                                                        .child("Following").child(theFireBaseUser.uid)
                                                    updateTheFriends.addValueEventListener(object : ValueEventListener{
                                                        override fun onDataChange(snapshot: DataSnapshot) {
                                                           if (snapshot.exists())
                                                           {

                                                               FirebaseDatabase.getInstance().reference
                                                                   .child("Friends").child(userlList[adapterPosition].uid).child(theFireBaseUser.uid).setValue(true)
                                                               FirebaseDatabase.getInstance().reference
                                                                   .child("Friends").child(theFireBaseUser.uid).child(userlList[adapterPosition].uid).setValue(true)
                                                           }
                                                        }

                                                        override fun onCancelled(error: DatabaseError) {
                                                            TODO("Not yet implemented")
                                                        }
                                                    })



                                                }
                                            }
                                    }
                                }
                            }
                    }
                } else {
                    theFireBaseUser.uid.let { it4 ->
                        FirebaseDatabase.getInstance()
                            .reference.child("Follow")
                            .child(it4.toString())
                            .child("Following")
                            .child(userlList[adapterPosition].uid)
                            .removeValue().addOnCompleteListener { it5 ->
                                if (it5.isSuccessful) {
                                    theFireBaseUser.uid.let { it6 ->
                                        FirebaseDatabase.getInstance().reference
                                            .child("Follow")
                                            .child(userlList[adapterPosition].uid)
                                            .child("Followers").child(it6.toString())
                                            .removeValue().addOnCompleteListener { it7 ->
                                                if (it7.isSuccessful) {
                                                    val updateTheFriends=  FirebaseDatabase.getInstance().reference
                                                        .child("Follow")
                                                        .child(userlList[adapterPosition].uid)
                                                        .child("Following").child(theFireBaseUser.uid)
                                                    updateTheFriends.addValueEventListener(object : ValueEventListener{
                                                        override fun onDataChange(snapshot: DataSnapshot) {
                                                            if (snapshot.exists())
                                                            {
                                                                FirebaseDatabase.getInstance().reference
                                                                    .child("Friends").child(userlList[adapterPosition].uid).child(theFireBaseUser.uid).removeValue()
                                                                FirebaseDatabase.getInstance().reference
                                                                    .child("Friends").child(theFireBaseUser.uid).child(userlList[adapterPosition].uid).removeValue()
                                                                FirebaseDatabase.getInstance().reference
                                                                    .child("Chats").child(userlList[adapterPosition].uid).child(theFireBaseUser.uid).removeValue()
                                                                FirebaseDatabase.getInstance().reference
                                                                    .child("Chats").child(theFireBaseUser.uid).child(userlList[adapterPosition].uid).removeValue()
                                                            }
                                                        }

                                                        override fun onCancelled(error: DatabaseError) {
                                                            TODO("Not yet implemented")
                                                        }
                                                    })
                                                }
                                            }
                                    }
                                }
                            }
                    }


                }



            }


        }

    }
}





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapterViewHolder {

      return  UserAdapterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.useritemforsearch,parent,false ))
    }

    override fun onBindViewHolder(holder: UserAdapterViewHolder, position: Int) {
        holder.theTextView.text=userlList[position].username
        holder.theTextViewBio.text=userlList[position].bio

        checkFollowingStats(userlList[position].uid,holder.theButton )
        updateTheFollowing(userlList[position].uid,holder.theTextfollowers)
        updateTheFollowers(userlList[position].uid,holder.theTextViewfollowing)
        holder.setIsRecyclable(false)
        //coil image maker
        holder.theImageView.load(userlList[position].image){
            size(1100,700)
            transformations(CircleCropTransformation())

        }





        /**
         * the buttom on click is initialized in the init block already
         *
         */
    }

    private fun checkFollowingStats(uid: String, theButton: Button?) {
    val theFollowingIdentfier= theFireBaseUser.uid.let {
        FirebaseDatabase.getInstance()
            .reference.child("Follow")
            .child(it.toString())
            .child("Following")
    }
        theFollowingIdentfier.addValueEventListener(object :ValueEventListener{
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(uid).exists())
                {
                    theButton?.text="Kiwi"

                }
                else theButton?.text="Follow"
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }
    private fun updateTheFollowing(uid: String, textView: TextView) {
        val theFollowingIdentfier= FirebaseDatabase.getInstance().reference.child("Follow").child(uid).child("Following")
         theFollowingIdentfier.addValueEventListener(object :ValueEventListener{
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
    private fun updateTheFollowers(uid: String, textView: TextView) {
        val theFollowingIdentfier= FirebaseDatabase.getInstance().reference.child("Follow").child(uid).child("Followers")
        theFollowingIdentfier.addValueEventListener(object :ValueEventListener{
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


    override fun getItemCount(): Int {

    return  userlList.size
    }

    fun addToTheAdapterList(user: User)
    {
        userlList.add(user)
    }
    fun clearTheList()
    {
        userlList.clear()
    }
}


