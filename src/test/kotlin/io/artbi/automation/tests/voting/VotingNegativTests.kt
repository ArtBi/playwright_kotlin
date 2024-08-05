package io.artbi.automation.tests.voting

import com.microsoft.playwright.Page
import com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat
import io.artbi.automation.core.playwright.PlaywrightTestFixture
import io.artbi.automation.core.ui.pages.Application
import mu.KLogging
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class VotingNegativTests : PlaywrightTestFixture() {
    companion object : KLogging()

    private lateinit var app: Application

    @BeforeEach
    fun setupApplication(page: Page) {
        app = Application(page)
    }

    @Test
    fun `Verify downvote works only after login`() {
        app.mainPage.open()
        app.mainPage.header.search
            .searchFor("gaming")
        app.mainPage.header.search
            .searchResultItemWithText("r/gaming")
            .click()
        logger.info(app.subReddit.articleTitle(1))
        app.subReddit.downvoteArticle(1)
        assertThat(app.loginPage.getLoginPageHeader()).isVisible()
    }

    @Test
    fun `Verify upvote works only after login`() {
        app.mainPage.open()
        app.mainPage.header.search
            .searchFor("gaming")
        app.mainPage.header.search
            .searchResultItemWithText("r/gaming")
            .click()
        app.subReddit.upvoteArticle(2)
        assertThat(app.loginPage.getLoginPageHeader()).isVisible()
    }
}
