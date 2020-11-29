package com.example.kiwi.ui.logIn

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController

import com.example.kiwi.R
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_log_in.*
import kotlinx.android.synthetic.main.fragment_log_in.view.*

@AndroidEntryPoint
class LogInFragment : Fragment(R.layout.fragment_log_in) {


val theViewMode:LoginFragmentModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val theView = inflater.inflate(R.layout.fragment_log_in,container,false)
        theViewMode.getTheEamailModel(theView.theEmaileinputinsignUp)
        theViewMode.getPasswordforViewmodel(theView.passwordfromSignUP)
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
                Snackbar.make(requireView(), "Please Enter Your Account Details", Snackbar.LENGTH_SHORT).setBackgroundTint(requireContext().getColor(R.color.colorAccent))
                    .show()
            }
            else
            {
                theViewMode.signInForFragment().addOnCompleteListener {
                    if (it.isSuccessful)
                    {

                        if (findNavController().currentDestination?.id==R.id.logInFragment) {
                            findNavController().navigate(LogInFragmentDirections.actionLogInFragmentToProfileragment2())
                        }
                        //
                        //
                        // theViewMode.runTheServiceForFetching()
                    }
                    else
                    {
                        it.exception?.message?.let { it1 ->
                            Snackbar.make(requireView(), it1, Snackbar.LENGTH_SHORT).setBackgroundTint(requireContext().getColor(R.color.colorAccent))
                                .show()
                        }
                    }
                }
            }




        }
    }




}