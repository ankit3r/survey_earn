package com.gyanhub.surveyearn.fragments.mainActivityFragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.gyanhub.surveyearn.R
import com.gyanhub.surveyearn.activitys.LoginActivity
import com.gyanhub.surveyearn.activitys.MainActivity
import com.gyanhub.surveyearn.activitys.PaymentActivity
import com.gyanhub.surveyearn.activitys.SettingActivity
import com.gyanhub.surveyearn.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {
    private lateinit var binding: FragmentAccountBinding
    private val amount = 10
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(layoutInflater, container, false)
        (context as MainActivity). binding.txtBalance.visibility = View.GONE
        // call requestIdToken as follows
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(context as Activity, gso)



        (context as MainActivity). binding.titles.text= getString(R.string.account)
//        (context as MainActivity).   binding.bottomNav.selectedItemId =  R.id.Account
        val i = Intent(context as MainActivity, SettingActivity::class.java)

        binding.btnEmailS.setOnClickListener {
            i.putExtra("id", 0)
            (context as MainActivity).startActivity(i)
        }
        binding.btnHelpS.setOnClickListener {
            i.putExtra("id", 1)
            (context as MainActivity).startActivity(i)
        }
        binding.btnPrivacyS.setOnClickListener {
            i.putExtra("id", 2)
            (context as MainActivity).startActivity(i)
        }
        binding.btnDeleteAccountS.setOnClickListener {
            i.putExtra("id", 3)
            (context as MainActivity).startActivity(i)
        }
        binding.btnTerm.setOnClickListener {
            i.putExtra("id", 4)
            (context as MainActivity).startActivity(i)
        }
        if (amount>5){
            binding.btnPayment.visibility = View.VISIBLE
        }
        binding.btnPayment.setOnClickListener {
            val intent = Intent(context, PaymentActivity::class.java)
            (context as MainActivity).balanceViewModel.getBalance.observe(viewLifecycleOwner){
                intent.putExtra("mainBalance",it.mainBalance)
            }
            (context as MainActivity).startActivity(intent)
        }
        binding.btnLogOut.setOnClickListener{
            Firebase.auth.signOut()
            mGoogleSignInClient.signOut().addOnCompleteListener {
                startActivity(Intent(context as Activity, LoginActivity::class.java))
                (context as MainActivity).finish()
            }
        }

        return binding.root
    }
}