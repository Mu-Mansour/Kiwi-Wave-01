package com.example.kiwi.ui.home

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
import com.example.kiwi.adapters.PostAdapter
import com.example.kiwi.ui.accountSettings.AccountSettingsDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    val homeViewModel :HomeViewModel by viewModels()

    val theAdapterforthisFragment = PostAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch(Dispatchers.Main) {
            homeViewModel.getThePosts()
            homeViewModel.netWorkFound.value=isNetworkAvailable(requireContext())

        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val theviewforHome = inflater.inflate(R.layout.fragment_home, container, false)
        lifecycleScope.launch(Dispatchers.Main) {
            homeViewModel.theLiveDataOfPosts.observe(viewLifecycleOwner,{
                theAdapterforthisFragment.submitTheList(it)
            })
        }
        homeViewModel.netWorkFound.observe(viewLifecycleOwner,{
            it?.let {
                if (!it)
                {
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToNoInternet())

                }
            }
        })
        return theviewforHome
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    val theLayoutManager = LinearLayoutManager(requireContext())
    theLayoutManager.reverseLayout = true
    theLayoutManager.stackFromEnd = true
    homeRV.adapter = theAdapterforthisFragment
    homeRV.layoutManager = theLayoutManager




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







