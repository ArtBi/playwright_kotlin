package io.artbi.automation.pageObjects

import com.microsoft.playwright.Page
import io.artbi.automation.pageObjects.widgets.MenuWidget

class MainPage(override val page: Page) : BasePage(page = page) {

    val menu: MenuWidget = MenuWidget(page)

    override fun getPageUrl() = "login"
}