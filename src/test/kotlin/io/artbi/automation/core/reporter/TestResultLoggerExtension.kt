package io.artbi.automation.core.reporter

import mu.KLogging
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.TestWatcher
import java.util.Optional
import java.util.concurrent.ConcurrentHashMap

class TestResultLoggerExtension :
    TestWatcher,
    BeforeEachCallback,
    AfterEachCallback,
    AfterAllCallback {
    companion object : KLogging()

    private val testResults = ConcurrentHashMap<String, Boolean>()
    private val testResultsStatus = ArrayList<TestResultStatus>()

    private enum class TestResultStatus {
        PASSED,
        ABORTED,
        FAILED,
        DISABLED,
    }

    override fun testSuccessful(context: ExtensionContext) {
        testResultsStatus.add(TestResultStatus.PASSED)
        context.displayName?.let { displayName ->
            testResults[displayName] = true
            logger.info("Test '$displayName' - ${TestResultStatus.PASSED}")
        }
    }

    override fun testFailed(
        context: ExtensionContext,
        cause: Throwable,
    ) {
        testResultsStatus.add(TestResultStatus.FAILED)
        context.displayName.let { displayName ->
            testResults[displayName] = false
            logger.error("Test '$displayName' - ${TestResultStatus.FAILED}. Reason: ${cause.message}", cause)
        }
    }

    override fun testAborted(
        context: ExtensionContext,
        cause: Throwable,
    ) {
        testResultsStatus.add(TestResultStatus.ABORTED)
        context.displayName?.let { displayName ->
            testResults[displayName] = false
            logger.warn("Test '$displayName' - ${TestResultStatus.ABORTED}. Reason: ${cause.message}", cause)
        }
    }

    override fun testDisabled(
        context: ExtensionContext,
        reason: Optional<String>,
    ) {
        testResultsStatus.add(TestResultStatus.DISABLED)
        context.displayName.let { displayName ->
            testResults[displayName] = false
            if (reason.isPresent) {
                logger.info("Test '$displayName' - ${TestResultStatus.DISABLED}. Reason: ${reason.get()}")
            } else {
                logger.info("Test '$displayName' - ${TestResultStatus.DISABLED}")
            }
        }
    }

    override fun beforeEach(context: ExtensionContext) {
        logger.info("START TEST ${context.displayName} with Tags ${context.tags}")
    }

    fun getTestStatus(displayName: String): Boolean = testResults.getOrDefault(displayName, false)

    fun isTestFailed(displayName: String): Boolean = !getTestStatus(displayName)

    override fun afterEach(context: ExtensionContext) {
        testResults[context.displayName] = false
        logger.info("FINISH TEST ${context.displayName} with Tags ${context.tags}")
    }

    override fun afterAll(context: ExtensionContext) {
        val summary =
            testResultsStatus
                .groupingBy { it }
                .eachCount()

        logger.info("Test result summary for {} {}", context.displayName, summary.toString())
    }
}
