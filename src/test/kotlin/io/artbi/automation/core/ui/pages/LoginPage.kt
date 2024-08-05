package io.artbi.automation.core.ui.pages

import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page
import com.microsoft.playwright.options.AriaRole
import io.artbi.automation.core.reporter.Step

class LoginPage(
    override val page: Page,
) : BasePage(page = page) {
    private val pageHeader: Locator by lazy { page.getByRole(AriaRole.HEADING, Page.GetByRoleOptions().setName("Sign Up")) }
    private val closeButton: Locator by lazy { page.locator("button[aria-label='Close']") }
    private val loginLink: Locator by lazy { page.locator("auth-flow-link") }
    private val loginField: Locator by lazy { page.locator("input[id='login-username']") }
    private val passwordField: Locator by lazy { page.locator("input[id='login-password']") }
    private val loginButton: Locator by lazy { page.locator("button[class *= 'login']") }

    @Step("Get page header")
    fun getLoginPageHeader(): Locator = pageHeader

    @Step("User login with: {0}, {1}")
    fun login(
        username: String,
        password: String,
    ) {
        loginField.fill(username)
        passwordField.fill(password)
        loginButton.click()
    }

    @Step("Close Login form")
    fun closeLoginForm() {
        closeButton.click()
    }

    @Step("Click on Log In link")
    fun clickOnLogInLink() {
        loginLink.click()
    }

    override fun getPageUrl(): String = "/login"
}
