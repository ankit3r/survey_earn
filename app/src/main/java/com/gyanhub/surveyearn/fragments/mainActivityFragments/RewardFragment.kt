package com.gyanhub.surveyearn.fragments.mainActivityFragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.gyanhub.surveyearn.R
import com.gyanhub.surveyearn.activitys.MainActivity
import com.gyanhub.surveyearn.adapter.HistoryAdapter
import com.gyanhub.surveyearn.databinding.FragmentRewardBinding
import com.gyanhub.surveyearn.model.BalanceModel
import com.gyanhub.surveyearn.model.HistoryModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class RewardFragment : Fragment() {
    private lateinit var binding: FragmentRewardBinding
    private val data = ArrayList<HistoryModel>()
    private lateinit var ref: FirebaseStorage
    private lateinit var firebaseAuth: FirebaseAuth
    private val balance = MutableLiveData<BalanceModel>()
    private val getBalance: LiveData<BalanceModel>
        get() = balance

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRewardBinding.inflate(layoutInflater, container, false)
        (context as MainActivity).binding.titles.text = getString(R.string.reward)
        (context as MainActivity). binding.txtBalance.visibility = View.GONE
        ref = FirebaseStorage.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        balanceManage()

        getBalance.observe(viewLifecycleOwner) {
            binding.progressBar.progress = (it.points * 100) / 2000
            binding.txtPoint.text = "${it.points} of 2000"
        }
        CoroutineScope(Dispatchers.IO).launch {
            addData()
        }

        return binding.root
    }

    private fun addData() {
        binding.progressBar2.visibility = View.VISIBLE
        FirebaseFirestore.getInstance().collection("users")
            .document(firebaseAuth.currentUser?.uid.toString()).collection("history")
            .orderBy("date", Query.Direction.DESCENDING)
            .get().addOnSuccessListener { document ->
                document?.let {
                    for (snapshot in it) {
                        data.add(snapshot.toObject(HistoryModel::class.java))
                    }
                    binding.rcHistory.layoutManager = LinearLayoutManager(activity)
                    val adapter = HistoryAdapter(data)
                    binding.rcHistory.adapter = adapter
                    binding.progressBar2.visibility = View.GONE
                }
            }.addOnFailureListener {
                binding.progressBar2.visibility = View.GONE
            }
    }

    private fun balanceManage() {
        FirebaseFirestore.getInstance().collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid.toString()).collection("balance")
            .document("userbalance").get().addOnSuccessListener {
                if (it != null) {
                    balance.value = it.toObject(BalanceModel::class.java) ?: BalanceModel()
                }
            }
    }
}