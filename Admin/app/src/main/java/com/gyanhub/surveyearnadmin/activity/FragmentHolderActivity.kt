package com.gyanhub.surveyearnadmin.activity


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.gyanhub.surveyearnadmin.R
import com.gyanhub.surveyearnadmin.databinding.ActivityFragmentHolderBinding
import com.gyanhub.surveyearnadmin.fragments.holder.UploadProgrammesFragment
import com.gyanhub.surveyearnadmin.fragments.holder.UserDetailsFragment
import com.gyanhub.surveyearnadmin.fragments.main.UsersFragment

class FragmentHolderActivity : AppCompatActivity() {
    private var id: Int = 0
    private var docID: String = ""
    private var userID: String = ""
    private lateinit var binding: ActivityFragmentHolderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFragmentHolderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainFun()

    }


    private fun mainFun() {
        binding.back.setOnClickListener{
            onBackPressed()
        }
        id = intent.getIntExtra("id", 0)
        when (id) {
            0 -> {
                loadFragment(UploadProgrammesFragment())
                binding.title.text = getString(R.string.uploadProgrammes)
            }
            1 -> {
                docID = intent.getStringExtra("docID").toString()
                loadFragment(UsersFragment(docID,1))
                binding.title.text = getString(R.string.selectUser)
            }
            2->{
                userID = intent.getStringExtra("userID").toString()
                loadFragment(UserDetailsFragment(userID))
                binding.title.text = getString(R.string.userDetails)
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        if (supportFragmentManager.findFragmentById(R.id.holderContainer) != null) {
            supportFragmentManager.popBackStack()
        } else {
            val fragmentTransient = supportFragmentManager.beginTransaction()
            fragmentTransient.add(R.id.holderContainer, fragment)
            fragmentTransient.commit()
        }
    }

}