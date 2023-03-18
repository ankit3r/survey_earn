package com.gyanhub.surveyearn.fragments.massage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gyanhub.surveyearn.databinding.FragmentMassageBinding

class MassageFragment : Fragment() {
   private lateinit var binding : FragmentMassageBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentMassageBinding.inflate(layoutInflater,container,false)


        return binding.root
    }

}