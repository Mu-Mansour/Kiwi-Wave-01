package com.example.kiwi.ui.post


import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import coil.size.Scale
import coil.transform.RoundedCornersTransformation
import com.example.kiwi.R
import com.google.android.material.snackbar.Snackbar
import com.theartofdev.edmodo.cropper.CropImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_post.*
import kotlinx.android.synthetic.main.fragment_post.view.*

@AndroidEntryPoint
class PostFragment : Fragment(R.layout.fragment_post) {





   private  val theviewMode:PostViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val theviewhere = inflater.inflate(R.layout.fragment_post,container,false   )

        theviewMode.getTheTextForStatus(theviewhere.textField)
        return theviewhere
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         if (!theviewMode.isOnline)
        {
            findNavController().navigate(PostFragmentDirections.actionPostFragmentToNoInternet())
        }
        else {

        addImage.setOnClickListener {
            updateThePostImage()
        }
        postThePost.setOnClickListener {
            if (textField.editText?.text!!.isEmpty() ||theviewMode.imageURI==null)
            {
                Snackbar.make(requireView(), "Chose an image and type a story for it..", Snackbar.LENGTH_SHORT).setBackgroundTint(requireContext().getColor(
                    R.color.colorAccent))
                    .show()


            }
            else
            {


                theviewMode.uploadPostDetails()


            }


        }
        }
    }




    private fun updateThePostImage() {
        CropImage.activity().start(requireContext(), this)

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE &&resultCode== Activity.RESULT_OK)
        {

            val result = CropImage.getActivityResult(data)

            imageofThePost.load(result.uri){
                crossfade(true)
                crossfade(300)
                size(500,500)
                scale(Scale.FILL)
                transformations(RoundedCornersTransformation(30f))
            }
            theviewMode.getTheUri(result.uri!!)
        }
    }




}