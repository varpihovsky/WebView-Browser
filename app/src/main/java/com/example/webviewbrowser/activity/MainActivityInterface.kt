package com.example.webviewbrowser.activity

import com.example.webviewbrowser.model.Page

interface MainActivityInterface {
    fun createPage(page: Page)

    fun removePage(page: Page)

    fun showPage(page: Page)

    fun hidePage(page: Page)

    fun loadPage(page: Page)

    fun setTextOfButton(from: Page, to: Page)

    fun changePage(from: Page, to: Page)

    fun refreshPage(page: Page)
}