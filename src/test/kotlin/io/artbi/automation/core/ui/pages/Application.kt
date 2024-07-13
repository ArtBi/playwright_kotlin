package io.artbi.automation.core.ui.pages

import com.microsoft.playwright.Page
import io.artbi.automation.core.reporter.Step

class Application(
    val page: Page,
) {
    val subReddit = SubRedditPage(page)
    val loginPage = LoginPage(page)
    val mainPage = MainPage(page)

    @Step("Refresh the page")
    fun refresh() = page.reload()

    @Step("Press Enter")
    fun pressEnter() = page.keyboard().press("Enter")
}
