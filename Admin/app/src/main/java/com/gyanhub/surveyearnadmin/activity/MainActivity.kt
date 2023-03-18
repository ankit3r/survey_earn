package com.gyanhub.surveyearnadmin.activity

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.gyanhub.surveyearnadmin.R
import com.gyanhub.surveyearnadmin.databinding.ActivityMainBinding
import com.gyanhub.surveyearnadmin.fragments.main.ProgrammesFragment
import com.gyanhub.surveyearnadmin.fragments.main.UsersFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        mainFun()
    }

    private fun mainFun() {


        if (supportFragmentManager.findFragmentById(R.id.container) == null) {
            supportFragmentManager.beginTransaction().replace(R.id.container, UsersFragment(num=0))
                .commit()
        }
        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.Account -> {
                    loadFragment(UsersFragment(num=0))
                    binding.titles.text = getString(R.string.account)
                }
                R.id.program -> {
                    loadFragment(ProgrammesFragment())
                    binding.titles.text = getString(R.string.programs)
                }
            }
            true
        }
    }



    private fun loadFragment(fragment: Fragment) {
        val fragmentTransient = supportFragmentManager.beginTransaction()
        fragmentTransient.replace(R.id.container, fragment)
        fragmentTransient.commit()
    }

    override fun onBackPressed() {
        if (binding.bottomNav.selectedItemId == R.id.Account) {
            dialog()
        } else {
            binding.bottomNav.selectedItemId = R.id.Account
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
}