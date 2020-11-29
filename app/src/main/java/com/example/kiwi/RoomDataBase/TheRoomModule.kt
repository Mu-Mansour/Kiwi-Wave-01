package com.example.kiwi.RoomDataBase

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class TheRoomModule {
    @Singleton
    @Provides
    fun provideTheDataBase(@ApplicationContext context: Context):ThePostsDataBase= Room.databaseBuilder(context,ThePostsDataBase::class.java,"ThePostsDataBase.db").fallbackToDestructiveMigration().build()
    //here we create the dow to control the DB methods

    @Provides
    @Singleton
    fun provideTheDataBaseDao(dataBase: ThePostsDataBase):TheRoomDao=dataBase.getTheRoomPostsDase()
}
