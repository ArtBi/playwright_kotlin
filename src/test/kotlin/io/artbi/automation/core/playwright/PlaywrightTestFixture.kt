package io.artbi.automation.core.playwright

import com.microsoft.playwright.BrowserContext
import com.microsoft.playwright.Page
import com.microsoft.playwright.Tracing
import com.microsoft.playwright.junit.UsePlaywright
import io.artbi.automation.core.reporter.HtmlReporter
import io.artbi.automation.core.reporter.TestResultLoggerExtension
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import mu.KLogging
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInfo
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.RegisterExtension
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.Base64
import java.util.concurrent.TimeoutException
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@UsePlaywright(CustomOptions::class)
// @ExtendWith(TestResultLoggerExtension::class, SoftAssertionsExtension::class)
open class PlaywrightTestFixture {
    companion object : KLogging() {
        @JvmStatic
        @RegisterExtension
        private val testResultLoggerExtension = TestResultLoggerExtension()
    }

    @BeforeEach
    fun setupBrowserContext(
        browserContext: BrowserContext,
        testInfo: TestInfo,
    ) {
        browserContext.tracing().start(TracingHelper.getTracingOptions())
        HtmlReporter.createTest(testInfo.displayName)
        HtmlReporter.addTestInfo(testInfo)
        HtmlReporter.startTestTimer(testInfo.displayName)
    }

    @AfterEach
    fun tearDownContext(
        testInfo: TestInfo,
        browserContext: BrowserContext,
        page: Page,
    ) {
        HtmlReporter.addScreenshot(page, "Failure Screenshot")
        // HtmlReporter.getTest().fail("Test failed")
        page.close()
        HtmlReporter.endTestTimer(testInfo.displayName)
        handleTracing(testInfo, browserContext)
        handleVideo(page, testInfo)
        HtmlReporter.flush()
    }

    private fun handleTracing(
        testInfo: TestInfo,
        browserContext: BrowserContext,
    ) {
        if (testResultLoggerExtension.isTestFailed(testInfo.displayName)) {
            val traceName = TracingHelper.getTraceName(testInfo)
            TracingHelper.saveTraceReport(browserContext, traceName)
        }
    }

    private fun handleVideo(
        page: Page,
        testInfo: TestInfo,
    ) {
        runBlocking {
            page.video().saveAs(Paths.get("build/reports/videos/${testInfo.testClass.get().simpleName}_${testInfo.displayName}.webm"))
            val base64Video = VideoHelper.getVideoAsBase64(page)
            HtmlReporter.addVideoFromBase64String(base64Video, testInfo.displayName)
        }
    }
}

object TracingHelper {
    fun getTracingOptions() =
        Tracing
            .StartOptions()
            .setScreenshots(true)
            .setSnapshots(true)

    fun getTraceName(testInfo: TestInfo) = "${testInfo.testClass.get().simpleName}-${testInfo.testMethod.get().name}-trace.zip"

    fun saveTraceReport(
        browserContext: BrowserContext,
        traceName: String,
    ) {
        PlaywrightTestFixture.logger.info { "Saving report: $traceName" }
        browserContext.tracing().stop(
            Tracing.StopOptions().setPath(Paths.get("build/reports/traces/$traceName")),
        )
    }
}

object VideoHelper {
    private const val STABLE_SIZE_CHECK_COUNT = 3
    private const val DELAY_BETWEEN_CHECKS = 100L // milliseconds

    suspend fun waitForVideoFile(
        path: Path,
        timeout: Duration = 30.seconds,
    ): ByteArray =
        withContext(Dispatchers.IO) {
            val startTime = System.currentTimeMillis()
            var previousSize = -1L
            var stableSizeCount = 0

            while (System.currentTimeMillis() - startTime <= timeout.inWholeMilliseconds) {
                if (!Files.exists(path)) {
                    delay(DELAY_BETWEEN_CHECKS)
                    continue
                }

                val currentSize = Files.size(path)

                if (currentSize == previousSize) {
                    stableSizeCount++
                    if (stableSizeCount >= STABLE_SIZE_CHECK_COUNT) {
                        return@withContext Files.readAllBytes(path)
                    }
                } else {
                    stableSizeCount = 0
                }

                previousSize = currentSize
                delay(DELAY_BETWEEN_CHECKS)
            }

            throw TimeoutException("Timeout waiting for video file to stabilize: $path")
        }

    suspend fun getVideoAsBase64(page: Page): String {
        val videoPath = page.video().path().toString()
        val path = Paths.get(videoPath)
        val videoBytes = waitForVideoFile(path)
        return Base64.getEncoder().encodeToString(videoBytes)
    }
}
