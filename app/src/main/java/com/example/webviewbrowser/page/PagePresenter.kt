package com.example.webviewbrowser.page

import com.example.webviewbrowser.model.Page

class PagePresenter(private val pageFragmentInterface: PageFragmentInterface) {
    fun onUrlEntered(path: String, page: Page) {
        pageFragmentInterface.assignPage(Page(path, page.id))
        pageFragmentInterface.load()
    }
}