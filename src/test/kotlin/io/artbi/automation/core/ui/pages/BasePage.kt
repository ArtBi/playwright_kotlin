package io.artbi.automation.core.ui.pages

import com.microsoft.playwright.Page
import io.artbi.automation.core.reporter.Step

abstract class BasePage(
    open val page: Page,
) {
    @Step("Open page: {0}")
    fun open(pageUrl: String = getPageUrl()) = page.navigate(pageUrl)

    abstract fun getPageUrl(): String
}
