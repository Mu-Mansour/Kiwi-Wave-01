package com.example.kiwi.ui.MessageChatFragment

import android.content.Context
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.kiwi.R
import com.example.kiwi.adapters.ChatAdapter
import com.example.kiwi.ui.logIn.LogInFragmentDirections
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_messiging_chat.*
import kotlinx.android.synthetic.main.fragment_messiging_chat.view.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private val args:ChatFragmentArgs by navArgs()
    private val theChatAdapter=ChatAdapter()
    private val theVM :MessageViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            theVM.makeTheFriendDetails(args.TheUser)
            theVM.getTheMessages(args.TheUser)
         theVM.unfriended.value=false
         theVM.netWorkFound.value=isNetworkAvailable(requireContext())

        }
    }


    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val theChatView = inflater.inflate(R.layout.fragment_messiging_chat, container, false)
        theVM.theCurrentFriend.observe(viewLifecycleOwner,{
            it?.let {
                theChatView.TheFriendImage.load(it.image){
                    crossfade(true)
                    transformations(CircleCropTransformation())
                }
                theChatView.theFriendName.text=it.username
            }
        })
        theVM.unfriended.observe(viewLifecycleOwner, {
            it?.let {
                if (it) {
                    Toast.makeText(requireContext(), "You Cant Reply To This Conversation Any More", Toast.LENGTH_SHORT).show()
                    theChatView.theSendImage.setOnClickListener{
                        Toast.makeText(requireContext(), "You Cant Reply To This Conversation Any More", Toast.LENGTH_SHORT).show()

                    }
                }
                else
                {
                    theSendImage.setOnClickListener {


                        if(theMessageEditText.text.isNotEmpty()) {
                            theVM.sendTheMessage(theMessageEditText.text.toString(),args.TheUser)
                            YoYo.with(Techniques.Wave).duration(500).playOn(theSendImage)
                            theMessageEditText.text.clear()

                        }

                    }
                }

            }
        })


        theVM.netWorkFound.observe(viewLifecycleOwner,{
            it?.let {
                if (!it)
                {
                    findNavController().navigate(ChatFragmentDirections.actionChatFragmentToNoInternet())

                }
            }
        })

        return theChatView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        backToHomeMsg.setOnClickListener {
            findNavController().navigate(ChatFragmentDirections.actionChatFragmentToHomeMessagesFragment())
        }

        recyclerView2.adapter=theChatAdapter
        val theLayoutManager =LinearLayoutManager(requireContext())
        theLayoutManager.stackFromEnd=true
        recyclerView2.layoutManager=theLayoutManager

        theVM.theLiveDataChat.observe(viewLifecycleOwner,{
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

    }


    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // For 29 api or above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ->    true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ->   true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ->   true
                else ->     false
            }
        }
        // For below 29 api
        else {
            if (connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnectedOrConnecting) {
                return true
            }
        }
        return false
    }



}