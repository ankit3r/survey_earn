package com.gyanhub.surveyearn.fragments.setting

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.gyanhub.surveyearn.R
import com.gyanhub.surveyearn.databinding.FragmentPrivacySettingBinding
import com.gyanhub.surveyearn.fragments.massage.MassageViewFragment
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PrivacySettingFragment : Fragment() {
    private lateinit var binding: FragmentPrivacySettingBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPrivacySettingBinding.inflate(layoutInflater, container, false)
        binding.txtDataPolicy.setOnClickListener {
            FirebaseFirestore.getInstance().collection("settings")
                .document("settingsDOC").get().addOnSuccessListener {
                    setFragment(MassageViewFragment(it.data?.get("dataPolicy").toString()))
                }
        }
        binding.txtDownloadData.setOnClickListener{
            sendRequest()
        }

        return binding.root
    }

    private fun setFragment(fragment: Fragment) {
        val fragmentTransient = parentFragmentManager.beginTransaction()
        fragmentTransient.replace(R.id.container, fragment)
        fragmentTransient.addToBackStack(null)
        fragmentTransient.commit()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendRequest(){
        val time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy   hh:mm"))
        val request = hashMapOf(
            "request" to "Please Send My Data",
            "requestDate" to time,
            )
        FirebaseFirestore.getInstance().collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid.toString()).
            collection("request").document("MyDataSendMe").
            set(request).addOnSuccessListener {
                Toast.makeText(context, "We'll send your data in seven working days on email id.", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Toast.makeText(context, "Request failed please try again later.", Toast.LENGTH_SHORT).show()
            }

    }
}