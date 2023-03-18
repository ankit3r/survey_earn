@file:Suppress("DEPRECATION")
@file:RequiresApi(Build.VERSION_CODES.O)

package com.gyanhub.surveyearn.activitys

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.gyanhub.surveyearn.R
import com.gyanhub.surveyearn.Viewmodel.BalanceViewModel
import com.gyanhub.surveyearn.databinding.ActivityMassageHolder2Binding
import com.gyanhub.surveyearn.fragments.massage.MassageViewFragment
import com.gyanhub.surveyearn.model.BalanceModel
import com.gyanhub.surveyearn.model.HistoryModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MassageHolderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMassageHolder2Binding
    private lateinit var url: String
    private var mRewardedAd: RewardedAd? = null
    private var earn: Int = 0
    private var id: String = ""
    private var cyName: String = ""
    private var docID: String = ""
    private var oldPoint: Int = 0
    private var oldBalance: Int = 0

    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMassageHolder2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        url = intent.getStringExtra("url").toString()
        docID = intent.getStringExtra("docID").toString()
        cyName = intent.getStringExtra("Title").toString()
        binding.title.text = cyName
        earn = intent.getIntExtra("earn", 0)
        oldPoint = intent.getIntExtra("oldPoints", 0)
        oldBalance = intent.getIntExtra("oldBalance", 0)
        id = intent.getStringExtra("programmesId").toString()
        load()


        if (url.isNotEmpty()) {
            loadFragment(MassageViewFragment(url))
        } else {
            Toast.makeText(this, "No Survey available.", Toast.LENGTH_SHORT).show()
            onBackPressed()
        }

        binding.back.setOnClickListener {
            onBackPressed()
        }
        deleteSurvey()
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

    private fun load() {
        val adRequest = AdRequest.Builder().build()
        FirebaseFirestore.getInstance().collection("AdsIDs")
            .document("reward").get().addOnSuccessListener {
                if (it != null) {
                    RewardedAd.load(
                        this,
                        it.data?.get("id").toString(),
                        adRequest,
                        object : RewardedAdLoadCallback() {

                            override fun onAdFailedToLoad(adError: LoadAdError) {
                                mRewardedAd = null
                            }

                            override fun onAdLoaded(rewardedAd: RewardedAd) {
                                mRewardedAd = rewardedAd
                            }
                        })

                } else {
                    RewardedAd.load(
                        this,
                        "ca-app-pub-3585523772729447/9241701944",
                        adRequest,
                        object : RewardedAdLoadCallback() {

                            override fun onAdFailedToLoad(adError: LoadAdError) {
                                mRewardedAd = null
                            }


                            override fun onAdLoaded(rewardedAd: RewardedAd) {
                                mRewardedAd = rewardedAd
                            }
                        })

                }
            }
        mRewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                addPoints(oldPoint, earn)
                mRewardedAd = null
            }
        }


    }

    override fun onBackPressed() {
        if (mRewardedAd != null) {
            binding.prog.visibility = View.VISIBLE
            mRewardedAd?.show(this) {
                CoroutineScope(Dispatchers.IO).launch {
                    addPoints(oldPoint, earn)
                }
            }
            addPoints(oldPoint, earn)
            super.onBackPressed()
        } else {
            super.onBackPressed()
        }
    }

    private fun backGroundTask(earn: String) {
        val time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM"))
        FirebaseFirestore.getInstance().collection("users")
            .document(firebaseAuth.currentUser?.uid.toString())
            .collection("history")
            .add(HistoryModel(id, "Programme completed", earn, "$time  $cyName"))
            .addOnSuccessListener {
                binding.prog.visibility = View.GONE
            }.addOnFailureListener {
                binding.prog.visibility = View.GONE
            }

    }

    private fun addPoints(oldPoints: Int, newPoints: Int) {
        val totalPoint = oldPoints + newPoints
        if (totalPoint >= 2000) {
            FirebaseFirestore.getInstance().collection("users")
                .document(firebaseAuth.currentUser?.uid.toString()).collection("history")
                .whereEqualTo("programmesId", id).get().addOnSuccessListener {
                    if (it.isEmpty) {
                        BalanceViewModel().addBalance(
                            BalanceModel(
                                totalPoint - 2000,
                                oldBalance + 3
                            )
                        )
                        backGroundTask("$newPoints")
                        backGroundTask("$3")
                    }
                }.addOnFailureListener {
                    BalanceViewModel().addBalance(BalanceModel(totalPoint - 2000, oldBalance + 3))
                    backGroundTask("$newPoints")
                    backGroundTask("$3")
                }
        } else {
            FirebaseFirestore.getInstance().collection("users")
                .document(firebaseAuth.currentUser?.uid.toString()).collection("history")
                .whereEqualTo("programmesId", id).get().addOnSuccessListener {
                    if (it.isEmpty) {
                        BalanceViewModel().addBalance(BalanceModel(totalPoint, oldBalance))
                        backGroundTask("$newPoints")
                    }
                }.addOnFailureListener {

                    BalanceViewModel().addBalance(BalanceModel(totalPoint, oldBalance))
                    backGroundTask("$newPoints")
                }

        }

    }

    private fun deleteSurvey() {
        FirebaseFirestore.getInstance().collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .collection("userProgram").document(docID).delete()
           .addOnFailureListener {
                Toast.makeText(this, "Some thing wrong Please Try Again", Toast.LENGTH_SHORT).show()
            }

    }


}