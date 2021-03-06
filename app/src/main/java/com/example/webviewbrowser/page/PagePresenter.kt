package com.example.webviewbrowser.page

import com.example.webviewbrowser.model.Page

class PagePresenter(private val pageFragmentInterface: PageFragmentInterface) {
    fun onUrlEntered(path: String) {
        pageFragmentInterface.assignPage(Page(path))
        pageFragmentInterface.load()
    }
}