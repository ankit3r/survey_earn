package com.gyanhub.surveyearnadmin.fragments.holder

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.gyanhub.surveyearnadmin.adapter.HistoryAdapter
import com.gyanhub.surveyearnadmin.adapter.RequestAdapter
import com.gyanhub.surveyearnadmin.databinding.FragmentUserDetailsBinding
import com.gyanhub.surveyearnadmin.model.UserBalanceModel
import com.gyanhub.surveyearnadmin.model.UserHistoryModel
import com.gyanhub.surveyearnadmin.model.userWithdrawModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserDetailsFragment(private val userID: String) : Fragment() {
    private lateinit var binding: FragmentUserDetailsBinding
    private var show = false

    // balance data
    private val balance = MutableLiveData<UserBalanceModel>()
    val getBalance: LiveData<UserBalanceModel>
        get() = balance

    // history data
    private val historys: MutableLiveData<ArrayList<UserHistoryModel>?> = MutableLiveData()
    private val addHistoryDataInModel = ArrayList<UserHistoryModel>()
    val getHistory: LiveData<ArrayList<UserHistoryModel>?>
        get() = historys

    //Request data
    private val request: MutableLiveData<ArrayList<userWithdrawModel>?> = MutableLiveData()
    private val addRequestData = ArrayList<userWithdrawModel>()
    val getRequest: LiveData<ArrayList<userWithdrawModel>?>
        get() = request

//-----------------------------------------------------------------------------------------------------//

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserDetailsBinding.inflate(layoutInflater, container, false)
        mainFun()
        return binding.root
    }

    private fun mainFun() {
        CoroutineScope(Dispatchers.IO).launch {
            getBalance()
            getRequest()
            getHistory()
        }
        buttonClick()
    }

    private fun buttonClick() {


        binding.txtViewBalance.setOnClickListener {
            hideAndShow(binding.viewBalance, !show)
        }
        binding.txtViewHistory.setOnClickListener {
            hideAndShow(binding.viewHistory, !show)
        }
        binding.txtViewRequest.setOnClickListener {
            hideAndShow(binding.viewRequest, !show)
        }

    }

    private fun hideAndShow(view: RelativeLayout, shows: Boolean) {

        if (shows) {
            view.visibility = View.VISIBLE
            show = true
        } else {
            view.visibility = View.GONE
            show = false
        }
    }

    private fun getHistory() {
        FirebaseFirestore.getInstance().collection("users")
            .document(userID).collection("history")
            .get().addOnSuccessListener { document ->
                document?.let {
                    for (snapshot in it) {
                        addHistoryDataInModel.add(snapshot.toObject(UserHistoryModel::class.java))
                    }
                    historys.value = addHistoryDataInModel
                    setHistory()
                }
            }
    }

    private fun setHistory() {
        binding.rcHistory.layoutManager = LinearLayoutManager(activity)
        getHistory.observe(viewLifecycleOwner) {
          it?.let {
              val adapter = HistoryAdapter(it)
              binding.rcHistory.adapter = adapter
          }
        }
    }

    private fun getRequest() {
        FirebaseFirestore.getInstance().collection("users")
            .document(userID).collection("request")
            .get().addOnSuccessListener {
                for (data in it) {
                    addRequestData.add(data.toObject(userWithdrawModel::class.java))
                }
                request.value = addRequestData
                setRequest()
            }
    }

    private fun setRequest(){
        binding.rcRequest.layoutManager = LinearLayoutManager(activity)
        getRequest.observe(viewLifecycleOwner){
           it?.let {
               val adapter = RequestAdapter(it)
               binding.rcRequest.adapter = adapter
           }
        }
    }

    private fun getBalance() {
        FirebaseFirestore.getInstance().collection("users")
            .document(userID).collection("balance")
            .document("userbalance").get().addOnSuccessListener {
                if (it != null) {
                    balance.value = it.toObject(UserBalanceModel::class.java)
                    setBalance()
                }
            }

    }

    @SuppressLint("SetTextI18n")
    private fun setBalance() {
        getBalance.observe(viewLifecycleOwner) {
            binding.txtUserBalance.text = "$${it.mainBalance}"
            binding.txtUserPoints.text = "$${it.points}"
        }
    }


}