package com.example.kiwi.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kiwi.R
import com.example.kiwi.adapters.NotificationsAdapter
import com.example.kiwi.ui.home.HomeFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_notifications.*

@AndroidEntryPoint
class NotificationsFragment : Fragment(R.layout.fragment_notifications) {

val theViewModel :NotificationsViewModel by viewModels()

val theNotificationsAdapter= NotificationsAdapter()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        theViewModel.makeTheNotifications()
        theViewModel.theNotifications.observe(viewLifecycleOwner,{
            theNotificationsAdapter.submitTheList(it)
        })

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!theViewModel.isOnline)
        {
            findNavController().navigate(NotificationsFragmentDirections.actionNotificationsFragmentToNoInternet())
        }
        else {
            val theLayoutManager = LinearLayoutManager(requireContext())
            theLayoutManager.reverseLayout = true
            theLayoutManager.stackFromEnd = true
            recyclerView.adapter = theNotificationsAdapter

            recyclerView.layoutManager = theLayoutManager
            removeAllTheNotifications.setOnClickListener {
                theViewModel.removeTheNotifcations()
                theNotificationsAdapter.notifyDataSetChanged()
            }

        }





    }






}