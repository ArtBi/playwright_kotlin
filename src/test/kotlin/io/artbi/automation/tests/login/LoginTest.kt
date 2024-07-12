package io.artbi.automation.tests.login

import com.microsoft.playwright.Page
import com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat
import io.artbi.automation.pageObjects.Application
import io.artbi.automation.core.playwright.PlaywrightTestFixture
import mu.KLogging
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LoginTest: PlaywrightTestFixture() {

    companion object : KLogging()

    private lateinit var app: Application

    @BeforeEach
    fun setupApplication(page: Page) {
        app = Application(page)
    }

    @Test
    fun testLogin() {
        app.loginPage.open()
        app.loginPage.login("admin", "123456")

        assertThat(app.mainPage.menu.logoutButton).hasText("Logout")
        app.refresh()
        app.mainPage.menu.logoutButton.click()
        assertThat(app.loginPage.pageHeader).hasText("Loginn")
    }
}