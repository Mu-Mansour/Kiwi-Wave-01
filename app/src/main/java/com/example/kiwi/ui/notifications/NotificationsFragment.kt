package com.example.kiwi.ui.notifications

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
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
import com.example.kiwi.ui.MessageChatFragment.ChatFragmentDirections
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
        theViewModel.netWorkFound.value=isNetworkAvailable(requireContext())
        theViewModel.theNotifications.observe(viewLifecycleOwner,{
            theNotificationsAdapter.submitTheList(it)
        })
        theViewModel.netWorkFound.observe(viewLifecycleOwner,{
            it?.let {
                if (!it)
                {
                    findNavController().navigate(NotificationsFragmentDirections.actionNotificationsFragmentToNoInternet())

                }
            }
        })

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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