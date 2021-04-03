package com.example.webviewbrowser.activity

import android.os.Parcelable
import com.example.webviewbrowser.model.Page
import com.example.webviewbrowser.model.PageRepository
import com.example.webviewbrowser.page.PageFragment
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
class MainActivityPresenter
private constructor(private var mainActivityInterface: @RawValue MainActivityInterface?) :
    Parcelable, PageFragment.MainActivityPresenterInterface {
    private val pageRepository = PageRepository()

    private var currentPage: Page = Page(id = 0)

    fun bind(mainActivityInterface: MainActivityInterface) {
        this.mainActivityInterface = mainActivityInterface
    }

    fun unBind() {
        this.mainActivityInterface = null
    }

    fun init() {
        pageRepository.addPageToList(currentPage)

        mainActivityInterface?.createPage(currentPage)
        mainActivityInterface?.loadPage(currentPage)
    }

    fun onCreateNew(text: String = Page.DEFAULT_URL) {
        Page(text, pageRepository.size() + 1).let {
            mainActivityInterface?.hidePage(currentPage)

            pageRepository.addPageToList(it)

            mainActivityInterface?.createPage(it)

            currentPage = it

            mainActivityInterface?.showPage(it)
            mainActivityInterface?.loadPage(it)
        }
    }

    fun onRemoveCurrent() {
        if (pageRepository.size() == MIN_REPOSITORY_PAGES_COUNT) {
            removeCurrentWhenRepositorySizeIsMin()
        } else {
            removeCurrentWhenRepositorySizeIsNotMin()
        }
    }

    private fun removeCurrentWhenRepositorySizeIsMin() {
        Page(id = 0).let {
            pageRepository.replacePage(currentPage, it)

            mainActivityInterface?.changePage(currentPage, it)
            mainActivityInterface?.showPage(it)
            mainActivityInterface?.loadPage(it)

            currentPage = it
        }
    }

    private fun removeCurrentWhenRepositorySizeIsNotMin() {
        pageRepository.removePageFromList(currentPage)

        mainActivityInterface?.removePage(currentPage)

        pageRepository.getLast().let {
            mainActivityInterface?.showPage(currentPage)
            currentPage = it
        }
    }

    override fun onPageChanged(from: Page, to: Page) {
        pageRepository.replacePage(from, to)

        mainActivityInterface?.setTextOfButton(from, to)

        if (from == currentPage) {
            currentPage = to
        }
    }

    fun selectPage(page: Page) {
        if (page == currentPage) {
            mainActivityInterface?.refreshPage(page)
        } else {
            mainActivityInterface?.hidePage(currentPage)
            mainActivityInterface?.showPage(page)

            currentPage = page
        }
    }

    fun onRestore() {
        mainActivityInterface?.showPage(currentPage)
        pageRepository.list().forEach {
            if (it != currentPage)
                mainActivityInterface?.hidePage(it)
        }
        mainActivityInterface?.loadPage(currentPage)
    }

    companion object {
        private const val MIN_REPOSITORY_PAGES_COUNT = 1

        fun newInstance(mainActivityInterface: MainActivityInterface) =
            MainActivityPresenter(mainActivityInterface)
    }
}