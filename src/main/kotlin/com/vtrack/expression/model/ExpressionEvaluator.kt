package com.vtrack.expression.model

import com.vtrack.generated.antlr.ExprLexer
import com.vtrack.generated.antlr.ExprParser
import expression.EvalVisitor
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream

class ExpressionEvaluator {

    val visitor = EvalVisitor()

    fun evaluate(statement: String): VariantType {
        val lexer = ExprLexer(CharStreams.fromString(statement))
        val tokens = CommonTokenStream(lexer)
        val parser = ExprParser(tokens)
        return visitor.visit(parser.statement())
    }

    fun setVariable(variable: String, value: List<Any>): ExpressionEvaluator {
        visitor.variables[variable] = ListType().also { it.value = value }
        return this
    }
}
