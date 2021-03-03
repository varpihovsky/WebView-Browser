package com.example.webviewbrowser.activity

import android.os.Bundle
import android.text.Editable
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.example.webviewbrowser.*
import com.example.webviewbrowser.databinding.ActivityMainBinding
import com.example.webviewbrowser.model.Page
import com.example.webviewbrowser.model.PageButton
import com.example.webviewbrowser.page.PageFragment

class MainActivity : AppCompatActivity(), MainActivityInterface {
    private val mainActivityPresenter = MainActivityPresenter(this)
    private lateinit var binding: ActivityMainBinding
    private lateinit var fragmentManager: FragmentManager
    private val mainActivityPresenterAdapter = MainActivityPresenterAdapter(mainActivityPresenter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.addressBar.setOnEditorActionListener(this::onAddressBarTextEntered)

        fragmentManager = supportFragmentManager
    }

    override fun createPage(page: Page) {
        val button = PageButton(
            applicationContext,
            page,
            mainActivityPresenter
        ).apply {
            text = page.toString()
            setOnClickListener(listener)
        }

        binding.buttonsLayout.addView(button)

        fragmentManager.commit {
            setReorderingAllowed(true)
            add(
                binding.browserFragment.id,
                PageFragment.newInstance(page, mainActivityPresenterAdapter)
            )
        }
    }

    override fun removePage(page: Page) {
        fragmentManager.letFoundFragmentByPageOrDoNothing(page) {
            fragmentManager.commit {
                remove(it)
            }
        }

        fragmentManager.letLastFragmentOrDoNothing {
            fragmentManager.commit {
                show(it)
            }
        }

        binding.buttonsLayout.letFoundPageButtonByPage(page) {
            binding.buttonsLayout.removeView(it)
        }
    }

    override fun showPage(page: Page) {
        fragmentManager.letFoundFragmentByPageOrDoNothing(page) {
            fragmentManager.commit {
                show(it)
            }
        }
    }

    override fun hidePage(page: Page) {
        fragmentManager.letFoundFragmentByPageOrDoNothing(page) {
            fragmentManager.commit {
                hide(it)
            }
        }
    }

    override fun loadPage(page: Page) {
        fragmentManager.letFoundFragmentByPageOrDoNothing(page) {
            if (it is PageFragment) it.load()
        }
    }

    override fun setTextOfButton(page: Page) {
        binding.buttonsLayout.letFoundPageButtonByPage(page) {
            if (it is PageButton) {
                it.text = page.toString()
                it.page = page
            }
        }
    }

    override fun updateAddressBar(text: Editable) {
        binding.addressBar.text = text
    }

    override fun changePage(from: Page, to: Page) {
        fragmentManager.letFoundFragmentByPageOrDoNothing(from) {
            if (it is PageFragment) it.page = to
        }
        binding.buttonsLayout.letFoundPageButtonByPage(from) {
            if (it is PageButton) it.page = to
        }
    }

    override fun refreshPage(page: Page) {
        fragmentManager.letFoundFragmentByPageOrDoNothing(page) {
            if (it is PageFragment) {
                it.refresh()
            }
        }
    }

    fun onCreateNewButtonClicked(view: View) {
        mainActivityPresenter.onCreateNew()
    }

    fun onRemoveCurrentButtonClicked(view: View) {
        mainActivityPresenter.onRemoveCurrent()
    }

    private fun onAddressBarTextEntered(
        textView: TextView?,
        actionId: Int?,
        event: KeyEvent?
    ): Boolean {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            mainActivityPresenter.onUserAddressEntered(textView?.text.toString())
            return true
        }
        return false
    }
}
