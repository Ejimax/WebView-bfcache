package com.example.webview_bfcache

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewCompat
import androidx.webkit.WebViewFeature
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var view: WebView

    @OptIn(WebSettingsCompat.ExperimentalBackForwardCache::class)
    @SuppressLint("SetJavaScriptEnabled", "WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        view = WebView(this)
        view.webViewClient = WebViewClient()
        view.settings.javaScriptEnabled = true
        if (ENABLE_BFCACHE) {
            if (WebViewFeature.isFeatureSupported(WebViewFeature.BACK_FORWARD_CACHE)) {
                WebSettingsCompat.setBackForwardCacheEnabled(view.settings, true)
            } else {
                Toast.makeText(
                    this,
                    "Back-forward cache is not supported in this WebView version",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
                return
            }
        }
        view.loadUrl("https://developer.mozilla.org/en-US/docs/Glossary/bfcache")
        findViewById<FrameLayout>(R.id.container).addView(view)
        findViewById<ExtendedFloatingActionButton>(R.id.fab_back).setOnClickListener {
            if (view.canGoBack()) {
                view.goBack()
            }
        }
        findViewById<ExtendedFloatingActionButton>(R.id.fab_version).setOnClickListener {
            val packageInfo = WebViewCompat.getCurrentWebViewPackage(this)
            val text = buildString {
                if (packageInfo != null) {
                    append("WebView ${packageInfo.versionName}")
                } else {
                    append("WebView version not detected")
                }
                append(" (bfcache ${if (ENABLE_BFCACHE) "enabled" else "disabled"})")
            }
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {

        private const val ENABLE_BFCACHE = false
    }
}
