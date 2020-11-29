package com.example.kiwi.ui.HomeKiwiMessages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kiwi.R
import com.example.kiwi.adapters.HomeMessagesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.home_messages_fragment.*

@AndroidEntryPoint
class HomeMessagesFragment : Fragment() {

val theViewModel :HomeMessagesViewModel by viewModels()

   val theAdapter = HomeMessagesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val theView = inflater.inflate(R.layout.home_messages_fragment, container, false)

        theViewModel.makeTheFriendList()
        theViewModel.theLiveDataOfFriends.observe(viewLifecycleOwner ,{
            theAdapter.submitTheFriendList(it)



        })
        return theView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewforHomemessages.adapter=theAdapter
       recyclerViewforHomemessages.layoutManager=LinearLayoutManager(requireContext())
        imageView2.setOnClickListener {
            findNavController().navigate(HomeMessagesFragmentDirections.actionHomeMessagesFragmentToProfileragment2())
        }

    }


}