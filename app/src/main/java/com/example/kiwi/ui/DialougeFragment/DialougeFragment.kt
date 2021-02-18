package com.example.kiwi.ui.DialougeFragment

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DialogueFragment() : AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val theBuilder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            theBuilder.setTitle("Confirmation").setMessage("If you pressed ok some of  your saved data will be deleted from this device " +
                    "and Kiwi will shut down")
                .setPositiveButton("Confirm ") { _, _ ->
                    CoroutineScope(Dispatchers.IO).launch {

                        FirebaseDatabase.getInstance().reference.child("UsersTokens").child(FirebaseAuth.getInstance().currentUser!!.uid).removeValue().addOnSuccessListener {
                            FirebaseAuth.getInstance().signOut()
                        }
                        withContext(Dispatchers.Main){
                            requireActivity().finish()
                        }
                    }
                }.setNegativeButton("Cancel", DialogInterface.OnClickListener { _, _ ->
                    Toast.makeText(requireContext(), "Canceled", Toast.LENGTH_SHORT).show()

                })
        return theBuilder.create()
    }
}