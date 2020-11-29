package com.example.kiwi.RoomDataBase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.google.firebase.auth.FirebaseAuth

@Dao
interface TheRoomDao {



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upSert(thePostForRoom: ThePostForRoom)

    @Query("DELETE FROM  MyOwnPosts WHERE postId = :thePostForRoom")
    suspend fun deletethePost(thePostForRoom: String)

    @Query("select * from MyOwnPosts where poster =  :user ")
    fun getThePosts( user :String ):LiveData<List<ThePostForRoom>>

    @Query("DELETE FROM MyOwnPosts")
    suspend fun deleteallThePosts()

}
