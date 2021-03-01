package com.example.webviewbrowser.controller

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import com.example.webviewbrowser.view.fragment.PageFragment
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

class PageControllerTest {
    private val fragmentManagerMock: FragmentManager = mockk(relaxed = true)

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        PageController.fragmentManager = Pair(fragmentManagerMock, 0)

        mockkObject(ButtonController)

        mockkObject(PageFragment)
        every { PageFragment.newInstance() } answers {
            mockk<PageFragment>(relaxed = true).apply {
                every { this@apply.lifecycle.currentState } returns Lifecycle.State.CREATED
            }
        }

        every { ButtonController.addButton(any()) } returns Unit
        every { ButtonController.removeButton(any()) } returns Unit

        Dispatchers.setMain(Dispatchers.Default)
    }

    @Test
    fun `Create new page listener should create new button in ButtonController`() {
        PageController.createNewPageListener.invoke(null)
        verify(exactly = 1) { ButtonController.addButton(any()) }
    }

    @Test
    fun `Delete current page listener should remove button in ButtonController`() {
        PageController.createNewPageListener.invoke(null)
        PageController.deleteCurrentPageListener.invoke(null)
        verify(exactly = 1) { ButtonController.removeButton(any()) }
    }

    @Test
    fun `Current page selection should refresh current page`() {
        val pageFragmentList: MutableList<PageFragment> = mutableListOf()

        every { ButtonController.addButton(any()) } answers {
            pageFragmentList.add(firstArg())
        }

        PageController.createNewPageListener.invoke(null)

        @Suppress("ControlFlowWithEmptyBody")
        while (pageFragmentList.size == 0); //Wait for PageFragment addition

        PageController.selectPage(pageFragmentList.last())
        verify(exactly = 1) { pageFragmentList.last().refresh() }
    }

    @Test
    fun `Only current page should be refreshed on selectPage invocation`() {
        val pageFragmentList: MutableList<PageFragment> = mutableListOf()

        every { ButtonController.addButton(any()) } answers {
            pageFragmentList.add(firstArg())
        }

        PageController.createNewPageListener.invoke(null)
        PageController.createNewPageListener.invoke(null)

        PageController.selectPage(pageFragmentList.first())

        verify(exactly = 1) { pageFragmentList.first().refresh() }
        verify(exactly = 0) { pageFragmentList.last().refresh() }
    }
}