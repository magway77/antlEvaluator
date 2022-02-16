package com.vtrack.expression

import com.vtrack.expression.stat.VtrackExpressionVisitor
import com.vtrack.generated.antlr.VtrackExprLexer
import com.vtrack.generated.antlr.VtrackExprParser
import org.antlr.v4.runtime.*

data class VtrackExpressionStatParams(
    var alertName: String? = null
) {
    enum class TimeDurationKind(val value: String) {

        MINUTE("m"),
        HOUR("h"),
        DAY("d");

        companion object {
            fun byValue(value: String): TimeDurationKind = values().first { it.value.equals(value, ignoreCase = true) }
        }

    }

    var sourceId: String? = null
    var qualifierName: String? = null
    var qualifierValue: String? = null
    var rangeLowValue: Int? = null
    var rangeHighValue: Int? = null
    var durationValue: Int? = null
    var durationKind: TimeDurationKind? = null
}


class VtrackExpressionStatEvaluator :
    ExpressionEvaluatorBase<VtrackExpressionStatParams>(VtrackExpressionVisitor()) {

    override fun evaluate(statement: String): VtrackExpressionStatParams {
        val lexer = VtrackExprLexer(CharStreams.fromString(statement))
        val tokens: CommonTokenStream = CommonTokenStream(lexer)
        val parser = VtrackExprParser(tokens)
        return visitor.visit(parser.expression())
    }
}

