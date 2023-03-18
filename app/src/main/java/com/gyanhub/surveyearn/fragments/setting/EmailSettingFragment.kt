package com.gyanhub.surveyearn.fragments.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gyanhub.surveyearn.activitys.SettingActivity
import com.gyanhub.surveyearn.databinding.FragmentEmailSettingBinding

class EmailSettingFragment : Fragment() {
    private lateinit var binding: FragmentEmailSettingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentEmailSettingBinding.inflate(layoutInflater, container, false)
        binding.checkBox.isChecked = (context as SettingActivity).sharedPreferences.getBoolean("check", false)

        binding.userEmail.text = (context as SettingActivity).sharedPreferences.getString("shEmail", "").toString()
        binding.checkBox.setOnClickListener {
            if ((context as SettingActivity).sharedPreferences.getBoolean("check", false)) {
                addEmail(false)
            } else {
                addEmail(true)
            }
        }

        return binding.root
    }

  private fun addEmail(check: Boolean) {
        (context as SettingActivity).editor.putBoolean("check", check)
        (context as SettingActivity).editor.commit()
    }
}