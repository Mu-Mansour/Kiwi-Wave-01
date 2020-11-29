package com.example.kiwi.ui.DialougeFragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.navigation.fragment.findNavController
import com.example.kiwi.NoFCMService.FirebaseService
import com.example.kiwi.R
import com.example.kiwi.Repos.TheAppRepo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DialogueFragment() : AppCompatDialogFragment() {

    @Inject
    lateinit var theAppRepo: TheAppRepo

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val theBuilder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            theBuilder.setTitle("Confirmation").setMessage("If you pressed ok some of  your saved data will be deleted from this device " +
                    "and Kiwi will shut down")
                .setPositiveButton("Confirm ") { _, _ ->

                    CoroutineScope(Dispatchers.IO).launch {

                      //  theAppRepo.theFinalDeleteAll()
                        theAppRepo.signOut()


                        CoroutineScope(Dispatchers.Main) .launch {  requireActivity().finish()  }

                    }
                }.setNegativeButton("Cancel", DialogInterface.OnClickListener { _, _ ->
                    Toast.makeText(requireContext(), "Canceled", Toast.LENGTH_SHORT).show()

                })
return theBuilder.create()
    }
}