package com.gyanhub.surveyearnadmin.activity

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Keep
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.gyanhub.surveyearnadmin.databinding.ActivityLoginBinding
import com.gyanhub.surveyearnadmin.model.UserModelModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            login(
                binding.editTextPhone.text.toString(),
                binding.edTxtPassword.text.toString(),
                binding.prog,binding.txtForgot
            )
        }

    }
   private fun login(email: String, pass: String, view: LinearLayout, forgot: TextView) {
        view.visibility = View.VISIBLE
        firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                view.visibility = View.GONE
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }.addOnFailureListener {
            view.visibility = View.GONE
            forgot.visibility = View.VISIBLE
            Log.d("ANKIT",it.message.toString())
            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
        }
    }
}