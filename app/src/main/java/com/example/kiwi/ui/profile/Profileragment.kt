package com.example.kiwi.ui.profile

import android.annotation.SuppressLint
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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.kiwi.R
import com.example.kiwi.RoomDataBase.ThePostForRoom
import com.example.kiwi.adapters.ProfileRVAdapter
import com.example.kiwi.ui.DialougeFragment.DialogueFragmentForProfile
import com.example.kiwi.ui.post.PostFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profileragment.*
import kotlinx.android.synthetic.main.fragment_profileragment.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Profileragment: Fragment() {
    val thePostsRVAdapter = ProfileRVAdapter()
   private val theViewModel: ProfileViewModel by  viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        theViewModel.netWorkFound.value=isNetworkAvailable(requireContext())
        lifecycleScope.launch(Dispatchers.Main) {
            theViewModel.getfollowing()
            theViewModel.getfollowers()
            theViewModel.getTheUserDetails()
            theViewModel.subsToMyNots()
        }


    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

            val theView = inflater.inflate(R.layout.fragment_profileragment, container, false)
        theViewModel.theUserDetails.observe(viewLifecycleOwner,{
            it?.let {
                theView.ProfileUserName.text=it.username
                theView.BioINProfile.text=it.bio
                theView.circleImageView.load(it.image){
                        transformations(CircleCropTransformation())
                }


            }
        })
        theViewModel.thefollower.observe(viewLifecycleOwner,{
            it?.let {
                theView.followersCount.text=it
            }
        })
        theViewModel.thefollowing.observe(viewLifecycleOwner,{
            it?.let {
                theView.totallFollowing.text=it
            }
        })

        theViewModel.haveItems.observe(viewLifecycleOwner,{
            it?.let {
                if (it)
                {
                    theView.toShow.visibility=View.VISIBLE
                    theView.SwipeableTV.visibility=View.VISIBLE
                }
                else
                {
                    theView.toShow.visibility=View.GONE
                    theView.SwipeableTV.visibility=View.GONE
                }
            }
        })
        theViewModel.netWorkFound.observe(viewLifecycleOwner,{
            it?.let {
                if (!it)
                {
                    findNavController().navigate(ProfileragmentDirections.actionProfileragment2ToNoInternet())

                }
            }
        })
            return theView
        }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




            val theProfileLayOutManage = LinearLayoutManager(requireContext())
            theProfileLayOutManage.reverseLayout = true
            theProfileLayOutManage.stackFromEnd = true
            MyeOwnPosts.adapter = thePostsRVAdapter
            MyeOwnPosts.layoutManager = theProfileLayOutManage



            theViewModel.getThePosts().observe(viewLifecycleOwner,
                {
                    thePostsRVAdapter.submitTheList(it as ArrayList<ThePostForRoom>)
                    if (it.isNotEmpty())
                    {
                        theViewModel.haveItems.value=true
                    }

                })

            ItemTouchHelper(theSwipper).attachToRecyclerView(MyeOwnPosts)
            editPublicDetails.setOnClickListener {

                findNavController().navigate(ProfileragmentDirections.actionProfileragment2ToAccountSettings())
            }
            GoToChat.setOnClickListener {
                findNavController().navigate(ProfileragmentDirections.actionProfileragment2ToHomeMessagesFragment())
            }

    }




private val theSwipper = object :ItemTouchHelper.SimpleCallback ( 0,ItemTouchHelper.RIGHT) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val theDialogue = DialogueFragmentForProfile( thePostsRVAdapter.theDiffrence.currentList[viewHolder.adapterPosition])
        theDialogue.show(childFragmentManager,"Profile Dialogue")
        thePostsRVAdapter.notifyDataSetChanged()

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