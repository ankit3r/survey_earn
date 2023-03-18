@file:Suppress("DEPRECATION")

package com.gyanhub.surveyearn.activitys

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.gyanhub.surveyearn.R
import com.gyanhub.surveyearn.Viewmodel.GetDatas
import com.gyanhub.surveyearn.databinding.ActivityMainBinding
import com.gyanhub.surveyearn.fragments.mainActivityFragments.AccountFragment
import com.gyanhub.surveyearn.fragments.mainActivityFragments.ProgramsFragment
import com.gyanhub.surveyearn.fragments.mainActivityFragments.RewardFragment
import com.gyanhub.surveyearn.model.BalanceModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var balanceViewModel: GetDatas
    private val balance = MutableLiveData<BalanceModel>()
    private val getBalance: LiveData<BalanceModel>
        get() = balance
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        balanceManage()
        getBalance.observe(this){
            binding.txtBalance.text = it.points.toString()
        }

        FirebaseFirestore.getInstance().collection("users")
            .document(firebaseAuth.currentUser?.uid.toString()).get().addOnSuccessListener {
                if (it.data?.get("userCountry").toString().isEmpty()) {
                    val i = Intent(this, SettingActivity::class.java)
                    i.putExtra("id", 6)
                    i.putExtra("name", "${it.data?.get("userName")}")
                    i.putExtra("email", "${it.data?.get("userEmail")}")
                    startActivity(i)
                }
            }
        binding.titles.text = getString(R.string.program)
        MobileAds.initialize(this)
        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.program -> loadFragment(ProgramsFragment())
                R.id.Rewards -> loadFragment(RewardFragment())
                R.id.Account -> loadFragment(AccountFragment())
            }
            true
        }
//        binding.notiBell.setOnClickListener {
//            val i = Intent(this, SettingActivity::class.java)
//            i.putExtra("id", 5)
//            startActivity(i)
//        }
        if (supportFragmentManager.findFragmentById(R.id.container) == null) {
            supportFragmentManager.beginTransaction().replace(R.id.container, ProgramsFragment())
                .commit()
        }
        CoroutineScope(Dispatchers.IO).launch {
            balanceViewModel = ViewModelProvider(this@MainActivity)[GetDatas::class.java]
        }


    }

    private fun loadFragment(fragment: Fragment) {
        val fragmentTransient = supportFragmentManager.beginTransaction()
        fragmentTransient.replace(R.id.container, fragment)
        fragmentTransient.commit()

    }

  override fun onBackPressed() {
        if (binding.bottomNav.selectedItemId == R.id.program) {
            dialog()
        } else {
            binding.bottomNav.selectedItemId = R.id.program
        }
    }

     private fun dialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Do you want to Exit?")
            .setCancelable(true)
            .setPositiveButton("Proceed") { _, _ ->
                super.onBackPressed()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }

        val alert = dialogBuilder.create()
        alert.setTitle("Exit Alert")
        alert.show()
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


