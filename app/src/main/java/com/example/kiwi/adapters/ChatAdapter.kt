package com.example.kiwi.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kiwi.Logics.ChatMessage
import com.example.kiwi.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.the_message_left.view.*


class ChatAdapter()  : RecyclerView.Adapter<ChatAdapter.TheViewHolderOfMessage>() {
    private var theChatList :ArrayList<ChatMessage >  =ArrayList()
    var theOtherIsHere=false

    inner class TheViewHolderOfMessage(theView:View ,):RecyclerView.ViewHolder(theView)
    {



        var theTextMessage =theView.message
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): TheViewHolderOfMessage {
        return  if (  position==1  )
            {
                theOtherIsHere= false
                     TheViewHolderOfMessage(LayoutInflater.from(parent.context).inflate(R.layout.the_message_right,parent,false))

            }
                 else
            {

                theOtherIsHere=true
                    TheViewHolderOfMessage(LayoutInflater.from(parent.context).inflate(R.layout.the_message_left,parent,false))


            }

    }

    override fun onBindViewHolder(holder: TheViewHolderOfMessage, position: Int) {

        holder. theTextMessage .text=theChatList[position].Message
        holder.setIsRecyclable(false)


    }

    override fun getItemViewType(position: Int): Int {

        val thecurrentUser =FirebaseAuth.getInstance().currentUser?.uid

        return if (theChatList[position].Sender==thecurrentUser) 1 else 0

    }

    fun submitTheList(arrayList:MutableList<ChatMessage>)
    {
        theChatList=arrayList as ArrayList<ChatMessage>
        notifyDataSetChanged()

        
    }

    override fun getItemCount(): Int {
return  theChatList.size   }


}