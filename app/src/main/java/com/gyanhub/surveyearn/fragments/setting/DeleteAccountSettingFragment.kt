@file:Suppress("DEPRECATION")

package com.gyanhub.surveyearn.fragments.setting

import android.app.Activity
import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager.getDefaultSharedPreferences
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.gyanhub.surveyearn.R
import com.gyanhub.surveyearn.activitys.SettingActivity
import com.gyanhub.surveyearn.databinding.FragmentDeleteAccountSettingBinding

@Suppress("DEPRECATION")
class DeleteAccountSettingFragment : Fragment() {
    private lateinit var binding: FragmentDeleteAccountSettingBinding
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDeleteAccountSettingBinding.inflate(layoutInflater, container, false)
        sharedPreferences = getDefaultSharedPreferences(context)
        editor = sharedPreferences.edit()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(context as Activity, gso)
        binding.btnDelete.setOnClickListener {
            dialog()

        }

        return binding.root
    }

    private fun deleteUserData() {
        FirebaseFirestore.getInstance().collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid.toString()).delete()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = Firebase.auth.currentUser!!
                    user.delete()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Firebase.auth.signOut()
                                mGoogleSignInClient.signOut().addOnCompleteListener {
                                    editor.putBoolean("done", false)
                                    editor.commit()
                                    (context as SettingActivity).closeActivity()
                                }
                            }
                        }
                }
            }
    }

    private fun dialog() {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setMessage("Do you want to Delete Your Account?")
            .setCancelable(false)
            .setPositiveButton("Proceed") { _, _ ->
                deleteUserData()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }

        val alert = dialogBuilder.create()
        alert.setTitle("Account Delete Alert")
        alert.show()
    }



}