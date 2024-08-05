package io.artbi.automation.core.reporter

import com.aventstack.extentreports.ExtentReports
import com.aventstack.extentreports.ExtentTest
import com.aventstack.extentreports.reporter.ExtentSparkReporter
import com.aventstack.extentreports.reporter.configuration.Theme
import com.microsoft.playwright.Page
import org.junit.jupiter.api.TestInfo
import java.util.Base64

object HtmlReporter {
    private lateinit var reports: ExtentReports
    internal val testThreadLocal: ThreadLocal<ExtentTest> = ThreadLocal()
    internal val testNameThreadLocal: ThreadLocal<String> = ThreadLocal()

    init {
        initializeReporter("build/reports/extent_reports.html")
    }

    fun initializeReporter(fileName: String) {
        val sparkReporter = ExtentSparkReporter(fileName)

        sparkReporter.config().theme = Theme.DARK
        sparkReporter.config().isTimelineEnabled = true
        sparkReporter.config().setTimeStampFormat("dd MMM yyyy HH:mm:ss")

        sparkReporter.config().documentTitle = "Playwright Test Report"
        sparkReporter.config().reportName = "Automated Test Results"

        reports = ExtentReports()
        reports.attachReporter(sparkReporter)

        reports.setSystemInfo("OS", System.getProperty("os.name"))
        reports.setSystemInfo("Java Version", System.getProperty("java.version"))
    }

    fun addTestInfo(testInfo: TestInfo) {
        val test = getTest()
        test.assignCategory(testInfo.testClass.get().simpleName)
        testInfo.tags.forEach { test.assignCategory(it) }
        test.assignAuthor(System.getProperty("user.name"))
        test.assignDevice(System.getProperty("os.name"))
    }

    fun createTest(name: String): ExtentTest {
        val test = reports.createTest(name)
        testThreadLocal.set(test)
        testNameThreadLocal.set(name)
        return test
    }

    fun getTest(): ExtentTest =
        testThreadLocal.get()
            ?: throw IllegalStateException("Test not initialized")

    fun getCurrentTestName(): String =
        testNameThreadLocal.get()
            ?: throw IllegalStateException("Test name not set")

    fun endTest() {
        testThreadLocal.remove()
        testNameThreadLocal.remove()
    }

    fun flush() {
        reports.flush()
        endTest()
    }

    fun addVideoFromBase64String(
        base64Video: String,
        testName: String,
    ) {
        getTest().addVideoFromBase64String(base64Video, testName)
    }

    fun addScreenshot(
        page: Page,
        name: String,
    ) {
        val screenshot = page.screenshot(Page.ScreenshotOptions().setFullPage(true))
        getTest().addScreenCaptureFromBase64String(
            Base64.getEncoder().encodeToString(screenshot),
            name,
        )
    }

    private val testStartTimes = mutableMapOf<String, Long>()

    fun startTestTimer(testName: String) {
        testStartTimes[testName] = System.currentTimeMillis()
    }

    fun endTestTimer(testName: String) {
        val startTime = testStartTimes.remove(testName) ?: return
        val duration = System.currentTimeMillis() - startTime
        getTest().info("Test duration: ${duration / 1000.0} seconds")
    }
}
