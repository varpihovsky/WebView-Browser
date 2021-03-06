package com.example.webviewbrowser.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.webviewbrowser.*
import com.example.webviewbrowser.databinding.ActivityMainBinding
import com.example.webviewbrowser.model.Page
import com.example.webviewbrowser.model.PageButton
import com.example.webviewbrowser.page.PageFragment

class MainActivity : AppCompatActivity(), MainActivityInterface {
    private lateinit var mainActivityPresenter: MainActivityPresenter
    private lateinit var binding: ActivityMainBinding
    private var isUiNeedsRestoration = false

    override fun onCreate(savedInstanceState: Bundle?) {
        restorePresenter(savedInstanceState?.getParcelable(PRESENTER_PARAM))

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        if (savedInstanceState != null) {
            restore(savedInstanceState)
        } else {
            init()
        }
    }

    private fun init() {
        mainActivityPresenter = MainActivityPresenter.newInstance(this)

        mainActivityPresenter.init()
    }

    private fun restore(savedInstanceState: Bundle) {
        savedInstanceState.run {
            supportFragmentManager.removeAllFragments()

            restoreUI(getBundle(PAGE_BUTTON_DATA_PARAM))
            isUiNeedsRestoration = true
        }
    }

    private fun restorePresenter(parcelablePresenter: MainActivityPresenter?) {
        parcelablePresenter?.let {
            mainActivityPresenter = it
            mainActivityPresenter.bind(this)
        }
    }

    private fun restoreUI(pageButtonBundle: Bundle?) {
        pageButtonBundle?.let {
            val size = it.getInt(PAGE_BUTTON_DATA_SIZE_PARAM)
            for (i in 1..size) {
                it.getParcelable<Page>(i.toString())?.let { it1 ->
                    createPage(it1)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (isUiNeedsRestoration) {
            mainActivityPresenter.onRestore()
        }
    }

    @Suppress("DEPRECATION")
    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment is PageFragment) {
            fragment.attachMainActivityPresenterInterface(mainActivityPresenter)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mainActivityPresenter.unBind()

        outState.apply {
            putParcelable(PRESENTER_PARAM, mainActivityPresenter)
            putBundle(
                PAGE_BUTTON_DATA_PARAM,
                Bundle().apply {
                    putInt(PAGE_BUTTON_DATA_SIZE_PARAM, binding.buttonsLayout.childCount)
                    binding.buttonsLayout.children.forEachIndexed { index, view ->
                        if (view is PageButton) {
                            putParcelable(index.toString(), view.page)
                        }
                    }
                }
            )
        }
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

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add(
                binding.browserFragment.id,
                PageFragment.newInstance(page)
            )
        }
    }

    override fun removePage(page: Page) {
        supportFragmentManager.commit {
            supportFragmentManager.getFragmentByPage(page)?.let { ::remove }
            supportFragmentManager.fragments.lastOrNull()?.let { ::show }
        }

        binding.buttonsLayout.letFoundPageButtonByPage(page) {
            binding.buttonsLayout.removeView(it)
        }
    }

    override fun showPage(page: Page) {
        Log.d("Fragments", "size:" + supportFragmentManager.fragments.size.toString())
        supportFragmentManager.letFoundFragmentByPage(page) {
            supportFragmentManager.commit { show(it) }
        }
    }

    override fun hidePage(page: Page) {
        Log.d("Fragments", "size:" + supportFragmentManager.fragments.size.toString())
        supportFragmentManager.letFoundFragmentByPage(page) {
            supportFragmentManager.commit { hide(it) }
        }
    }

    override fun loadPage(page: Page) {
        Log.d("Fragments", "size:" + supportFragmentManager.fragments.size.toString())
        supportFragmentManager.letFoundFragmentByPage(page) {
            if (it is PageFragment) it.load()
        }
    }

    override fun setTextOfButton(from: Page, to: Page) {
        binding.buttonsLayout.letFoundPageButtonByPage(from) {
            if (it is PageButton) {
                it.text = to.toString()
                it.page = to
            }
        }
    }

    override fun changePage(from: Page, to: Page) {
        supportFragmentManager.letFoundFragmentByPage(from) {
            if (it is PageFragment) it.page = to
        }
        binding.buttonsLayout.letFoundPageButtonByPage(from) {
            if (it is PageButton) it.page = to
        }
    }

    override fun refreshPage(page: Page) {
        supportFragmentManager.letFoundFragmentByPage(page) {
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

    companion object {
        private const val PRESENTER_PARAM = "presenter"
        private const val PAGE_BUTTON_DATA_PARAM = "pageButtonData"
        private const val PAGE_BUTTON_DATA_SIZE_PARAM = "pageButtonDataSize"
    }
}
