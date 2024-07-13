package io.artbi.automation.core.ui.pages

import com.microsoft.playwright.Page
import io.artbi.automation.core.ui.components.Header

class MainPage(
    override val page: Page,
) : BasePage(page = page) {
    val header: Header = Header(page)

    override fun getPageUrl() = "/"
}
