package com.example.webviewbrowser.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.webviewbrowser.R
import com.example.webviewbrowser.controller.ButtonController
import com.example.webviewbrowser.model.Page

private const val PATH_PARAM = "page"

const val DEFAULT_PATH = "https://www.google.com/"

class PageFragment(path: String? = null) : Fragment() {
    var page: Page = Page(path ?: DEFAULT_PATH)
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            page = it.get(PATH_PARAM) as Page
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_browser, container, false)
    }

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
        webView.loadUrl(page.path!!)
        webView.reload()
    }

    private inner class WebViewWebClient : WebViewClient(){
        override fun onPageFinished(view: WebView?, url: String?) {
            page.path = url
            ButtonController.changeTextOfButton(this@PageFragment)
        }
    }
}