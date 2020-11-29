package com.example.kiwi.ui.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kiwi.R
import com.example.kiwi.RoomDataBase.ThePostForRoom
import com.example.kiwi.adapters.ProfileRVAdapter
import com.example.kiwi.ui.DialougeFragment.DialogueFragmentForProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profileragment.*
import kotlinx.android.synthetic.main.fragment_profileragment.view.*

@AndroidEntryPoint
class Profileragment: Fragment() {
    val thePostsRVAdapter = ProfileRVAdapter()

   private val theViewModel: ProfileViewModel by  viewModels()



    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

            val theView = inflater.inflate(R.layout.fragment_profileragment, container, false)

            theViewModel.getTheViewFollowers(theView.totallFollowing)

                 theViewModel.subsToMyNots()

            theViewModel.getTheViewFollowing(theView.followersCount)

            theViewModel.getTheuserInfo(
                theView.ProfileUserName,
                theView.circleImageView,
                theView.BioINProfile
            )




            return theView
        }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!theViewModel.isOnline) {
            findNavController().navigate(ProfileragmentDirections.actionProfileragment2ToNoInternet())
        } else {

            val theProfileLayOutManage = LinearLayoutManager(requireContext())
            theProfileLayOutManage.reverseLayout = true
            theProfileLayOutManage.stackFromEnd = true
            MyeOwnPosts.adapter = thePostsRVAdapter
            MyeOwnPosts.layoutManager = theProfileLayOutManage
            theViewModel.getThePosts().observe(viewLifecycleOwner,
                { thePostsRVAdapter.submitTheList(it as ArrayList<ThePostForRoom>) })
            theViewModel.getfollowing()
            theViewModel.getfolloers()
            theViewModel.getTheUserDetails()
            ItemTouchHelper(theSwipper).attachToRecyclerView(MyeOwnPosts)
            editPublicDetails.setOnClickListener {

                findNavController().navigate(ProfileragmentDirections.actionProfileragment2ToAccountSettings())


            }
            imageView7.setOnClickListener {
                findNavController().navigate(ProfileragmentDirections.actionProfileragment2ToHomeMessagesFragment())
            }
        }
    }


private val theSwipper = object :ItemTouchHelper.SimpleCallback ( 0,ItemTouchHelper.RIGHT) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val theDialogue = DialogueFragmentForProfile( thePostsRVAdapter.theDiffrence.currentList[viewHolder.adapterPosition])
        theDialogue.show(childFragmentManager,"Profile Dialogue")
        thePostsRVAdapter.notifyDataSetChanged()

    }
}


}