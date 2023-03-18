package com.gyanhub.surveyearnadmin.fragments.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gyanhub.surveyearnadmin.activity.FragmentHolderActivity
import com.gyanhub.surveyearnadmin.activity.MainActivity
import com.gyanhub.surveyearnadmin.adapter.OnClick
import com.gyanhub.surveyearnadmin.adapter.ProgrammAdapter
import com.gyanhub.surveyearnadmin.databinding.FragmentProgramesBinding
import com.gyanhub.surveyearnadmin.factory.Factory
import com.gyanhub.surveyearnadmin.viewModel.GetProgrammesData

class ProgrammesFragment : Fragment(), OnClick {
    private lateinit var binding: FragmentProgramesBinding
    private lateinit var programmesData: GetProgrammesData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProgramesBinding.inflate(layoutInflater, container, false)
        mainFun()
        return binding.root
    }

    private fun mainFun() {
        binding.floatingActionButton.setOnClickListener {
            val i = Intent(context, FragmentHolderActivity::class.java)
            i.putExtra("id", 0)
            (context as MainActivity).startActivity(i)
        }
        rcViewSet()

    }

    private fun rcViewSet() {
        programmesData = ViewModelProvider(
            this,
            Factory(context as Activity, binding.layoutLoading, "programmes")
        )[GetProgrammesData::class.java]
        binding.rcProgrammes.layoutManager = LinearLayoutManager(activity)
        programmesData.getProgrammes.observe(viewLifecycleOwner) {
            it?.let {
                val adapter = ProgrammAdapter(it, context as Activity, this)
                binding.rcProgrammes.adapter = adapter
            }
        }
    }

    override fun click(docID: String, textView: TextView) {
        val i = Intent(context, FragmentHolderActivity::class.java)
        i.putExtra("id", 1)
        i.putExtra("docID", docID)
        (context as MainActivity).startActivity(i)
    }

}