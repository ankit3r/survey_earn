package com.gyanhub.surveyearnadmin.viewModel

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.gyanhub.surveyearnadmin.model.UserModelModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GetUserData(private val context: Context, private val view: LinearLayout) : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val userMutableData: MutableLiveData<ArrayList<UserModelModel>?> = MutableLiveData()
    private val addUserInMutableData = ArrayList<UserModelModel>()
    val getUser: LiveData<ArrayList<UserModelModel>?>
        get() = userMutableData

    private fun getUserDataFromDB() {
        view.visibility = View.VISIBLE
        db.collection("users").get()
            .addOnSuccessListener {
                it?.let {
                    for (user in it) {
                        addUserInMutableData.add(user.toObject(UserModelModel::class.java))
                    }
                    view.visibility = View.GONE
                    userMutableData.value = addUserInMutableData
                }
            }.addOnFailureListener {
                view.visibility = View.GONE
            }
    }

    init {
        CoroutineScope(Dispatchers.IO).launch {
            getUserDataFromDB()
        }
    }
}