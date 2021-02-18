package com.example.kiwi.ui.DialougeFragment

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.kiwi.Repos.TheAppRepo
import com.example.kiwi.RoomDataBase.ThePostForRoom
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DialogueFragmentForProfile(private var  thepost:ThePostForRoom) : AppCompatDialogFragment() {

    @Inject
    lateinit var theAppRepo: TheAppRepo

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val theBuilder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            theBuilder.setTitle("Confirmation").setMessage("Confirm Deleting this Post?!!")
                .setPositiveButton("Confirm ") { _, _ ->

                    CoroutineScope(Dispatchers.IO).launch {

                        theAppRepo.theFinalDelete(thepost)


                    }
                }.setNegativeButton("Cancel", DialogInterface.OnClickListener { _, _ ->
                    Toast.makeText(requireContext(), "Canceled", Toast.LENGTH_SHORT).show()

                })
return theBuilder.create()
    }
}