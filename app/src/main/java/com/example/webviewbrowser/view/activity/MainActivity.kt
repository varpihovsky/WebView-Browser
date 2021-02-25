package com.example.webviewbrowser.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.webviewbrowser.R
import com.example.webviewbrowser.controller.ButtonController
import com.example.webviewbrowser.controller.Controller

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.popBackStackImmediate()

        findViewById<Button>(R.id.newPageButton).setOnClickListener(Controller.createNewPageListener)
        findViewById<Button>(R.id.removeCurrentPageButton).setOnClickListener(Controller.deleteCurrentPageListener)

        Controller.fragmentManager = supportFragmentManager to R.id.browserFragment

        ButtonController.linearLayout = findViewById(R.id.buttonsLayout)
        ButtonController.context = applicationContext
    }

}
