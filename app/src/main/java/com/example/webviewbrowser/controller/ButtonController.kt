package com.example.webviewbrowser.controller

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import com.example.webviewbrowser.model.PageButton
import com.example.webviewbrowser.view.fragment.PageFragment

object ButtonController {
    private val buttons = mutableListOf<PageButton>()
    lateinit var linearLayout: LinearLayout
    lateinit var context: Context

    private val listener: (View?) -> Unit = {
        Controller.selectPage((it as PageButton).pageFragment)
    }

    fun addButton(pageFragment: PageFragment) {
        val button = PageButton(context, pageFragment)
        button.text = pageFragment.page?.path
        button.setOnClickListener(listener)
        buttons.add(button)
        linearLayout.addView(button)
    }

    fun removeButton(pageFragment: PageFragment) {
        val button = buttons.find { it.pageFragment == pageFragment }
        buttons.remove(button)
        linearLayout.removeView(button)
    }
}

