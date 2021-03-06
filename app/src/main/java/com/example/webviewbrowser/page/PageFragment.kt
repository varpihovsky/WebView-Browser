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
import com.example.webviewbrowser.R
import com.example.webviewbrowser.databinding.FragmentBrowserBinding
import com.example.webviewbrowser.model.Page

class PageFragment : Fragment(), PageFragmentInterface {
    lateinit var page: Page

    private lateinit var webView: WebView
    private lateinit var binding: FragmentBrowserBinding
    private lateinit var mainActivityPresenterInterface: MainActivityPresenterInterface
    private val pagePresenter = PagePresenter(this)

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
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_browser, container, false)

        binding.addressBar.setOnEditorActionListener(this::onAddressBarTextEntered)

        binding.url = page.path


        webView = binding.page


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

    fun attachMainActivityPresenterInterface(mainActivityPresenterInterface: MainActivityPresenterInterface) {
        this.mainActivityPresenterInterface = mainActivityPresenterInterface
    }

    override fun refresh() {
        webView.reload()
    }

    override fun load() {
        webView.loadUrl(page.path)
    }

    override fun assignPage(page: Page) {
        mainActivityPresenterInterface.onPageChanged(this.page, page)
        this.page = page
    }

    private fun onAddressBarTextEntered(
        textView: TextView?,
        actionId: Int?,
        event: KeyEvent?
    ): Boolean {
        if ((event?.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER) || actionId == ACTION_SEARCH) {
            pagePresenter.onUrlEntered(textView?.text.toString())
            return true
        }
        return false
    }

    private inner class WebViewWebClient : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            url?.let {
                binding.url = page.path
                mainActivityPresenterInterface.onPageChanged(page, Page(it))
                page = Page(it)
            }
        }
    }

    interface MainActivityPresenterInterface {
        fun onPageChanged(from: Page, to: Page)
    }

    companion object {
        const val PATH_PARAM = "page"
        private const val ACTION_SEARCH = 5

        fun newInstance(page: Page) =
            PageFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(PATH_PARAM, page)
                }
            }
    }
}