package io.artbi.automation.core.playwright

import com.microsoft.playwright.Browser
import com.microsoft.playwright.BrowserType
import com.microsoft.playwright.junit.Options
import com.microsoft.playwright.junit.OptionsFactory
import java.nio.file.Paths

class CustomOptions : OptionsFactory {

    override fun getOptions(): Options = Options()
        .setHeadless(false)
        .setLaunchOptions(getLaunchOptions())
        .setContextOptions(getContextOptions())
        .setLaunchOptions(BrowserType.LaunchOptions().setTracesDir(Paths.get("")))

    private fun getLaunchOptions() = BrowserType.LaunchOptions()
        .setHeadless(false)

    private fun getContextOptions() = Browser.NewContextOptions()
        .setBaseURL("http://localhost:8087")
        .setRecordVideoDir(Paths.get("build/reports/videos/"))
}
