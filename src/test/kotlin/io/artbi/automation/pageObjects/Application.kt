package io.artbi.automation.pageObjects

import com.microsoft.playwright.Page

class Application(val page: Page) {

     val loginPage: LoginPage = LoginPage(page)
     val mainPage: MainPage = MainPage(page)

     fun refresh() = page.reload()
}