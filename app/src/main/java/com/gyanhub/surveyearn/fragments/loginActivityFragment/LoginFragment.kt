package com.gyanhub.surveyearn.fragments.loginActivityFragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.gyanhub.surveyearn.activitys.LoginActivity
import com.gyanhub.surveyearn.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        firebaseAuth = FirebaseAuth.getInstance()
        binding.back.setOnClickListener {
            (activity as LoginActivity).onBackPressed()
        }

        binding.btnLogin.setOnClickListener {
            hideKeyboard(it)
            checkInput()
        }
        binding.btnFBCo.setOnClickListener {
            (context as LoginActivity).signInGoogle()
        }
        binding.txtForgot.setOnClickListener {
            if (binding.editTextPhone.text.isEmpty()) {
                binding.editTextPhone.error = "Enter Register Email ID"
            } else {
                (context as LoginActivity).exit(binding.editTextPhone.text.toString(), binding.prog)
            }
        }
        return binding.root
    }

    private fun checkInput() {
        if (binding.editTextPhone.text.isEmpty()) {
            binding.editTextPhone.error = "Enter Register Email"
        } else if (binding.edTxtPassword.text.isEmpty()) {
            binding.edTxtPassword.error = "Enter Correct password"
        } else {
            (context as LoginActivity).login(
                binding.editTextPhone.text.toString(),
                binding.edTxtPassword.text.toString(),
                binding.prog, binding.txtForgot
            )
        }
    }

    private fun hideKeyboard(v: View) {
        val imm = ContextCompat.getSystemService(
            v.context,
            InputMethodManager::class.java
        ) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }

}