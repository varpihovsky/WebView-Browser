package com.example.webviewbrowser

import android.view.View
import android.widget.LinearLayout
import androidx.core.view.children
import com.example.webviewbrowser.model.Page
import com.example.webviewbrowser.model.PageButton

fun <T> LinearLayout.letFoundPageButtonByPage(page: Page, block: (View?) -> T): T? =
    children.find {
        if (it is PageButton) {
            it.page == page
        } else {
            false
        }
    }.let(block)