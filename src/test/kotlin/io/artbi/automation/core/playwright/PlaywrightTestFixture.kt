package io.artbi.automation.core.playwright

import com.microsoft.playwright.BrowserContext
import com.microsoft.playwright.Tracing
import com.microsoft.playwright.junit.UsePlaywright
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInfo
import org.junit.jupiter.api.TestInstance
import java.nio.file.Paths

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@UsePlaywright(CustomOptions::class)
open class PlaywrightTestFixture {

    @BeforeEach
    fun setupBrowserContext(browserContext: BrowserContext) {
        browserContext.tracing().start(getTracingOptions())
    }

    fun getTracingOptions() = Tracing.StartOptions()
        .setScreenshots(true)
        .setSnapshots(true)

    @AfterEach
    fun tearDownContext(testInfo: TestInfo, browserContext: BrowserContext) {
        val traceName = getTraceName(testInfo)
        saveTraceReport(browserContext, traceName)
    }

    private fun getTraceName(testInfo: TestInfo) =
        "${testInfo.testClass.get().simpleName}-${testInfo.testMethod.get().name}-trace.zip"

    private fun saveTraceReport(browserContext: BrowserContext, traceName: String) {
        browserContext.tracing().stop(Tracing.StopOptions().setPath(Paths.get("build/reports/traces/$traceName")))
    }
}
