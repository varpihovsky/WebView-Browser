package com.example.webviewbrowser.view.activity

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.webviewbrowser.R
import com.example.webviewbrowser.controller.ButtonController
import com.example.webviewbrowser.controller.PageController

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.newPageButton).setOnClickListener(PageController.createNewPageListener)
        findViewById<Button>(R.id.removeCurrentPageButton).setOnClickListener(PageController.deleteCurrentPageListener)

        PageController.fragmentManager = supportFragmentManager to R.id.browserFragment

        ButtonController.linearLayout = findViewById(R.id.buttonsLayout)
        ButtonController.context = this
    }

}
