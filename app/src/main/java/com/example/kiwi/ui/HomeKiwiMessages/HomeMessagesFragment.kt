package com.example.kiwi.ui.HomeKiwiMessages

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kiwi.R
import com.example.kiwi.adapters.HomeMessagesAdapter
import com.example.kiwi.ui.home.HomeFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.home_messages_fragment.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeMessagesFragment : Fragment() {

val theViewModel :HomeMessagesViewModel by viewModels()

   val theAdapter = HomeMessagesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            theViewModel.makeTheFriendList()
            theViewModel.netWorkFound.value=isNetworkAvailable(requireContext())
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val theView = inflater.inflate(R.layout.home_messages_fragment, container, false)
        theViewModel.theLiveDataOfFriends.observe(viewLifecycleOwner ,{
            theAdapter.submitTheFriendList(it)
        })
        theViewModel.netWorkFound.observe(viewLifecycleOwner,{
            it?.let {
                if (!it)
                {
                    findNavController().navigate(HomeMessagesFragmentDirections.actionHomeMessagesFragmentToNoInternet())

                }
            }
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