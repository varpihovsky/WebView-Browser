package com.example.webviewbrowser.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.webviewbrowser.R
import com.example.webviewbrowser.model.Page

private const val PATH_PARAM = "page"

class PageFragment : Fragment() {
    var page: Page? = null
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            page = it.get(PATH_PARAM) as Page?
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
        webView.webViewClient = WebViewClient()

        if(page != null) {
            load()
        }
    }

    fun refresh(){
        webView.reload()
    }

    fun load(){
        webView.loadUrl(page!!.path!!)
        webView.reload()
    }

    companion object {

        private const val DEFAULT_PATH = "https://www.google.com/"

        @JvmStatic
        fun newInstance(page: Page? = null) =
            PageFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(PATH_PARAM, page ?: Page(DEFAULT_PATH))
                }
            }
    }
}