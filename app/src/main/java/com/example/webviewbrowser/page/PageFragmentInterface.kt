package com.example.webviewbrowser.page

import com.example.webviewbrowser.model.Page

interface PageFragmentInterface {
    fun refresh()

    fun load()

    fun assignPage(page: Page)
}