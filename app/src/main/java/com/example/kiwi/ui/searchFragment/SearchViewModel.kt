package com.example.kiwi.ui.searchFragment

import android.text.Editable
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kiwi.Logics.User
import com.example.kiwi.adapters.UserAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SearchViewModel  @ViewModelInject constructor(): ViewModel(){

    var adapter:UserAdapter?=null
    val netWorkFound:MutableLiveData<Boolean> = MutableLiveData()


    fun doTheWatchSearch(chars: Editable?, adapter: UserAdapter)
    {
        if (chars.toString()!="")
        {

            val theFireBaseDB=FirebaseDatabase.getInstance().reference.child("Users").orderByChild("username").startAt(chars.toString()).endAt((chars.toString())+"\uf8ff")

            //here we create the value change listner object for it and update the Recycle view also

            val theValueChangerListner = object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    adapter.clearTheList()
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


}