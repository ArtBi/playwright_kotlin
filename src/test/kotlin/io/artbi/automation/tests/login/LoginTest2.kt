package io.artbi.automation.tests.login

import com.microsoft.playwright.Page
import com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat
import io.artbi.automation.core.playwright.PlaywrightTestFixture
import io.artbi.automation.core.ui.pages.Application
import mu.KLogging
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LoginTest2 : PlaywrightTestFixture() {

    companion object : KLogging()

    private lateinit var app: Application

    @BeforeEach
    fun setupApplication(page: Page) {
        app = Application(page)
    }

    @Test
    fun testLogin2() {
        app.loginPage.open()
        app.loginPage.login("admin", "123456")

        assertThat(app.mainPage.header.logoutButton).hasText("Logout")
        app.refresh()
        app.mainPage.header.logoutButton.click()
        assertThat(app.loginPage.pageHeader).hasText("Loginn")
    }
}
