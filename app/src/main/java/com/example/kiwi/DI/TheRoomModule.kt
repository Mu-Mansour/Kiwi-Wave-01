package com.example.kiwi.DI

import android.content.Context
import androidx.room.Room
import com.example.kiwi.RoomDataBase.ThePostsDataBase
import com.example.kiwi.RoomDataBase.TheRoomDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object TheRoomModule {
    @Singleton
    @Provides
    fun provideTheDataBase(@ApplicationContext context: Context): ThePostsDataBase = Room.databaseBuilder(context,
        ThePostsDataBase::class.java,"ThePostsDataBase.db").fallbackToDestructiveMigration().build()


    @Provides
    @Singleton
    fun provideTheDataBaseDao(dataBase: ThePostsDataBase): TheRoomDao =dataBase.getTheRoomPostsDase()
}
