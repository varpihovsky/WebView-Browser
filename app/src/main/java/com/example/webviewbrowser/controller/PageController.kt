package com.example.webviewbrowser.controller

import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import com.example.webviewbrowser.view.fragment.PageFragment
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object PageController {
    lateinit var fragmentManager: Pair<FragmentManager, Int>

    private val pagesList = mutableListOf<PageFragment>()
    private var currentPage: PageFragment? = null

    val createNewPageListener: (View?) -> Unit = {
        fragmentManager.first.createNewPage()
    }

    val deleteCurrentPageListener: (View?) -> Unit = {
        fragmentManager.first.removeCurrentPage()
        deleteCurrentPage()
        fragmentManager.first.showCurrentPage()
    }

    private fun createNewPage(): PageFragment =
        PageFragment.newInstance().let {
            pagesList.add(it)
            currentPage = pagesList.last()
            return@let it
        }


    private fun deleteCurrentPage() =
        currentPage?.let {
            pagesList.remove(it)
            ButtonController.removeButton(it)
            currentPage = pagesList.lastOrNull()
        }

    private fun refreshCurrentPage() {
        currentPage?.refresh()
    }

    fun selectPage(pageFragment: PageFragment) {
        if (pageFragment == currentPage) {
            refreshCurrentPage()
        } else {
            fragmentManager.first.hideCurrentPage()
            currentPage = pageFragment
            fragmentManager.first.showCurrentPage()
            refreshCurrentPage()
        }
    }

    private fun FragmentManager.createNewPage() {
        this.commit {
            setReorderingAllowed(true)
            PageController.createNewPage().let {
                add(fragmentManager.second, it)
                show(it)
            }
        }
        MainScope().launch {
            currentPage?.let {
                while (it.lifecycle.currentState == Lifecycle.State.INITIALIZED) {
                    delay(100)
                }
                ButtonController.addButton(it)
            }
        }
    }

    private fun FragmentManager.removeCurrentPage() =
        this.commit {
            setReorderingAllowed(true)
            currentPage?.let { ::remove }
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