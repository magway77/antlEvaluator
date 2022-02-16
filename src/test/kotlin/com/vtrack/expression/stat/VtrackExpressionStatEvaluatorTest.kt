package com.vtrack.expression.stat

import com.vtrack.expression.VtrackExpressionStatEvaluator
import com.vtrack.expression.VtrackExpressionStatParams
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class VtrackExpressionStatEvaluatorTest {

    @Test
    internal fun test_test_parseExpression() {
        val result: VtrackExpressionStatParams =
            VtrackExpressionStatEvaluator().evaluate("alert1:r1:Antenna[1,2]:[10]/30m ")
        val expected = VtrackExpressionStatParams().apply {
            alertName = "alert1"
            sourceId = "r1"
        }
        assertEquals(expected, result)
    }
}
