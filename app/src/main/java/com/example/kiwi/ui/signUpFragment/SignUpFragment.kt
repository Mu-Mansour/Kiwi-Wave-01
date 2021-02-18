package com.example.kiwi.ui.signUpFragment

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.kiwi.R
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.fragment_sign_up.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    val theViewModel:SignUpModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        theViewModel.netWorkFound.value=isNetworkAvailable(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val thisView=inflater.inflate(R.layout.fragment_sign_up,container,false)
        return thisView
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        alreadyHaveaccount.setOnClickListener {

            findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLogInFragment())
        }
        theViewModel.netWorkFound.observe(viewLifecycleOwner,{
         it?.let {
            if (!it)
            {
               findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToNoInternet())

            }
         }
      })

        theSignUp.setOnClickListener {

            if (passwordfromSignUP.editText!!.text.isEmpty()
                || theEmaileinputinsignUp.editText!!.text.isEmpty()|| confirmpassword.editText!!.text.isEmpty()||
                userNamefirsttimeSignUp.editText!!.text.isEmpty())
            {
                Snackbar.make(requireView(), "Empty Field", Snackbar.LENGTH_SHORT).setBackgroundTint(requireContext().getColor(
                    R.color.colorAccent))
                    .show()
            }
            else if (passwordfromSignUP.editText!!.text.toString() != confirmpassword.editText!!.text.toString() )
            {
                Snackbar.make(requireView(), "check Your Confirmed password", Snackbar.LENGTH_SHORT).setBackgroundTint(requireContext().getColor(
                    R.color.colorAccent))
                    .show()
            }
            else
            {
                theViewModel.passwordfromSignIn=passwordfromSignUP.editText!!.text.toString()
                theViewModel.theEmaileinputinsignIn=theEmaileinputinsignUp.editText!!.text.toString()
                theViewModel.userNamefirsttime=userNamefirsttimeSignUp.editText!!.text.toString()
                CoroutineScope(Dispatchers.IO).launch {
                    theViewModel.createAnewAccount()
                }
            }

            theViewModel.theResultFromNewAccountCreation.observe(viewLifecycleOwner ,{
                it?.let {
                    if (it)
                    {
                        Snackbar.make(requireView(), "Account Created successfully ..please log in again", Snackbar.LENGTH_SHORT).setBackgroundTint(requireContext().getColor(
                            R.color.colorAccent))
                            .show()
                      //  theViewModel.signOut()
                        if (findNavController().currentDestination!!.id==R.id.signUpFragment) {
                            findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLogInFragment())
                        }
                    }
                    else
                    {
                        theViewModel.theError.observe(viewLifecycleOwner,{error1->
                            error1?.let {
                                Snackbar.make(requireView(), error1, Snackbar.LENGTH_SHORT).setBackgroundTint(requireContext().getColor(
                                    R.color.colorAccent))
                                    .show()
                            }
                        })
                    }
                }
            })

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