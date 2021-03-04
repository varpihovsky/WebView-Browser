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
    fun onUpdateButton(from: Page, to: Page) {
        mainActivityPresenter.onUpdateButton(from, to)
    }
}