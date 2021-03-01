package com.example.webviewbrowser.controller

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import com.example.webviewbrowser.model.PageButton
import com.example.webviewbrowser.view.fragment.PageFragment

object ButtonController {
    lateinit var linearLayout: LinearLayout
    lateinit var context: Context

    private val buttons = mutableListOf<PageButton>()
    private val listener: (View?) -> Unit = {
        PageController.selectPage((it as PageButton).pageFragment)
    }

    fun addButton(pageFragment: PageFragment) {
        PageButton(context, pageFragment).apply {
            text = pageFragment.page.path
            setOnClickListener(listener)
            buttons.add(this)
            linearLayout.addView(this)
        }
    }

    fun removeButton(pageFragment: PageFragment) {
        findButton(pageFragment).let {
            buttons.remove(it)
            linearLayout.removeView(it)
        }
    }

    fun changeTextOfButton(pageFragment: PageFragment) {
        findButton(pageFragment)?.apply {
            text = pageFragment.page.path
        }
    }

    private fun findButton(pageFragment: PageFragment) =
        buttons.find { it.pageFragment == pageFragment }
}

