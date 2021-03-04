package com.example.webviewbrowser.activity

import com.example.webviewbrowser.model.Page
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Before
import org.junit.Test

class MainActivityPresenterTest {
    private lateinit var mainActivityPresenter: MainActivityPresenter

    private lateinit var mainActivityMock: MainActivity

    @Before
    fun setUp() {
        mainActivityMock = mockk(relaxed = true)

        mainActivityPresenter = MainActivityPresenter.newInstance(mainActivityMock)
    }

    @Test
    fun `first page should be loaded on startup`() {
        mainActivityPresenter.init()

        verifyOrder {
            mainActivityMock.createPage(any())
            mainActivityMock.loadPage(any())
        }
    }

    @Test
    fun `new page should be shown and loaded in onCreateNew method`() {
        mainActivityPresenter.onCreateNew(TEST_ADDRESS)

        val testPage = Page(TEST_ADDRESS)

        verifyOrder {
            mainActivityMock.createPage(testPage)
            mainActivityMock.showPage(testPage)
            mainActivityMock.loadPage(testPage)
        }
    }

    @Test
    fun `current page should be removed and shown page that was created before in onRemoveCurrent method`() {
        mainActivityPresenter.onCreateNew(TEST_ADDRESS2)
        mainActivityPresenter.onCreateNew(TEST_ADDRESS)

        val pageBefore = Page(TEST_ADDRESS2)
        val currentPage = Page(TEST_ADDRESS)

        mainActivityPresenter.onRemoveCurrent()

        verifyOrder {
            mainActivityMock.removePage(currentPage)
            mainActivityMock.showPage(pageBefore)
        }
    }

    @Test
    fun `current page should be replaced when there are no other pages in onRemoveCurrent method`() {
        mainActivityPresenter.init()
        mainActivityPresenter.onRemoveCurrent()

        verifyOrder {
            mainActivityMock.changePage(any(), any())
            mainActivityMock.loadPage(any())
        }
    }

    @Test
    fun `selected page should be updated in onUpdateButton method`() {
        mainActivityPresenter.onCreateNew(TEST_ADDRESS)
        mainActivityPresenter.onCreateNew(TEST_ADDRESS2)
        mainActivityPresenter.onCreateNew(TEST_ADDRESS3)

        mainActivityPresenter.onUpdateButton(
            Page(TEST_ADDRESS3),
            Page(TEST_ADDRESS)
        )

        verify { mainActivityMock.setTextOfButton(Page(TEST_ADDRESS3), Page(TEST_ADDRESS)) }

        verify(exactly = 0) { mainActivityMock.setTextOfButton(Page(TEST_ADDRESS2), any()) }
    }

    @Test
    fun `current page should be refreshed in selectPage method`() {
        Page(TEST_ADDRESS).let {
            mainActivityPresenter.onCreateNew(it.path)
            mainActivityPresenter.selectPage(it)

            verify { mainActivityMock.refreshPage(it) }
        }
    }

    @Test
    fun `page should be shown if it is not current page in selectPage method`() {
        mainActivityPresenter.onCreateNew(TEST_ADDRESS)
        mainActivityPresenter.onCreateNew(TEST_ADDRESS2)

        mainActivityPresenter.selectPage(Page(TEST_ADDRESS))

        verify { mainActivityMock.showPage(Page(TEST_ADDRESS)) }
    }

    companion object {
        private const val TEST_ADDRESS = "https://www.example.com/"
        private const val TEST_ADDRESS2 = "https://www.some.another.example.com/"
        private const val TEST_ADDRESS3 = "https://www.google.com/"
    }
}