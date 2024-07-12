package io.artbi.automation.core.ui.components

import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page

class Header(val page: Page) {

    val logoutButton: Locator by lazy { page.locator("#navbarTogglerDemo01 > button") }
}
