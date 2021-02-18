package com.example.kiwi.ui.accountSettings

import android.app.Activity
import android.content.Context
import android.content.Intent
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
import coil.load
import coil.transform.CircleCropTransformation
import com.example.kiwi.R
import com.example.kiwi.ui.DialougeFragment.DialogueFragment
import com.theartofdev.edmodo.cropper.CropImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_account_settings.*
import kotlinx.android.synthetic.main.fragment_account_settings.view.*
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AccountSettings : Fragment(R.layout.fragment_account_settings) {

    private val theViewModel:AccountModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        theViewModel.netWorkFound.value=isNetworkAvailable(requireContext())
        lifecycleScope.launch {
            theViewModel.getTheUserData()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE &&resultCode==Activity.RESULT_OK)
        {

            val result = CropImage.getActivityResult(data)
            ProfileImageFromSettings.load(result.uri)
            theViewModel.gettheViewModelTheisChecked(true)
            theViewModel.gettheViewModelTheimageURI(result.uri)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val theView =inflater.inflate(R.layout.fragment_account_settings,container,false)

        theViewModel.theUserData.observe(viewLifecycleOwner,{
            it?.let {
                theView.ProfileImageFromSettings.load(it.image){
                    crossfade(true)
                    transformations(CircleCropTransformation())
                }
                theView.theEmaileinputinsignUp.editText!!.setText(it.username)
                theView.Bio.editText!!.setText(it.bio)
            }
        })
        theViewModel.netWorkFound.observe(viewLifecycleOwner,{
            it?.let {
                if (!it)
                {
                    findNavController().navigate(AccountSettingsDirections.actionAccountSettingsToNoInternet())

                }
            }
        })

        return theView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        changeImage.setOnClickListener {
            CropImage.activity().start(requireContext(), this)

        }


        CancelChanging.setOnClickListener {
            findNavController().navigate(AccountSettingsDirections.actionAccountSettingsToProfileragment2())
        }



        alreadyChanged.setOnClickListener {
            if (theViewModel.isChecked)
            {
                theViewModel.updateTheUserData(theEmaileinputinsignUp.editText!!.text.toString(),Bio.editText!!.text.toString())
                theViewModel.updateTheImage()
                theViewModel.updated.observe(viewLifecycleOwner,{
                    it?.let {
                        if (it)
                        {
                            findNavController().navigate(AccountSettingsDirections.actionAccountSettingsToProfileragment2())

                        }
                    }
                })

            }
            else
            {
                theViewModel.updateTheUserData(theEmaileinputinsignUp.editText!!.text.toString(),Bio.editText!!.text.toString())
                theViewModel.updated.observe(viewLifecycleOwner,{
                    it?.let {
                        if (it)
                        {
                            findNavController().navigate(AccountSettingsDirections.actionAccountSettingsToProfileragment2())

                        }
                    }
                })            }

        }
        button2.setOnClickListener {

           val thedialouge = DialogueFragment()
            thedialouge.show(childFragmentManager,"Sign Out Dialogue")

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