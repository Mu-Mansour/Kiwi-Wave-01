package com.example.kiwi.ui.Splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.kiwi.NoFCMService.FirebaseService
import com.example.kiwi.R
import com.firebase.ui.database.paging.FirebaseDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Splaaaaaaash : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splaaaaaaash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        var theUserIsNotNull = FirebaseAuth.getInstance().currentUser?.uid


        if (theUserIsNotNull != null) {

            CoroutineScope(Dispatchers.Main).launch {
                delay(1000)

                findNavController().navigate(SplaaaaaaashDirections.actionSplaaaaaaashToProfileragment2())

                }


        } else {
            CoroutineScope(Dispatchers.Main).launch {
                delay(1000)
                findNavController().navigate(SplaaaaaaashDirections.actionSplaaaaaaashToLogInFragment())
            }

        }

    }
}