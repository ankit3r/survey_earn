package com.gyanhub.surveyearn.Viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.gyanhub.surveyearn.model.BalanceModel
import com.gyanhub.surveyearn.model.HistoryModel
import com.gyanhub.surveyearn.model.UserProgrammes

class GetDatas : ViewModel() {

    //**********************************************************************************************************************************//
    // ---------------------------   Balance get methode ----------------------------------------------//
    private val balance = MutableLiveData<BalanceModel>()
    val getBalance: LiveData<BalanceModel>
        get() = balance

    private fun balanceManage() {
        FirebaseFirestore.getInstance().collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid.toString()).collection("balance")
            .document("userbalance").get().addOnSuccessListener {
                if (it != null) {
                    balance.value = it.toObject(BalanceModel::class.java) ?: BalanceModel()
                }
            }
    }

    //**********************************************************************************************************************************//
    // ---------------------------   history get methode ----------------------------------------------//


    private val historys: MutableLiveData<ArrayList<HistoryModel>?> = MutableLiveData()
    private val addHistoryDataInModel = ArrayList<HistoryModel>()
    val getHistory: LiveData<ArrayList<HistoryModel>?>
        get() = historys

    private fun addHistoryData() {
        FirebaseFirestore.getInstance().collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid.toString()).collection("history")
            .get().addOnSuccessListener { document ->
                document?.let {
                    for (snapshot in it) {
                        addHistoryDataInModel.add(snapshot.toObject(HistoryModel::class.java))
                    }
                }
            }
    }
    //**********************************************************************************************************************************//
    // ---------------------------   Programmes get methode ----------------------------------------------//

    private val programmes: MutableLiveData<ArrayList<UserProgrammes>?> = MutableLiveData()
    private val addProgramDataInModel = ArrayList<UserProgrammes>()
    val getProgramm: LiveData<ArrayList<UserProgrammes>?>
        get() = programmes

    private fun addProgrammesData() {
        FirebaseFirestore.getInstance().collection("SurveyProgram")
            .get().addOnSuccessListener { document ->
                document?.let {
                    for (snapshot in it) {
                        addProgramDataInModel.add(snapshot.toObject(UserProgrammes::class.java))
                    }

                }
            }
    }
    //**********************************************************************************************************************************//
    // ---------------------------   massages get methode ----------------------------------------------//




    //**********************************************************************************************************************************//
    // ---------------------------   Settings get methode ----------------------------------------------//
    init {
        balanceManage()
        addHistoryData()
        addProgrammesData()
    }
}