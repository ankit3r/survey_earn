package com.gyanhub.surveyearn.fragments.massage

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.gyanhub.surveyearn.databinding.FragmentMassageViewBinding

class MassageViewFragment(private val url: String) : Fragment() {
    private lateinit var binding: FragmentMassageViewBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMassageViewBinding.inflate(layoutInflater, container, false)
        binding.pageHolder.apply {
            settings.javaScriptEnabled = true
            settings.setSupportZoom(true)
            settings.builtInZoomControls = true
            settings.displayZoomControls = false
        }
        loadMyInfo(url)


        return binding.root
    }

    private fun loadMyInfo(url: String) {
        binding.pageHolder.loadUrl(url)
        binding.pageHolder.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                binding.progressBar.visibility= View.VISIBLE
                binding.pageHolder.visibility = View.GONE
                super.onPageStarted(view, url, favicon)
            }
            override fun onPageFinished(view: WebView?, url: String?) {
                binding.progressBar.visibility= View.GONE
                binding.pageHolder.visibility = View.VISIBLE
                super.onPageFinished(view, url)
            }
        }
    }
}