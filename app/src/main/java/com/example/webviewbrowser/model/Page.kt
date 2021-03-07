package com.example.webviewbrowser.model

import android.annotation.SuppressLint
import android.content.Context
import android.os.Parcelable
import android.view.View
import android.widget.Button
import com.example.webviewbrowser.activity.MainActivityPresenter
import kotlinx.android.parcel.Parcelize
import java.util.regex.Pattern


@Parcelize
data class Page(var path: String = DEFAULT_URL, val id: Int) : Parcelable {
    override fun toString(): String {
        return Pattern.compile("/[^/].+/").matcher(path).run {
            if (find()) {
                group().let {
                    it.substring(1, it.length - 1)
                }
            } else path
        }
    }

    companion object {
        const val DEFAULT_URL = "https://www.google.com/"
    }
}

class PageRepository {
    private val pageList = mutableListOf<Page>()

    fun addPageToList(page: Page) {
        pageList.add(page)
    }

    fun removePageFromList(page: Page) {
        pageList.remove(page)
    }

    fun replacePage(from: Page, to: Page) {
        if (from == to) {
            return
        }
        val index = pageList.indexOf(from)
        pageList[index] = to
    }

    fun size() = pageList.size

    fun getLast() = pageList.last()

    fun list() = ArrayList(pageList)
}

@SuppressLint("ViewConstructor", "AppCompatCustomView")
class PageButton(
    c: Context,
    var page: Page,
    var mainActivityPresenter: MainActivityPresenter
) : Button(c) {
    val listener: (View) -> Unit = {
        mainActivityPresenter.selectPage(page)
    }
}