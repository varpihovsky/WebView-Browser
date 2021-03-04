package com.example.webviewbrowser.page

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.webviewbrowser.MainActivityPresenterAdapter
import com.example.webviewbrowser.R
import com.example.webviewbrowser.databinding.FragmentBrowserBinding
import com.example.webviewbrowser.model.Page

class PageFragment : Fragment(), PageFragmentInterface {
    lateinit var page: Page

    private lateinit var webView: WebView
    private lateinit var mainActivityPresenterAdapter: MainActivityPresenterAdapter
    private lateinit var binding: FragmentBrowserBinding
    private val pagePresenter = PagePresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            page = it.get(PATH_PARAM) as Page
            mainActivityPresenterAdapter = it.get(ADAPTER_PARAM) as MainActivityPresenterAdapter
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_browser, container, false)

        binding.addressBar.setOnEditorActionListener(this::onAddressBarTextEntered)

        binding.url = page.path

        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        webView = binding.page

        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewWebClient()
        load()
    }

    override fun refresh() {
        webView.reload()
    }

    override fun load() {
        webView.loadUrl(page.path)
    }

    override fun assignPage(page: Page) {
        mainActivityPresenterAdapter.onUpdateButton(this.page, page)
        this.page = page
    }

    private fun onAddressBarTextEntered(
        textView: TextView?,
        actionId: Int?,
        event: KeyEvent?
    ): Boolean {
        if (event?.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER) {
            pagePresenter.onUrlEntered(textView?.text.toString())
            return true
        }
        return false
    }

    private inner class WebViewWebClient : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            url?.let {
                binding.url = page.path
                mainActivityPresenterAdapter.onUpdateButton(page, Page(it))
                page = Page(it)
            }
        }
    }

    companion object {
        const val PATH_PARAM = "page"
        const val ADAPTER_PARAM = "adapter"

        fun newInstance(page: Page, mainActivityPresenterAdapter: MainActivityPresenterAdapter) =
            PageFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(PATH_PARAM, page)
                    putParcelable(ADAPTER_PARAM, mainActivityPresenterAdapter)
                }
            }
    }
}