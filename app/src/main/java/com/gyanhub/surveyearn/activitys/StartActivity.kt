@file:Suppress("DEPRECATION")

package com.gyanhub.surveyearn.activitys

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager.getDefaultSharedPreferences
import android.view.View



import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

import com.gyanhub.surveyearn.R
import com.gyanhub.surveyearn.adapter.PageViewAdapter

import com.gyanhub.surveyearn.databinding.ActivityStartBinding
import com.gyanhub.surveyearn.model.StartModel

class StartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartBinding
    private val data = ArrayList<StartModel>()
    private lateinit var sharedPreferences: SharedPreferences
   private lateinit var editor: SharedPreferences.Editor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.prog.visibility = View.VISIBLE
        sharedPreferences = getDefaultSharedPreferences(this)
        editor = sharedPreferences.edit()
        addData()
        val adapter = PageViewAdapter(data,this,binding.prog)
        binding.viewPage2.adapter = adapter
        binding.viewPage2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                selectDot()
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                selectDot()
                super.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                selectDot()
                super.onPageScrollStateChanged(state)
            }
        })

        binding.btnNext.setOnClickListener {
            if (binding.viewPage2.currentItem == 2) {
                editor.putBoolean("done",true)
                editor.commit()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                binding.viewPage2.currentItem++
                binding.btnNext.text = getString(R.string.next)
            }

        }

        if(sharedPreferences.getBoolean("done",false)){
            binding.prog.visibility = View.GONE
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }else{
            binding.prog.visibility = View.GONE
        }
    }

    private fun allReset() {
        binding.img1.setImageResource(R.drawable.ic_outline_24)
        binding.img2.setImageResource(R.drawable.ic_outline_24)
        binding.img3.setImageResource(R.drawable.ic_outline_24)
    }

    private fun selectDot() {
        allReset()
        when (binding.viewPage2.currentItem) {
            0 -> binding.img1.setImageResource(R.drawable.ic_fill_24)
            1 -> {
                binding.img2.setImageResource(R.drawable.ic_fill_24)
                binding.btnNext.text = getString(R.string.next)
            }
            2 -> binding.img3.setImageResource(R.drawable.ic_fill_24)
            else -> allReset()
        }
    }

    private fun addData() {
        data.add(
            StartModel(
              "joinUs.json",
                getString(R.string.head01),
                getString(R.string.para01),
                getString(R.string.term)
            )
        )
        data.add(
            StartModel(
                "earns.json",
                getString(R.string.head02),
                getString(R.string.para02),
            )
        )
        data.add(
            StartModel(
                "control.json",
                getString(R.string.head03),
                getString(R.string.para03),
            )
        )
    }
}