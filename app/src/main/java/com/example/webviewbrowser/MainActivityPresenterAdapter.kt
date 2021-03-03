package com.example.webviewbrowser

import android.os.Parcelable
import com.example.webviewbrowser.activity.MainActivityPresenter
import com.example.webviewbrowser.model.Page
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue


//Is it right to do so?
@Parcelize
class MainActivityPresenterAdapter(private val mainActivityPresenter: @RawValue MainActivityPresenter) :
    Parcelable {
    fun onUpdateButton(page: Page) {
        mainActivityPresenter.onUpdateButton(page)
    }

    fun onUpdateAddressBar(page: Page) {
        mainActivityPresenter.onUpdateAddressBar(page)
    }
}