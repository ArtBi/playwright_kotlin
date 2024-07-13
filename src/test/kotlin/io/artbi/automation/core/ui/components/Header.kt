package io.artbi.automation.core.ui.components

import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page
import io.artbi.automation.core.reporter.Step

class Header(
    private val page: Page,
) {
    val search = Search(page)
}

class Search(
    private val page: Page,
) {
    val searchFiled: Locator by lazy { page.locator("#search-input input[type=text]") }
    val searchResultItem: Locator by lazy { page.locator("[data-type='search-dropdown-item-label-text']") }

    @Step("Search for: {0}")
    fun searchFor(text: String) {
        searchFiled.fill(text)
    }

    @Step("Search Item with text: {0}")
    fun searchResultItemWithText(
        resultItemText: String,
        exactOption: Boolean = true,
    ) = searchResultItem.getByText(
        resultItemText,
        Locator.GetByTextOptions().setExact(exactOption),
    )
}
