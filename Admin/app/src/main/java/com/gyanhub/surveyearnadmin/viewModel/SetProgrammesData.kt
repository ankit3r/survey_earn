package com.gyanhub.surveyearnadmin.viewModel


import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.gyanhub.surveyearnadmin.model.ProgrammesModel

class SetProgrammesData(private val context: Context, private val view: LinearLayout) :
    ViewModel() {
    val coll = "Programmes"
    val db = FirebaseFirestore.getInstance()
    fun addProgrammesInDB(programm: ProgrammesModel, uid: String): Boolean {
        view.visibility = View.VISIBLE
        var returnData = false
        db.collection(coll).document(uid)
            .set(programm).addOnSuccessListener {
            view.visibility = View.GONE
            Toast.makeText(context, "Uploading Successful", Toast.LENGTH_SHORT).show()
            returnData = true
        }.addOnFailureListener {
            view.visibility = View.GONE
            returnData = false
            Toast.makeText(context, "Uploading Failed", Toast.LENGTH_SHORT).show()
        }
        return returnData
    }
    fun addProgrammesInUserDB(programm: ProgrammesModel, uid: String,userDocID:String){
        db.collection("users")
            .document(userDocID).
            collection("userProgram").document(uid)
            .set(programm).addOnSuccessListener {
                Toast.makeText(context, "Uploading Successful", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(context, "Uploading Failed", Toast.LENGTH_SHORT).show()
            }
    }
}