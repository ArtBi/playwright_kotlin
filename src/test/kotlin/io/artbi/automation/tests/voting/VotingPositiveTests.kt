package io.artbi.automation.tests.voting

import com.microsoft.playwright.Page
import com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat
import io.artbi.automation.core.playwright.PlaywrightTestFixture
import io.artbi.automation.core.ui.pages.Application
import mu.KLogging
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class VotingPositiveTests : PlaywrightTestFixture() {
    companion object : KLogging()

    private lateinit var app: Application

    @BeforeEach
    fun setupApplication(page: Page) {
        app = Application(page)
    }

    @Test
    fun `Verify downvote works for logged in User`() {
        app.mainPage.open()
        app.mainPage.header.search
            .searchFor("gaming")
        app.mainPage.header.search
            .searchResultItemWithText("r/gaming")
            .click()
        assertThat(app.subReddit.articles[1]).isVisible()
        app.subReddit.downvoteArticle(1)
        assertThat(app.loginPage.getLoginPageHeader()).isVisible()
    }

    @Test
    fun `Verify upvote works for logged in User`() {
        app.mainPage.open()
        app.mainPage.header.search
            .searchFor("gaming")
        app.mainPage.header.search
            .searchResultItemWithText("r/gaming")
            .click()

        assertThat(app.subReddit.articles[2]).isVisible()
        app.subReddit.upvoteArticle(2)
        assertThat(app.loginPage.getLoginPageHeader()).isVisible()
    }
}
