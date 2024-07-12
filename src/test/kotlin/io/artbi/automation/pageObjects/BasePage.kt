package io.artbi.automation.pageObjects

import com.microsoft.playwright.Page

abstract class BasePage(open val page: Page) {
    companion object {
        private fun getBaseUrl() = "http://localhost:8087"
    }

//    fun open() = page.navigate("${getBaseUrl()}/${getPageUrl()}")
    fun open() = page.navigate("/${getPageUrl()}")
    abstract fun getPageUrl(): String
}