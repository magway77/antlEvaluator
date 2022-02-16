package com.vtrack.expression

import org.antlr.v4.runtime.*
import org.antlr.v4.runtime.tree.ParseTreeVisitor

interface ExpressionEvaluator<T> {

    fun evaluate(statement: String): T

    fun setVariable(variable: String, value: T): ExpressionEvaluator<T>
}


interface ExprVisitorBase<T> : ParseTreeVisitor<T>{
    val variables: HashMap<String, T>
}


abstract class ExpressionEvaluatorBase<T>(
    protected val visitor: ExprVisitorBase<T>
) : ExpressionEvaluator<T> {

    override fun setVariable(variable: String, value: T): ExpressionEvaluator<T> {
        visitor.variables[variable] = value
        return this
    }

}
