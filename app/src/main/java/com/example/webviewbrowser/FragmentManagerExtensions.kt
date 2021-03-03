package com.example.webviewbrowser

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.webviewbrowser.model.Page
import com.example.webviewbrowser.page.PageFragment

fun <T> FragmentManager.letLastFragmentOrDoNothing(block: (Fragment) -> T): T? =
    fragments.lastOrNull()?.let(block)

fun <T> FragmentManager.letFoundFragmentByPageOrDoNothing(page: Page, block: (Fragment) -> T): T? =
    fragments.find {
        println(it is PageFragment)
        if (it is PageFragment) {
            it.page == page
        } else {
            false
        }
    }?.let(block)

