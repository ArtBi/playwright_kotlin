package io.artbi.automation.core.reporter

import mu.KLogging
import org.apache.commons.lang3.StringUtils
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import java.text.MessageFormat

@Aspect
class StepAspect {
    companion object : KLogging()

    @Pointcut("@annotation(Step) && execution(* *(..))")
    fun stepMethod() {
    }

    @Around("stepMethod()")
    @Throws(Throwable::class)
    fun stepMethodAdvice(joinPoint: ProceedingJoinPoint): Any? {
        val methodSignature = joinPoint.signature as MethodSignature
        val methodName = methodSignature.name
        val stepAnnotation = methodSignature.method.getAnnotation(Step::class.java)
        val userDefinedName = stepAnnotation?.value ?: ""

        val formattedName =
            if (StringUtils.isNotBlank(userDefinedName)) {
                val args = joinPoint.args
                val escapedUserDefinedName = userDefinedName.replace("{}", "{0}")
                MessageFormat.format(escapedUserDefinedName, *args)
            } else {
                methodName
            }

        try {
            val result = joinPoint.proceed()
            HtmlReporter.getTest().pass(formattedName)
            return result
        } catch (e: Throwable) {
            HtmlReporter.getTest().fail(formattedName)
            throw e
        }
    }
}
