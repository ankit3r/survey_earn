package com.gyanhub.surveyearn.fragments.loginActivityFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gyanhub.surveyearn.R
import com.gyanhub.surveyearn.databinding.FragmentIntroBinding

class IntroFragment : Fragment() {
    private lateinit var binding : FragmentIntroBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       binding = FragmentIntroBinding.inflate(layoutInflater,container,false)
        binding.btnLogin.setOnClickListener {
            setFragment(LoginFragment(),"login")
        }
        binding.btnCreate.setOnClickListener {
            setFragment(SignUpFragment(),"sign")
        }

        return binding.root
    }
    private fun setFragment(fragment: Fragment, tag: String) {
        val fragmentTransient = parentFragmentManager.beginTransaction()
        fragmentTransient.replace(R.id.frameLayout, fragment, tag)
        fragmentTransient.addToBackStack(tag)
        fragmentTransient.commit()
    }
}