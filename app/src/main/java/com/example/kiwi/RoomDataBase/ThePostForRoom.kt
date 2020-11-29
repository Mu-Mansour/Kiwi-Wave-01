package com.example.kiwi.RoomDataBase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MyOwnPosts")
 data class ThePostForRoom constructor(var theImage:String, var theContent:String, @PrimaryKey(autoGenerate = false)var  postId:String , var poster :String)
{


}
