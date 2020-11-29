package com.example.kiwi.ui.accountSettings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.kiwi.R
import com.example.kiwi.ui.DialougeFragment.DialogueFragment
import com.theartofdev.edmodo.cropper.CropImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_account_settings.*
import kotlinx.android.synthetic.main.fragment_account_settings.view.*


@AndroidEntryPoint
class AccountSettings : Fragment(R.layout.fragment_account_settings) {

    private val theViewModel:AccountModel by viewModels()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE &&resultCode==Activity.RESULT_OK)
        {

            val result = CropImage.getActivityResult(data)
            settingProfileImage.load(result.uri)
            theViewModel.gettheViewModelTheisChecked("Checked")
            theViewModel.gettheViewModelTheimageURI(result.uri)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val theView =inflater.inflate(R.layout.fragment_account_settings,container,false)
        theViewModel.gettheViewModelTheBIO(theView.Bio.editText!!)
        theViewModel.gettheViewModelTheprofiletext(theView.theEmaileinputinsignUp.editText!!)
        theViewModel.gettheViewModelTheBio(theView.Bio)

        return theView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        theViewModel.getTheuserInfo()
        theViewModel.gettheViewModelThetheEmaileinputinsignIn(theEmaileinputinsignUp)

        changeImage.setOnClickListener {
            CropImage.activity().start(requireContext(), this)

        }


        CancelChanging.setOnClickListener {
            findNavController().navigate(AccountSettingsDirections.actionAccountSettingsToProfileragment2())
        }



        alreadyChanged.setOnClickListener {
            if (theViewModel.isChecked=="Checked")
            {
                theViewModel.updateTheUserInfro()
                theViewModel.UploadImage()
                findNavController().navigate(AccountSettingsDirections.actionAccountSettingsToProfileragment2())
            }
            else
            {

                theViewModel.updateTheUserInfro()
                findNavController().navigate(AccountSettingsDirections.actionAccountSettingsToProfileragment2())
            }

        }
        button2.setOnClickListener {

           val thedialouge = DialogueFragment()
            thedialouge.show(childFragmentManager,"Sign Out Dialogue")

        }
    }



        }