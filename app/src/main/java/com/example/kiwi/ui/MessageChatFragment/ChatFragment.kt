package com.example.kiwi.ui.MessageChatFragment

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.kiwi.R
import com.example.kiwi.adapters.ChatAdapter
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_messiging_chat.*
import kotlinx.android.synthetic.main.fragment_messiging_chat.view.*

@AndroidEntryPoint
class ChatFragment : Fragment() {

    val args:ChatFragmentArgs by navArgs()

    val theChatAdapter=ChatAdapter()
   private val TheVM :MessageViewModel by viewModels()



    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val theChatView = inflater.inflate(R.layout.fragment_messiging_chat, container, false)
        TheVM.provideTheFriend(args.TheUser)
        TheVM.provideEmAll(theChatView.theMessageEditText,theChatView.theFriendName,theChatView.TheFriendImage,theChatView.theSendImage)
        TheVM.makeTheFriend()



        return theChatView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        theSendImage.setOnClickListener {
            TheVM.sendTheMessage()

            if(theMessageEditText.text.isNotEmpty()) {

                YoYo.with(Techniques.Wave).duration(500).playOn(theSendImage)
                theMessageEditText.text.clear()

            }

        }
        backToHomeMsg.setOnClickListener {
            findNavController().navigate(ChatFragmentDirections.actionChatFragmentToHomeMessagesFragment())
        }

        recyclerView2.adapter=theChatAdapter
        val theLayoutManager =LinearLayoutManager(requireContext())
        theLayoutManager.stackFromEnd=true
        recyclerView2.layoutManager=theLayoutManager

        TheVM.theLiveDatat.observe(viewLifecycleOwner,{
            theChatAdapter.submitTheList(it)
            recyclerView2.scrollToPosition((it.size)-1)
           var thelastmessage =it.size-1
            if (it[thelastmessage].Receiver==FirebaseAuth.getInstance().currentUser!!.uid)
            {
                val playon= MediaPlayer.create(requireContext(),R.raw.sendtext)
                playon.start()
            }
            theChatAdapter.notifyItemChanged(it.size)



        })

        TheVM.getTheMessagesForThis()
    }






}