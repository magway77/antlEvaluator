package com.vtrack.expression.calc

import com.vtrack.generated.antlr.ExprLexer
import com.vtrack.generated.antlr.ExprParser
import com.vtrack.expression.calc.model.ListType
import com.vtrack.expression.calc.model.VariantType
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream

class CalcExpressionEvaluator {

    val visitor = CalcEvalVisitor()

    fun evaluate(statement: String): VariantType {
        val lexer = ExprLexer(CharStreams.fromString(statement))
        val tokens = CommonTokenStream(lexer)
        val parser = ExprParser(tokens)
        return visitor.visit(parser.statement())
    }

    fun setVariable(variable: String, value: List<Any>): CalcExpressionEvaluator {
        visitor.variables[variable] = ListType().also { it.value = value }
        return this
    }
}
