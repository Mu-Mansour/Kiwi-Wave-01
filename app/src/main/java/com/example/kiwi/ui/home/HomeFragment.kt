package com.example.kiwi.ui.home

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kiwi.R
import com.example.kiwi.adapters.PostAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    val homeViewModel :HomeViewModel by viewModels()

    val theAdapterforthisFragment = PostAdapter()



    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val theviewforHome = inflater.inflate(R.layout.fragment_home, container, false)
        homeViewModel.getThePosts()
        homeViewModel.theLiveData.observe(viewLifecycleOwner,{
            theAdapterforthisFragment.submitTheList(it)
        })
        return theviewforHome
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
if (!homeViewModel.isOnline)
{
    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToNoInternet())
}
        else {
    val theLayoutManager = LinearLayoutManager(requireContext())
    theLayoutManager.reverseLayout = true
    theLayoutManager.stackFromEnd = true
    homeRV.adapter = theAdapterforthisFragment
    homeRV.layoutManager = theLayoutManager

}

    }
}







