@file:Suppress("DEPRECATION")

package com.gyanhub.surveyearn.activitys

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager.getDefaultSharedPreferences
import android.view.View
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.gyanhub.surveyearn.R
import com.gyanhub.surveyearn.databinding.ActivitySettingBinding
import com.gyanhub.surveyearn.fragments.loginActivityFragment.EmailLoginFragment
import com.gyanhub.surveyearn.fragments.massage.MassageViewFragment
import com.gyanhub.surveyearn.fragments.setting.DeleteAccountSettingFragment
import com.gyanhub.surveyearn.fragments.setting.EmailSettingFragment
import com.gyanhub.surveyearn.fragments.setting.HelpSettingFragment
import com.gyanhub.surveyearn.fragments.setting.PrivacySettingFragment

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding
    private var id = 0
    private var name: String = ""
    private var email: String = ""
    private var url: String = ""
    private lateinit var ref: FirebaseStorage
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startFromHere()


    }

    private fun startFromHere() {
        binding.tool.visibility = View.VISIBLE
        binding.back.setOnClickListener {
            onBackPressed()
        }
        ref = FirebaseStorage.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        sharedPreferences = getDefaultSharedPreferences(this)
        editor = sharedPreferences.edit()
        id = intent.getIntExtra("id", 0)
        name = intent.getStringExtra("name").toString()
        email = intent.getStringExtra("email").toString()
        url = intent.getStringExtra("url").toString()
        setFragment(id)
    }

    private fun setFragment(n: Int) {
        when (n) {
            0 -> {
                loadFragment(EmailSettingFragment())
                binding.txtTitle.text = getString(R.string.emailSettings)
            }
            1 -> {
                loadFragment(HelpSettingFragment())
                binding.txtTitle.text = getString(R.string.help)
            }
            2 -> {
                loadFragment(PrivacySettingFragment())
                binding.txtTitle.text = getString(R.string.privacy)
            }
            3 -> {
                loadFragment(DeleteAccountSettingFragment())
                binding.txtTitle.text = getString(R.string.deleteAccount)
            }
            4 -> {
                FirebaseFirestore.getInstance().collection("settings")
                    .document("settingsDOC").get().addOnSuccessListener {
                        loadFragment(MassageViewFragment(it.data?.get("termOfService").toString()))
                        binding.txtTitle.text = getString(R.string.helpCenter)
                    }.addOnFailureListener{
                        onBackPressed()
                    }

            }
            5 -> {
                FirebaseFirestore.getInstance().collection("settings")
                    .document("settingsDOC").get().addOnSuccessListener {
                        loadFragment(MassageViewFragment(it.data?.get("LearnMore").toString()))
                        binding.txtTitle.text = getString(R.string.aboutMor)
                    }.addOnFailureListener{
                        onBackPressed()
                    }
                binding.txtTitle.text = getString(R.string.learn_about_programs)
            }
            6 -> {
                loadFragment(EmailLoginFragment(email, "", "main", name))
                binding.tool.visibility = View.GONE
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        if (supportFragmentManager.findFragmentById(R.id.container) != null) {
            supportFragmentManager.popBackStack()
        } else {
            val fragmentTransient = supportFragmentManager.beginTransaction()
            fragmentTransient.add(R.id.container, fragment)
            fragmentTransient.commit()
        }
    }

    fun closeActivity(){
        startActivity(Intent(this,SplashActivity::class.java))
        finish()
    }

}