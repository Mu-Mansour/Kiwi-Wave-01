package com.example.kiwi.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.kiwi.R
import com.example.kiwi.Repos.TheAppRepo
import com.example.kiwi.RoomDataBase.ThePostForRoom
import com.example.kiwi.RoomDataBase.ThePostsDataBase
import dagger.hilt.android.AndroidEntryPoint

import kotlinx.android.synthetic.main.thepostforprofile.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileRVAdapter: RecyclerView.Adapter<ProfileRVAdapter.TheViewHolder>() {






    val thelistDifferCallBack= object : DiffUtil.ItemCallback<ThePostForRoom>()
    {
        override fun areItemsTheSame(oldItem: ThePostForRoom, newItem: ThePostForRoom): Boolean {
            return oldItem.postId == newItem.postId
        }

        override fun areContentsTheSame(oldItem: ThePostForRoom, newItem: ThePostForRoom): Boolean {
 //     here we compare everything in the memory so if there are 2 objects with 1 slight differ it will return true


            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    val theDiffrence= AsyncListDiffer(this,thelistDifferCallBack)
    fun submitTheList(list: ArrayList<ThePostForRoom>)=theDiffrence.submitList(list)




    inner class TheViewHolder (itemview: View):RecyclerView.ViewHolder(itemview)
    {

        val theimageFromprofile=itemview.thePostFromProfile
        val thecontenetFromProfile=itemview.theContenetFromProfile

        init {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TheViewHolder {
        return TheViewHolder( LayoutInflater.from(parent.context).inflate(R.layout.thepostforprofile,parent,false))

    }

    override fun onBindViewHolder(holder: TheViewHolder, position: Int) {
        holder. theimageFromprofile.load(theDiffrence.currentList[position].theImage){
            crossfade(true)
            crossfade(300)
            size(1100,750)
            transformations(RoundedCornersTransformation(10f))
        }
        holder.thecontenetFromProfile.text=theDiffrence.currentList[position].theContent

    }

    override fun getItemCount(): Int {
        return theDiffrence.currentList.size
    }
}
