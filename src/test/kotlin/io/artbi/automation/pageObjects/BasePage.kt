package io.artbi.automation.pageObjects

import com.microsoft.playwright.Page

abstract class BasePage(open val page: Page) {

    fun open() = page.navigate("/${getPageUrl()}")
    abstract fun getPageUrl(): String
}
