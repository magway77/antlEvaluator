package com.vtrack.expression.calc

import com.vtrack.expression.calc.model.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.reflect.KClass

class EvalVisitorTest {

    fun assertValue(result: VariantType, kClass: KClass<out VariantType>, expected: Any? = null) {
        assertEquals(result::class, kClass)
        assertEquals(result.isNull(), expected == null)
        assertEquals(expected, result.value)

    }

    @Test
    internal fun test_test_assignment() {
        val evaluator = CalcExpressionEvaluator()
        assertValue(evaluator.evaluate("b = true"), BoolType::class, true)
        assertValue(evaluator.evaluate("b = 5"), IntType::class, 5L)
        assertValue(evaluator.evaluate("b = 3.2"), RealType::class, 3.2)
        assertValue(evaluator.evaluate("b = 3.2"), RealType::class, 3.2)
        assertValue(evaluator.evaluate("b = \"vasya\""), StringType::class, "vasya")
    }

    @Test
    internal fun test_test_addition() {
        val evaluator = CalcExpressionEvaluator()
        assertValue(evaluator.evaluate("3 + 2"), IntType::class, 5L)
        assertValue(evaluator.evaluate("2 + 3"), IntType::class, 5L)
        assertValue(evaluator.evaluate("3.7 + 2"), RealType::class, 5.7)
        assertValue(evaluator.evaluate("2 + 3.7"), RealType::class, 5.7)
    }

    @Test
    internal fun test_test_subtraction() {
        val evaluator = CalcExpressionEvaluator()
        assertValue(evaluator.evaluate("3 - 2"), IntType::class, 1L)
    }

    @Test
    internal fun test_division() {
        val evaluator = CalcExpressionEvaluator()
        assertValue(evaluator.evaluate("9 / 2"), IntType::class, 4L)
        assertValue(evaluator.evaluate("9 / 2.0"), RealType::class, 4.5)
        assertValue(evaluator.evaluate("9.0 / 2"), RealType::class, 4.5)
        assertValue(evaluator.evaluate("9.0 / 2.0"), RealType::class, 4.5)
    }

    @Test
    internal fun test_testParens() {
        val evaluator = CalcExpressionEvaluator()
        assertValue(evaluator.evaluate(" 2 + 3 * 2"), IntType::class, 8L)
        assertValue(evaluator.evaluate(" (2 + 3) * 2"), IntType::class, 10L)

    }

    @Test
    internal fun test_funCount() {
        val evaluator =
            CalcExpressionEvaluator().setVariable("e", listOf(1, 2, 3))
        assertValue(evaluator.evaluate("e.count()"), IntType::class, 3)
    }

    @Test
    internal fun test_test_filterByType() {
        val evaluator =
            CalcExpressionEvaluator().setVariable("e", listOf(1, "a2", 3))
        assertValue(evaluator.evaluate("e.filterByType(Int)"), ListType::class, listOf(1, 3))
        assertValue(evaluator.evaluate("e.count()"), IntType::class, 3)
        assertValue(evaluator.evaluate("e.filterByType(Int).count() >=3"), IntType::class, 2)
    }


}
