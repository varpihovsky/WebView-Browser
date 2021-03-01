package com.example.webviewbrowser.model

import android.annotation.SuppressLint
import android.content.Context
import android.os.Parcelable
import com.example.webviewbrowser.view.fragment.PageFragment
import kotlinx.android.parcel.Parcelize


@Parcelize
class Page(var path: String) : Parcelable


@SuppressLint("ViewConstructor")
class PageButton(context: Context, val pageFragment: PageFragment) :
        androidx.appcompat.widget.AppCompatButton(context)