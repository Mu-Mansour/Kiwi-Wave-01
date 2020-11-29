package com.example.kiwi.modules


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.annotations.Nullable
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import javax.inject.Inject
import javax.inject.Qualifier


@Module
@InstallIn(ApplicationComponent::class)
 class  FireBaseModule {
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class FireBaseRefOfPosts

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class FireBaseRefOfUsers
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class FireBaseReStrorageRefPP
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class FireBaseReStrorageRefPPosts


    @Inject
    @Provides
    fun provideTheStorageRefrence():StorageReference
    {
        return FirebaseStorage.getInstance().reference
    }
    @Inject
    @FireBaseReStrorageRefPP
    @Provides
    fun provideTheStorageRefrenceProfileImage():StorageReference
    {
        return FirebaseStorage.getInstance().reference.child("Profile Picture")
    }
    @Inject
    @FireBaseReStrorageRefPPosts
    @Provides
    fun provideTheStorageRefrencePostImage():StorageReference
    {
        return FirebaseStorage.getInstance().reference.child("Posts Picture")
    }


    @Inject
    @Provides
    fun provideThefireBaseAuth(): FirebaseAuth
    {
        return FirebaseAuth.getInstance()
    }

    @Inject

    @Provides
    fun provideThefireBaseDataBaseInstenceRefrence(): DatabaseReference
    {
        return FirebaseDatabase.getInstance().reference
    }


    @Inject
    @Provides
    fun provideThefireBaseDataBaseInstence (): FirebaseDatabase
    {
        return FirebaseDatabase.getInstance()
    }
    @Inject

    @Provides
    @FireBaseRefOfPosts
    fun provideThefireBaseDataBaseRefrenceOfPosts (): DatabaseReference
    {
        return FirebaseDatabase.getInstance().reference.child("Posts")
    }
    @Provides
    @FireBaseRefOfUsers
    fun provideThefireBaseDataBaseRefrenceOfUsers (): DatabaseReference
    {
        return FirebaseDatabase.getInstance().reference.child("Users")
    }







}


