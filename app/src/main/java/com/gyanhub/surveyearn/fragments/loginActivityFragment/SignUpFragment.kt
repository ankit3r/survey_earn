package com.gyanhub.surveyearn.fragments.loginActivityFragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.gyanhub.surveyearn.R
import com.gyanhub.surveyearn.activitys.LoginActivity
import com.gyanhub.surveyearn.databinding.FragmentSignUpBinding


class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)
        auth = FirebaseAuth.getInstance()
        binding.back.setOnClickListener {
            (activity as LoginActivity).onBackPressed()
        }
        binding.btnFBCo.setOnClickListener {
            (context as LoginActivity).signInGoogle()
        }
        hideKeyboard(binding.root)
        binding.btnSignUp.setOnClickListener {
            register()
            hideKeyboard(it)
        }

        return binding.root
    }

    private fun setFragment(fragment: Fragment) {
        val fragmentTransient = parentFragmentManager.beginTransaction()
        fragmentTransient.replace(R.id.frameLayout, fragment)
        fragmentTransient.commit()
    }

    private fun register() {

        if (binding.editTextPhone.text.toString().isEmpty()) {
            binding.editTextPhone.error = "Enter Email ID"
            return
        } else if (binding.edTxtPassword.text.toString().isEmpty()) {
            binding.edTxtPassword.error = "Enter Password"
            return
        } else {
            binding.prog.visibility = View.VISIBLE
            val email = binding.editTextPhone.text.toString()
            val password = binding.edTxtPassword.text.toString()
            setFragment(EmailLoginFragment(email,password,"",""))
        }
    }

    private fun hideKeyboard(v:View) {
        val imm =  getSystemService(v.context,InputMethodManager::class.java) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }

}