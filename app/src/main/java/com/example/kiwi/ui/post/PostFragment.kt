package com.example.kiwi.ui.post


import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import coil.size.Scale
import coil.transform.RoundedCornersTransformation
import com.example.kiwi.R
import com.example.kiwi.ui.notifications.NotificationsFragmentDirections
import com.google.android.material.snackbar.Snackbar
import com.theartofdev.edmodo.cropper.CropImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_post.*
import kotlinx.android.synthetic.main.fragment_post.view.*

@AndroidEntryPoint
class PostFragment : Fragment(R.layout.fragment_post) {

   private  val theviewMode:PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        theviewMode.netWorkFound.value=isNetworkAvailable(requireContext())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val theviewhere = inflater.inflate(R.layout.fragment_post,container,false   )
        theviewMode.netWorkFound.observe(viewLifecycleOwner,{
            it?.let {
                if (!it)
                {
                    findNavController().navigate(PostFragmentDirections.actionPostFragmentToNoInternet())

                }
            }
        })
        return theviewhere
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         val theProgress= ProgressDialog(requireContext())

           // findNavController().navigate(PostFragmentDirections.actionPostFragmentToNoInternet())


        addImage.setOnClickListener {
            updateThePostImage()
        }
        postThePost.setOnClickListener {
            if (textField.editText?.text!!.isEmpty() ||theviewMode.imageURI==null)
            {
                Toast.makeText(requireContext(), "Chose an image and type a story for it.", Toast.LENGTH_LONG).show()

            }
            else
            {
                theviewMode.contentToBeAdeed=textField.editText!!.text.toString()
                    theProgress.setMessage("Posting")
                     theProgress.setCancelable(false)
                        theProgress.setCanceledOnTouchOutside(false)
                            theProgress.show()
                        theviewMode.uploadPostDetails()
            }




        }
        theviewMode.posted.observe(viewLifecycleOwner ,{
            it?.let {
                if (it)
                {
                    theProgress.dismiss()
                }
            }
        })
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
                transformations(RoundedCornersTransformation(5f))
            }
            theviewMode.imageURI=result.uri!!
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