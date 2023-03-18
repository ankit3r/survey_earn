@file:Suppress("DEPRECATION")

package com.gyanhub.surveyearn.activitys

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Keep
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.gyanhub.surveyearn.R
import com.gyanhub.surveyearn.databinding.ActivityLoginBinding
import com.gyanhub.surveyearn.fragments.loginActivityFragment.IntroFragment
import com.gyanhub.surveyearn.model.UserModel

@SuppressLint("SetTextI18n")

class LoginActivity : AppCompatActivity() {


    lateinit var binding: ActivityLoginBinding
    private val requestCodes: Int = 478

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var dialog: Dialog
    private var name: String = ""
    private var email: String = ""
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        editor = sharedPreferences.edit()


        FirebaseApp.initializeApp(this)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()





        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        firebaseAuth = FirebaseAuth.getInstance()


        if (supportFragmentManager.findFragmentById(R.id.frameLayout) != null) {
            supportFragmentManager.popBackStack()
        } else {
            val fragmentTransient = supportFragmentManager.beginTransaction()
            fragmentTransient.add(R.id.frameLayout, IntroFragment())
            fragmentTransient.commit()
        }
    }

    // login with google

    fun signInGoogle() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, requestCodes)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestCodes) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }


    private fun handleResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                addEmail(account.email.toString())
                email = account.email.toString()
                name = account.displayName.toString()
                updateUI(account)
            }
        } catch (e: ApiException) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    @Keep
    private fun updateUI(account: GoogleSignInAccount) {
        binding.prog.visibility = View.VISIBLE
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                checkMyDocument()
            }
        }.addOnFailureListener {
            binding.prog.visibility = View.GONE
        }
    }

    //Login with Email Or Password
    @Keep
    fun login(email: String, pass: String, view: LinearLayout, forgot: TextView) {
        view.visibility = View.VISIBLE
        firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                addEmail(email)
                view.visibility = View.GONE
                startActivity(Intent(this, MainActivity::class.java))
                finish()

            }
        }.addOnFailureListener {
            view.visibility = View.GONE
            forgot.visibility = View.VISIBLE
            Toast.makeText(this, "LogIn Failed", Toast.LENGTH_SHORT).show()
        }
    }

    @Keep
    private fun forgotPass(email: String, proView: LinearLayout) {
        proView.visibility = View.VISIBLE
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener {
            Toast.makeText(this, "check your Email", Toast.LENGTH_LONG).show()
            proView.visibility = View.GONE
            dialog.dismiss()
        }.addOnFailureListener {
            Toast.makeText(this, "This Email IID isn't Registered.", Toast.LENGTH_LONG).show()
            proView.visibility = View.GONE
            dialog.dismiss()
        }
    }

    fun exit(email: String, proView: LinearLayout) {
        dialog = Dialog(this)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custome_diloag)

        val dialogButton = dialog.findViewById<Button>(R.id.btnOk)
        val title = dialog.findViewById<TextView>(R.id.emailTitle)
        val dismissButton = dialog.findViewById<Button>(R.id.btnDismis)
        title.text = "We'll send a Email to: $email"
        dialogButton.setOnClickListener {
            forgotPass(email, proView)
        }
        dismissButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = firebaseAuth.currentUser

        if (GoogleSignIn.getLastSignedInAccount(this) != null || currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    fun addEmail(Email: String) {
        editor.putString("shEmail", Email)
        editor.commit()
    }

    private fun checkMyDocument() {
        binding.prog.visibility = View.VISIBLE
        FirebaseFirestore.getInstance().collection("users").get().addOnSuccessListener {
            var hai = false
            for (idList in it) {
                if (idList.id == firebaseAuth.currentUser?.uid.toString()) {
                    hai = true
                    break
                }
            }
            if (hai) {
                binding.prog.visibility = View.GONE
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                FirebaseFirestore.getInstance().collection("users")
                    .document(firebaseAuth.currentUser?.uid.toString())
                    .set(
                        UserModel(
                            name,
                            "",
                            "",
                            "",
                            email,
                            firebaseAuth.currentUser?.uid.toString()
                        )
                    ).addOnSuccessListener {
                        binding.prog.visibility = View.GONE
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }.addOnFailureListener {e ->
                        binding.prog.visibility = View.GONE
                        Toast.makeText(this, e.localizedMessage, Toast.LENGTH_LONG).show()
                    }
            }
        }

    }

}