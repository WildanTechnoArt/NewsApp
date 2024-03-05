package com.wildantechnoart.newsapp.view

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.wildantechnoart.newsapp.databinding.ActivityArticleViewBinding
import com.wildantechnoart.newsapp.utils.Constant
import com.wildantechnoart.newsapp.utils.ViewBindingExt.viewBinding

class ArticleViewActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityArticleViewBinding::inflate)
    private var getUrlArticle: String? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        getUrlArticle = intent.getStringExtra(Constant.ARTICLE_URL).toString()

        binding.webView.settings.javaScriptEnabled = true
        binding.swipeRefresh.setOnRefreshListener {
            loadWebView()
        }

        loadWebView()
    }

    private fun loadWebView() = with(binding) {
        swipeRefresh.isRefreshing = true
        webView.loadUrl(getUrlArticle.toString())
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                webView.loadUrl(request?.url.toString())
                swipeRefresh.isRefreshing = true
                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                swipeRefresh.isRefreshing = true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                swipeRefresh.isRefreshing = false
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && binding.webView.canGoBack()) {
            binding.webView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}