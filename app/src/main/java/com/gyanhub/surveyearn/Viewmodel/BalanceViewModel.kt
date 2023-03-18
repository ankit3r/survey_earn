package com.gyanhub.surveyearn.Viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.gyanhub.surveyearn.model.BalanceModel

class BalanceViewModel : ViewModel() {
    private val balance = MutableLiveData<BalanceModel>()
    val getBalance: LiveData<BalanceModel>
        get() = balance


    private fun balanceManage(){
        FirebaseFirestore.getInstance().collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid.toString()).
            collection("balance").document("userbalance").
            get().
            addOnSuccessListener {
                if (it != null) {
                    balance.value = it.toObject(BalanceModel::class.java) ?: BalanceModel()
                }
            }
    }

    fun addBalance(balance:BalanceModel):Boolean{
        var returndata = false
        FirebaseFirestore.getInstance().collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid.toString()).
            collection("balance").document("userbalance").
            set(balance).addOnSuccessListener {
                returndata = true
            }.addOnFailureListener{
                returndata= false
           }
        return returndata
    }

    init {
        balanceManage()
    }

}