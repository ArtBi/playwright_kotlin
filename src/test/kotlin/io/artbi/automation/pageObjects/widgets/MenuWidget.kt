package io.artbi.automation.pageObjects.widgets

import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page

class MenuWidget(val page: Page) {

    val logoutButton: Locator by lazy { page.locator("#navbarTogglerDemo01 > button") }
}