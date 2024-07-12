package io.artbi.automation.pageObjects

import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page

class LoginPage(override val page: Page) : BasePage(page = page) {
    val usernameField: Locator by lazy { page.getByLabel("Username") }
    val passwordField: Locator by lazy { page.getByLabel("Password") }
    val loginButton: Locator by lazy { page.locator("app-login  button") }
    val pageHeader: Locator by lazy { page.locator("app-login h2") }

    fun login(username: String, password: String) {
        enterUsername(username)
        enterPassword(password)
        submit()
    }

    fun enterUsername(username: String) {
        usernameField.fill(username)
    }

    fun enterPassword(password: String) {
        passwordField.fill(password)
    }

    fun submit() {
        loginButton.click()
    }

    override fun getPageUrl() = "login"
}