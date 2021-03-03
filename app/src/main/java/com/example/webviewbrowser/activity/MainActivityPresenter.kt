package com.example.webviewbrowser.activity

import android.text.Editable
import com.example.webviewbrowser.model.Page
import com.example.webviewbrowser.model.PageRepository

class MainActivityPresenter(private var mainActivityInterface: MainActivityInterface) {
    private val pageRepository = PageRepository()
    private var currentPage: Page? = null

    fun onCreateNew(text: String = "https://google.com/") {
        Page(text).let {
            pageRepository.addPage(it)
            mainActivityInterface.createPage(it)
            currentPage?.let { current -> mainActivityInterface.hidePage(current) }
            currentPage = it
            mainActivityInterface.showPage(it)
            mainActivityInterface.loadPage(it)
            onUpdateAddressBar(it)
        }
    }

    fun onRemoveCurrent() {
        currentPage?.let {
            pageRepository.removePage(it)
            mainActivityInterface.removePage(it)
            currentPage = pageRepository.getLast()
        }
        onUpdateAddressBar(currentPage ?: Page(""))
    }

    fun onUpdateButton(page: Page) {
        mainActivityInterface.setTextOfButton(page)
    }

    fun onUpdateAddressBar(page: Page) {
        mainActivityInterface.updateAddressBar(
            Editable.Factory.getInstance().newEditable(page.path)
        )
    }

    fun onUserAddressEntered(text: String) {
        val page = Page(text)
        if (currentPage != null) {
            mainActivityInterface.changePage(currentPage!!, page)
            mainActivityInterface.loadPage(page)
        } else {
            onCreateNew(text)
        }
        currentPage = page
    }

    fun selectPage(page: Page) {
        if (page == currentPage) {
            mainActivityInterface.refreshPage(page)
        } else {
            currentPage?.let { mainActivityInterface.hidePage(it) }
            mainActivityInterface.showPage(page)
            onUpdateAddressBar(page)
            currentPage = page
        }
    }
}