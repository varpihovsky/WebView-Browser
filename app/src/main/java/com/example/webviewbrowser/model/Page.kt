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
data class Page(var path: String = "https://www.google.com/", ) : Parcelable {
    override fun toString(): String {
        return Pattern.compile("/[^/].+/").matcher(path).run {
            if (find()) {
                group().let {
                    it.substring(1, it.length - 1)
                }
            } else path
        }
    }
}

class PageRepository {
    private val pageList = mutableListOf<Page>()

    fun addPage(page: Page) {
        pageList.add(page)
    }

    fun removePage(page: Page) {
        pageList.remove(page)
    }

    fun replacePage(from: Page, to: Page) {
        val index = pageList.indexOf(from)
        pageList[index] = to
    }

    fun size() = pageList.size

    fun getLast() = pageList.last()
}

@SuppressLint("ViewConstructor", "AppCompatCustomView")
class PageButton(context: Context, var page: Page, mainActivityPresenter: MainActivityPresenter) :
    Button(context) {
    val listener: (View) -> Unit = {
        mainActivityPresenter.selectPage(page)
    }
}