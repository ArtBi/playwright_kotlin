package io.artbi.automation.core.ui.pages

import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page
import io.artbi.automation.core.reporter.Step

class SubRedditPage(
    page: Page,
) {
    val articles: List<Locator> by lazy { page.locator("#main-content article").all() }

    @Step("Downvote article #{0}")
    fun downvoteArticle(int: Int) {
        articles[int].locator("button[downvote]").click()
    }

    @Step("Upvote article #{0}")
    fun upvoteArticle(int: Int) {
        articles[int].locator("button[upvote]").click()
    }

    @Step("Get Title for article #{0}")
    fun articleTitle(int: Int): String? = articles[int].getAttribute("aria-label")
}
