package com.example.webviewbrowser.model

import android.annotation.SuppressLint
import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
import com.example.webviewbrowser.view.fragment.PageFragment

class Page(var path: String?) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(path)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Page> {
        override fun createFromParcel(parcel: Parcel): Page {
            return Page(parcel)
        }

        override fun newArray(size: Int): Array<Page?> {
            return arrayOfNulls(size)
        }
    }
}

@SuppressLint("AppCompatCustomView")
class PageButton(context: Context, val pageFragment: PageFragment) :
        Button(context)