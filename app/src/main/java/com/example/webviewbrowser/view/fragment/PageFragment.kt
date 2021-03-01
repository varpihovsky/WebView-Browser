package com.example.webviewbrowser.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.example.webviewbrowser.R
import com.example.webviewbrowser.controller.ButtonController
import com.example.webviewbrowser.model.Page

class PageFragment : Fragment() {
    lateinit var page: Page
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            page = it.get(PATH_PARAM) as Page
            it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_browser, container, false)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        webView = view.findViewById(R.id.page)

        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewWebClient()

        load()
    }

    fun refresh(){
        webView.reload()
    }

    private fun load(){
        webView.loadUrl(page.path)
    }

    private inner class WebViewWebClient : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            page.path = url ?: ""
            ButtonController.changeTextOfButton(this@PageFragment)
        }
    }

    companion object {
        private const val DEFAULT_PATH = "https://www.google.com/"

        private const val PATH_PARAM = "page"

        fun newInstance(path: String? = null) = PageFragment().apply {
            arguments = Bundle().apply {
                putParcelable(PATH_PARAM, Page(path ?: DEFAULT_PATH))
            }
        }
    }
}