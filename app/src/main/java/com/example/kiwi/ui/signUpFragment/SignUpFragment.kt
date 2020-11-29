package com.example.kiwi.ui.signUpFragment

/*import com.example.kiwi.modules.FireBaseRefOfUsers*/
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

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    val theViewModel:SignUpModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val thisView=inflater.inflate(R.layout.fragment_sign_up,container,false)
        theViewModel.getTheItemsForFiewModel(thisView.passwordfromSignUP,thisView.theEmaileinputinsignUp,thisView.confirmpassword,thisView.userNamefirsttimeSignUp)
        return thisView
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        alreadyHaveaccount.setOnClickListener {

            findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLogInFragment())
        }

        theSignUp.setOnClickListener {

           var theAuth= theViewModel.createTheAuth()
            if (theAuth==null)
            {
                Snackbar.make(requireView(), "failed to create an account", Snackbar.LENGTH_SHORT).setBackgroundTint(requireContext().getColor(
                    R.color.colorAccent))
                    .show()
            }
            else
            {
                theAuth.addOnCompleteListener {
                    if (it.isSuccessful)
                    {
                       var theSafe= theViewModel.safeTheUserInDB()
                        if (theSafe==null)
                        {
                            Snackbar.make(requireView(), "failed to create an account", Snackbar.LENGTH_SHORT).setBackgroundTint(requireContext().getColor(
                                R.color.colorAccent))
                                .show()
                            theViewModel.signOut()

                        }
                        else
                        {
                            theSafe.addOnCompleteListener {it1->
                                if (it1.isSuccessful)
                                {

                                    Snackbar.make(requireView(), "account created successfully.." +
                                            "Please Log In", Snackbar.LENGTH_SHORT).setBackgroundTint(requireContext().getColor(
                                        R.color.colorAccent))
                                        .show()

                                    findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLogInFragment())
                                 //   theViewModel.cteateTheToken()
                                }
                                else
                                {
                                    Snackbar.make(requireView(), "failed to create an account", Snackbar.LENGTH_SHORT).setBackgroundTint(requireContext().getColor(
                                        R.color.colorAccent))
                                        .show()
                                    theViewModel.signOut()
                                }

                            }
                        }
                    }
                    else
                    {
                        Snackbar.make(requireView(), it.exception?.message.toString(), Snackbar.LENGTH_SHORT).setBackgroundTint(requireContext().getColor(
                            R.color.colorAccent))
                            .show()
                        theViewModel.signOut()

                        theViewModel.signOut()

                    }
                }
            }


        }

    }




}