package com.example.kiwi.RoomDataBase

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [ThePostForRoom::class],version = 2,exportSchema = false)
abstract class ThePostsDataBase:RoomDatabase() {
    abstract fun getTheRoomPostsDase():TheRoomDao

}
