package com.gyanhub.surveyearn.fragments.loginActivityFragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.RadioButton
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.Toast
import androidx.annotation.Keep
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.gyanhub.surveyearn.activitys.LoginActivity
import com.gyanhub.surveyearn.activitys.MainActivity
import com.gyanhub.surveyearn.activitys.SettingActivity
import com.gyanhub.surveyearn.databinding.FragmentEmailLoginBinding
import com.gyanhub.surveyearn.model.UserModel
import java.util.Calendar

class EmailLoginFragment(
    private val email: String,
    private val pass: String,
    private val id: String,
    private val name: String,
) : Fragment() {
    private lateinit var binding: FragmentEmailLoginBinding
    private lateinit var auth: FirebaseAuth
    private var d: Int = 0
    private var m: Int = 0
    private var y: Int = 0
    private var userID = ""
    private lateinit var radioButton: RadioButton


    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEmailLoginBinding.inflate(layoutInflater, container, false)
        auth = FirebaseAuth.getInstance()
        userID = FirebaseUtils().userId
        hideKeyboard(binding.root)
        binding.imgClander.setOnClickListener {
            val c = Calendar.getInstance()
            d = c.get(Calendar.DATE)
            m = c.get(Calendar.MONTH)
            y = c.get(Calendar.YEAR)
            hideKeyboard(it)

            val dpd = DatePickerDialog(context as Activity, { _, year, monthOfYear, dayOfMonth ->
                binding.eTxtDate.setText("$dayOfMonth - ${monthOfYear + 1} - $year")
            }, y, m, d)
            dpd.show()
        }

        if (id == "main") {
            binding.eTxtName.setText(name)
            binding.btnAddNext.setOnClickListener {
                radioButton = view?.findViewById(binding.group.checkedRadioButtonId)!!
                if (binding.eTxtName.text.isEmpty()) {
                    binding.eTxtName.error = "Enter Full Name"
                    return@setOnClickListener
                } else if (binding.eTxtCountyName.text.isEmpty()) {
                    binding.eTxtCountyName.error = "Enter Country Name"
                    return@setOnClickListener
                } else if (binding.eTxtDate.text.isEmpty()) {
                    binding.eTxtDate.error = "Enter Date of birth"
                    return@setOnClickListener
                } else {
                    binding.progress.visibility = View.VISIBLE
                    setYourData(
                        UserModel
                            (
                            binding.eTxtName.text.toString(),
                            radioButton.text.toString(),
                            binding.eTxtDate.text.toString(),
                            binding.eTxtCountyName.text.toString(),
                            email, FirebaseAuth.getInstance().currentUser?.uid.toString()
                        ),0
                    )
                }
            }
        } else {
            binding.btnAddNext.setOnClickListener {
                checkData()
            }
        }

        return binding.root
    }

    private fun checkData() {
        radioButton = view?.findViewById(binding.group.checkedRadioButtonId)!!
        if (binding.eTxtName.text.isEmpty()) {
            binding.eTxtName.error = "Enter Full Name"
            return
        } else if (binding.eTxtCountyName.text.isEmpty()) {
            binding.eTxtCountyName.error = "Enter Country Name"
            return
        } else if (binding.eTxtDate.text.isEmpty()) {
            binding.eTxtDate.error = "Enter Date of birth"
            return
        } else {

            binding.progress.visibility = View.VISIBLE
            register(
                email, pass
            )
        }
    }

    private fun setYourData(user: UserModel, id: Int) {
        FirebaseUtils().fireStoreDatabase.collection("users").document( FirebaseAuth.getInstance().currentUser?.uid.toString())
            .set(user).addOnSuccessListener {
                binding.progress.visibility = View.GONE
                if (id==0){
                    (context as SettingActivity).onBackPressed()
                }else{
                    (context as LoginActivity).startActivity(
                        Intent(
                            context as Activity,
                            MainActivity::class.java
                        )
                    )
                    (context as LoginActivity).finish()
                }
            }.addOnFailureListener {
                binding.progress.visibility = View.GONE
                Toast.makeText(context, it.localizedMessage, Toast.LENGTH_LONG).show()
            }

    }

    private fun register(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    (context as LoginActivity).addEmail(email)
                    setYourData(UserModel
                        (
                        binding.eTxtName.text.toString(),
                        radioButton.text.toString(),
                        binding.eTxtDate.text.toString(),
                        binding.eTxtCountyName.text.toString(),
                        email,FirebaseAuth.getInstance().currentUser?.uid.toString()
                    ),1)
                }
            }.addOnFailureListener { exception ->
                binding.progress.visibility = View.GONE
                Toast.makeText(context, exception.localizedMessage, Toast.LENGTH_LONG).show()
            }
    }

    private fun hideKeyboard(v: View) {
        val imm = ContextCompat.getSystemService(
            v.context,
            InputMethodManager::class.java
        ) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }
}

@Keep
class FirebaseUtils {
    val fireStoreDatabase = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid.toString()
}