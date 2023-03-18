package com.gyanhub.surveyearn.fragments.loginActivityFragment


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.gyanhub.surveyearn.R
import com.gyanhub.surveyearn.activitys.LoginActivity
import com.gyanhub.surveyearn.activitys.MainActivity
import com.gyanhub.surveyearn.databinding.FragmentOtpBinding


class OtpFragment : Fragment() {
   private lateinit var binding:FragmentOtpBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View{

        binding = FragmentOtpBinding.inflate(layoutInflater,container,false)
      (context as LoginActivity).window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        binding.txtNumber.text = String.format(getString(R.string.send_number), 2945476)
        binding.txtTimer.text = String.format(getString(R.string.timer), 30)
        binding.btnSubmit.setOnClickListener {
            (context as LoginActivity).startActivity(Intent(context as Activity,MainActivity::class.java))
            (context as LoginActivity).finish()
        }


        return binding.root
    }

}