package com.example.webviewbrowser

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.example.webviewbrowser.model.Page
import com.example.webviewbrowser.page.PageFragment

fun <T> FragmentManager.letLastFragmentOrDoNothing(block: (Fragment) -> T): T? =
    fragments.lastOrNull()?.let(block)

fun <T> FragmentManager.letFoundFragmentByPage(page: Page, block: (Fragment) -> T): T? =
    getFragmentByPage(page)?.let(block)

fun FragmentManager.removeAllFragments() {
    commit {
        this@removeAllFragments.fragments.forEach {
            remove(it)
        }
    }
}

fun FragmentManager.getFragmentByPage(page: Page) =
    fragments.find {
        if (it is PageFragment) {
            Log.d("Fragments:", it.page.path)
            it.page == page
        } else {
            false
        }
    }