package com.gyanhub.surveyearnadmin.fragments.holder

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.gyanhub.surveyearnadmin.databinding.FragmentUploadProgrammesBinding
import com.gyanhub.surveyearnadmin.factory.Factory
import com.gyanhub.surveyearnadmin.model.ProgrammesModel
import com.gyanhub.surveyearnadmin.viewModel.SetProgrammesData
import java.util.UUID.randomUUID

class UploadProgrammesFragment : Fragment() {
    private lateinit var binding: FragmentUploadProgrammesBinding
    private lateinit var addProgrammes: SetProgrammesData
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUploadProgrammesBinding.inflate(layoutInflater, container, false)
        addProgrammes = ViewModelProvider(
            this,
            Factory(context as Activity, binding.layoutLoading, "addProgram")
        )[SetProgrammesData::class.java]
        mainFun()
        return binding.root
    }

    private fun mainFun() {
        binding.btnUpload.setOnClickListener {
            takeInput()
        }
    }

    private fun takeInput() {
        if (binding.eTxtID.text.toString().isEmpty()) binding.eTxtID.error = "Enter ID"
        else if (binding.eTxtTitle.text.toString().isEmpty())
            binding.eTxtTitle.error = "Enter Title"
        else if (binding.eTxtDisplayTitle.text.toString().isEmpty())
            binding.eTxtDisplayTitle.error = "Enter Display Title"
        else if (binding.eTxtEarnPoints.text.toString().isEmpty())
            binding.eTxtEarnPoints.error = "Enter Earn Points"
        else if (binding.eTxtExpiry.text.toString().isEmpty())
            binding.eTxtExpiry.error = "Enter Expiry"
        else if (binding.eTxtTotalTask.text.toString().isEmpty())
            binding.eTxtTotalTask.error = "Enter Total Task"
        else if (binding.eTxtTakeTime.text.toString().isEmpty())
            binding.eTxtTakeTime.error = "Enter Take Time"
        else if (binding.eTxtUrl.text.toString().isEmpty())
            binding.eTxtUrl.error = "Enter Survey Url"
        val uUID = randomUUID().toString()
        addProgrammes.addProgrammesInDB(
            ProgrammesModel(
                binding.eTxtID.text.toString(),
                binding.eTxtTitle.text.toString(),
                binding.eTxtDisplayTitle.text.toString(),
                Integer.parseInt(binding.eTxtEarnPoints.text.toString()),
                binding.eTxtExpiry.text.toString(),
                binding.eTxtTotalTask.text.toString(),
                binding.eTxtTakeTime.text.toString(),
                binding.eTxtUrl.text.toString(),
                uUID
            ), uUID
        )
    }


}