package com.example.webviewbrowser.activity

import com.example.webviewbrowser.model.Page
import com.example.webviewbrowser.model.PageRepository

class MainActivityPresenter private constructor(private var mainActivityInterface: MainActivityInterface) {
    private val pageRepository = PageRepository()
    private var currentPage: Page = Page()

    fun init() {
        pageRepository.addPage(currentPage)

        mainActivityInterface.createPage(currentPage)
        mainActivityInterface.loadPage(currentPage)
    }

    fun onCreateNew(text: String = "https://www.google.com/") {
        Page(text).let {
            mainActivityInterface.hidePage(currentPage)

            pageRepository.addPage(it)

            mainActivityInterface.createPage(it)

            currentPage = it

            mainActivityInterface.showPage(it)
            mainActivityInterface.loadPage(it)
        }
    }

    fun onRemoveCurrent() {
        if (pageRepository.size() != 1) {
            pageRepository.removePage(currentPage)

            mainActivityInterface.removePage(currentPage)

            currentPage = pageRepository.getLast()

            mainActivityInterface.showPage(currentPage)
        } else {
            Page().let {
                pageRepository.replacePage(currentPage, it)

                mainActivityInterface.changePage(currentPage, it)
                mainActivityInterface.loadPage(it)

                currentPage = it
            }
        }
    }

    fun onUpdateButton(from: Page, to: Page) {
        pageRepository.replacePage(from, to)

        mainActivityInterface.setTextOfButton(from, to)
    }

    fun selectPage(page: Page) {
        if (page == currentPage) {
            mainActivityInterface.refreshPage(page)
        } else {
            mainActivityInterface.hidePage(currentPage)
            mainActivityInterface.showPage(page)

            currentPage = page
        }
    }

    companion object {
        fun newInstance(mainActivityInterface: MainActivityInterface) =
            MainActivityPresenter(mainActivityInterface)
    }
}