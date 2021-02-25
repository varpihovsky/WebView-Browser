package com.example.webviewbrowser.controller

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.example.webviewbrowser.view.fragment.PageFragment
import io.mockk.*
import org.junit.Before
import org.junit.Test

class ControllerTest {
    val fragmentManagerMock: FragmentManager = mockk(relaxed = true)

    @Before
    fun setUp() {
        Controller.fragmentManager = Pair(fragmentManagerMock, 0)

        mockkObject(ButtonController)

        mockkObject(PageFragment)
        every { PageFragment.newInstance() } returns mockk(relaxed = true) andThenAnswer {
            mockk(
                relaxed = true
            )
        }

        every { ButtonController.addButton(any()) } returns Unit
        every { ButtonController.removeButton(any()) } returns Unit
    }

    @Test
    fun `Create new page listener should create new button in ButtonController`() {
        Controller.createNewPageListener.invoke(null)
        verify(exactly = 1) { ButtonController.addButton(any()) }
    }

    @Test
    fun `Delete current page listener should remove button in ButtonController`() {
        Controller.createNewPageListener.invoke(null)
        Controller.deleteCurrentPageListener.invoke(null)
        verify(exactly = 1) { ButtonController.removeButton(any()) }
    }

    @Test
    fun `Current page selection should refresh current page`() {
        val pageFragmentList: MutableList<PageFragment> = mutableListOf()

        every { ButtonController.addButton(any()) } answers {
            pageFragmentList.add(firstArg())
        }

        Controller.createNewPageListener.invoke(null)

        Controller.selectPage(pageFragmentList.last())
        verify(exactly = 1) { pageFragmentList.last().refresh() }
    }

    @Test
    fun test() {
        val pageFragmentList: MutableList<PageFragment> = mutableListOf()

        every { ButtonController.addButton(any()) } answers {
            pageFragmentList.add(firstArg())
        }

        Controller.createNewPageListener.invoke(null)
        Controller.createNewPageListener.invoke(null)

        Controller.selectPage(pageFragmentList.first())

        verify(exactly = 1) { pageFragmentList.first().refresh() }
        verify(exactly = 0) { pageFragmentList.last().refresh() }
    }
}