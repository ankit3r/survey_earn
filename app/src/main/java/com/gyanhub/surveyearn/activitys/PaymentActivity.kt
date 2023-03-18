package com.gyanhub.surveyearn.activitys

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.gyanhub.surveyearn.R
import com.gyanhub.surveyearn.databinding.ActivityPaymentBinding
import com.gyanhub.surveyearn.fragments.AdSurveyFragment


class PaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentBinding
    private var mainBalance: Int = 0


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        balanceManage()
        binding.back.visibility = View.VISIBLE
        binding.back.setOnClickListener {
            onBackPressed()
        }

        mainBalance = intent.getIntExtra("mainBalance", 0)


        binding.btnShop.setOnClickListener {
            loadFragment(AdSurveyFragment(mainBalance))
        }


    }

    private fun loadFragment(fragment: Fragment) {
        val fragmentTransient = supportFragmentManager.beginTransaction()
        fragmentTransient.replace(R.id.container, fragment)
        fragmentTransient.addToBackStack(null)
        fragmentTransient.commit()
    }

     @SuppressLint("SetTextI18n")
     fun balanceManage(){
        FirebaseFirestore.getInstance().collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid.toString()).
            collection("balance").document("userbalance").
            get().
            addOnSuccessListener {
                if (it != null) {
                    if (it.data?.get("mainBalance").toString().isNotEmpty()){
                        binding.txtxBalance.text = "$${it.data?.get("mainBalance")}"
                    }else{
                        binding.txtxBalance.text = "$0"
                    }

                }
            }.addOnFailureListener{
                binding.txtxBalance.text = "$0"
            }
    }

}