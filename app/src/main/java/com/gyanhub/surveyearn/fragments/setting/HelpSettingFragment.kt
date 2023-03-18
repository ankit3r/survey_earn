package com.gyanhub.surveyearn.fragments.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.FirebaseFirestore
import com.gyanhub.surveyearn.R
import com.gyanhub.surveyearn.databinding.FragmentHelpSettingBinding
import com.gyanhub.surveyearn.fragments.massage.MassageViewFragment

class HelpSettingFragment : Fragment() {
    private lateinit var binding: FragmentHelpSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHelpSettingBinding.inflate(layoutInflater, container, false)
        binding.txtHelpCenter.setOnClickListener{
            FirebaseFirestore.getInstance().collection("settings")
                .document("settingsDOC").get().addOnSuccessListener {
                    setFragment(MassageViewFragment(it.data?.get("help").toString()))
                }
        }
        binding.txtPaymentHelp.setOnClickListener{
            FirebaseFirestore.getInstance().collection("settings")
                .document("settingsDOC").get().addOnSuccessListener {
                    setFragment(MassageViewFragment(it.data?.get("paymentSettings").toString()))
                }
        }



        return binding.root
    }
    private fun setFragment(fragment: Fragment) {
        val fragmentTransient = parentFragmentManager.beginTransaction()
        fragmentTransient.replace(R.id.container, fragment)
        fragmentTransient.addToBackStack(null)
        fragmentTransient.commit()
    }

}