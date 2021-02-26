package com.example.webviewbrowser.controller

import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.example.webviewbrowser.view.fragment.PageFragment

object Controller {
    private val pagesList = mutableListOf<PageFragment>()
    private var currentPage: PageFragment? = null

    lateinit var fragmentManager: Pair<FragmentManager, Int>

    val createNewPageListener: (View?) -> Unit = {
        fragmentManager.first.createNewPage()
    }

    val deleteCurrentPageListener: (View?) -> Unit = {
        fragmentManager.first.removeCurrentPage()
        deleteCurrentPage()
        fragmentManager.first.showCurrentPage()
    }

    private fun createNewPage(): PageFragment? {
        pagesList.add(PageFragment.newInstance())
        currentPage = pagesList.last()
        ButtonController.addButton(currentPage!!)
        return currentPage
    }

    private fun deleteCurrentPage() {
        pagesList.remove(currentPage)
        if (currentPage != null) {
            ButtonController.removeButton(currentPage!!)
        }
        currentPage = pagesList.lastOrNull()
    }

    private fun refreshCurrentPage() {
        currentPage?.refresh()
    }

    fun selectPage(pageFragment: PageFragment){
        if (pageFragment == currentPage) {
            refreshCurrentPage()
        } else {
            fragmentManager.first.hideCurrentPage()
            currentPage = pageFragment
            fragmentManager.first.showCurrentPage()
            refreshCurrentPage()
        }
    }

    private fun FragmentManager.createNewPage() =
        this.commit {
            setReorderingAllowed(true)
            add(fragmentManager.second, Controller.createNewPage()!!)
            show(currentPage!!)
        }

    private fun FragmentManager.removeCurrentPage() =
        this.commit {
            setReorderingAllowed(true)
            currentPage?.let { remove(it) }
        }

    private fun FragmentManager.showCurrentPage() = currentPage?.let {
                this.commit {
                    show(it)
                }
            }

    private fun FragmentManager.hideCurrentPage() = currentPage?.let {
        this.commit { hide(it) }
    }
}