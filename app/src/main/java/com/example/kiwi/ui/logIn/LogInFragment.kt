package com.example.kiwi.ui.logIn

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController

import com.example.kiwi.R
import com.example.kiwi.ui.HomeKiwiMessages.HomeMessagesFragmentDirections
import com.example.kiwi.ui.home.HomeFragmentDirections
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_log_in.*
import kotlinx.android.synthetic.main.fragment_log_in.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LogInFragment : Fragment(R.layout.fragment_log_in) {


val theViewMode:LoginFragmentModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        theViewMode.netWorkFound.value=isNetworkAvailable(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val theView = inflater.inflate(R.layout.fragment_log_in,container,false)
        theViewMode.netWorkFound.observe(viewLifecycleOwner,{
            it?.let {
                if (!it)
                {
                    findNavController().navigate(LogInFragmentDirections.actionLogInFragmentToNoInternet())

                }
            }
        })
        return theView
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signUpBtn.setOnClickListener {

          findNavController().navigate(LogInFragmentDirections.actionLogInFragmentToSignUpFragment())
        }

        logInBtn.setOnClickListener {

            if (theEmaileinputinsignUp.editText!!.text.isEmpty()  ||  passwordfromSignUP.editText!!.text.isEmpty())
            {

                Toast.makeText(requireContext(), "Please Enter Your Account Details", Toast.LENGTH_SHORT).show()
            }
            else
            {
              theViewMode.theEmail=theEmaileinputinsignUp.editText!!.text.toString()
              theViewMode.thepassword=passwordfromSignUP.editText!!.text.toString()
               lifecycleScope.launch {
                   theViewMode.signInForFragment()
               }
            }




        }

        theViewMode.loginResult.observe(viewLifecycleOwner ,{
            it?.let {
                if (it)
                {
                    theViewMode.findIfThereArePostsForThisUser()
                }
                else
                {
                    theViewMode.loginEroor.observe(viewLifecycleOwner,{error1->
                        error1?.let {
                            Snackbar.make(requireView(), error1, Snackbar.LENGTH_SHORT).setBackgroundTint(requireContext().getColor(R.color.colorAccent))
                                .show()
                        }

                    })

                }
            }
        })

        theViewMode.thereArePostsForThisUser.observe(viewLifecycleOwner,{
            it?.let {
                if (it)
                {
                    //show user fetching
                    findNavController().navigate(LogInFragmentDirections.actionLogInFragmentToProfileragment2())

                }
                else
                {
                    findNavController().navigate(LogInFragmentDirections.actionLogInFragmentToProfileragment2())
                }
            }
        })
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