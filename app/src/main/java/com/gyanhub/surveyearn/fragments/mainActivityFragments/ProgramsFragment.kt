package com.gyanhub.surveyearn.fragments.mainActivityFragments

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.gyanhub.surveyearn.R
import com.gyanhub.surveyearn.activitys.MainActivity
import com.gyanhub.surveyearn.activitys.MassageHolderActivity
import com.gyanhub.surveyearn.activitys.SettingActivity
import com.gyanhub.surveyearn.adapter.OnClick
import com.gyanhub.surveyearn.adapter.ProgramAdapter
import com.gyanhub.surveyearn.databinding.FragmentProgramsBinding
import com.gyanhub.surveyearn.model.UserProgrammes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProgramsFragment : Fragment(), OnClick {
    private lateinit var binding: FragmentProgramsBinding
    private val data = ArrayList<UserProgrammes>()
    private lateinit var ref: FirebaseStorage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProgramsBinding.inflate(layoutInflater, container, false)
        (context as MainActivity).binding.titles.text = getString(R.string.program)
        (context as MainActivity).binding.txtBalance.visibility = View.VISIBLE
//        (context as MainActivity).binding.notiBell.visibility = View.VISIBLE
        ref = FirebaseStorage.getInstance()
        CoroutineScope(Dispatchers.IO).launch {
            getData()
        }

        binding.btnLearnMore.setOnClickListener {
            binding.progressBar2.visibility = View.VISIBLE
            val i = Intent(context as MainActivity, SettingActivity::class.java)
            i.putExtra("id", 5)
            binding.progressBar2.visibility = View.GONE
            (context as MainActivity).startActivity(i)
        }
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun click(position: Int, textView: TextView) {
        binding.progressBar2.visibility = View.VISIBLE
        (context as MainActivity).balanceViewModel.getBalance.observe(viewLifecycleOwner) { balance ->
            val i = Intent(context as Activity, MassageHolderActivity::class.java)
            i.putExtra("url", data[position].url)
            i.putExtra("Title", data[position].title)
            i.putExtra("earn", data[position].earnP)
            i.putExtra("docID", data[position].docID)
            i.putExtra("oldPoints", balance.points)
            i.putExtra("oldBalance", balance.mainBalance)
            i.putExtra("programmesId", data[position].programID)
            (context as MainActivity).startActivity(i)
            binding.progressBar2.visibility = View.GONE
        }
    }

    private fun getData() {
        binding.progressBar2.visibility = View.VISIBLE
        FirebaseFirestore.getInstance().collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid.toString()).
            collection("userProgram")
            .get().addOnSuccessListener { document ->
                document?.let {
                    for (snapshot in it) {
                        data.add(snapshot.toObject(UserProgrammes::class.java))
                    }
                    if (data.isNotEmpty()) {
                        binding.progressBar2.visibility = View.GONE
                        binding.layout.visibility = View.GONE
                        binding.rcLayout.visibility = View.VISIBLE
                        binding.rcPrograms.layoutManager = LinearLayoutManager(activity)
                        val adapter = ProgramAdapter(data, context as Activity, this)
                        binding.rcPrograms.adapter = adapter
                    } else {
                        binding.progressBar2.visibility = View.GONE
                        binding.layout.visibility = View.VISIBLE
                        binding.rcLayout.visibility = View.GONE
                    }
                }
            }.addOnFailureListener {
                binding.progressBar2.visibility = View.GONE
                binding.layout.visibility = View.VISIBLE
            }
    }


}