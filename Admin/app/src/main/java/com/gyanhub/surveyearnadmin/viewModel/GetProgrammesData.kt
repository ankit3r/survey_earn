package com.gyanhub.surveyearnadmin.viewModel

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.gyanhub.surveyearnadmin.model.ProgrammesModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GetProgrammesData(private val context: Context, private val view: LinearLayout) :ViewModel(){
    private val db = FirebaseFirestore.getInstance()
    private val programmesMutableData: MutableLiveData<ArrayList<ProgrammesModel>?> = MutableLiveData()
    private val addProgrammesInMutableData = ArrayList<ProgrammesModel>()
    val getProgrammes: LiveData<ArrayList<ProgrammesModel>?>
        get() = programmesMutableData

    private fun getProgrammesDataFromDB() {
        view.visibility = View.VISIBLE
        db.collection("Programmes").get()
            .addOnSuccessListener {
                it?.let {
                    for (user in it) {
                        addProgrammesInMutableData.add(user.toObject(ProgrammesModel::class.java))
                    }
                    view.visibility = View.GONE
                    programmesMutableData.value = addProgrammesInMutableData
                }
            }.addOnFailureListener {
                view.visibility = View.GONE
            }
    }

    init {
        CoroutineScope(Dispatchers.IO).launch {
            getProgrammesDataFromDB()
        }
    }

}