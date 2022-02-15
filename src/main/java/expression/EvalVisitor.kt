package expression

import com.vtrack.expression.model.*
import com.vtrack.generated.antlr.ExprBaseVisitor
import com.vtrack.generated.antlr.ExprParser
import org.antlr.v4.runtime.tree.ErrorNode
import org.antlr.v4.runtime.tree.TerminalNode

class EvalVisitor :
    ExprBaseVisitor<VariantType>() {

    private var variables: HashMap<String, VariantType> = HashMap()

    override fun defaultResult(): VariantType {
        return StringType()
    }

    override fun visitTerminal(node: TerminalNode?): VariantType {
        throw java.lang.IllegalArgumentException("visit terminal antlr node")
    }

    override fun visitErrorNode(node: ErrorNode?): VariantType {
        throw java.lang.IllegalArgumentException("visit error antlr  node")
    }

    override fun visitStatement(ctx: ExprParser.StatementContext?): VariantType {
        return visit(ctx?.expression()) ?: StringType()
    }

    override fun visitIntValue(ctx: ExprParser.IntValueContext?): VariantType {
        return ctx?.let {
            IntType().apply { value = ctx.INT().text.toLongOrNull() }
        } ?: StringType()
    }

    override fun visitDoubleValue(ctx: ExprParser.DoubleValueContext?): VariantType {
        return ctx?.let {
            RealType().apply { value = ctx.DOUBLE().text.toDoubleOrNull() }
        } ?: StringType()
    }

    override fun visitStringValue(ctx: ExprParser.StringValueContext?): VariantType {
        return ctx?.let {
            StringType().apply { value = ctx.STRING().text.replace("\"(.*)\"".toRegex(), "$1") }
        } ?: StringType()
    }

    override fun visitBoolValue(ctx: ExprParser.BoolValueContext?): VariantType {
        return BoolType().apply { value = (ctx?.TRUE() != null) }
    }

    override fun visitParens(ctx: ExprParser.ParensContext?): VariantType {
        if (ctx?.expression() == null) {
            return StringType()
        }
        return visit(ctx.expression())
    }

    override fun visitMulDiv(ctx: ExprParser.MulDivContext?): VariantType {
        if (ctx == null) {
            return StringType()
        }
        val left = visit(ctx.left)
        val right = visit(ctx.right)
        return if (ctx.operator.type == ExprParser.MULT) left.multiple(right) else left.division(right)
    }

    override fun visitAddSub(ctx: ExprParser.AddSubContext?): VariantType {
        if (ctx == null) {
            return StringType()
        }
        val left = visit(ctx.left)
        val right = visit(ctx.right)
        return if (ctx.operator.type == ExprParser.PLUS) left.plus(right) else left.minus(right)
    }

    override fun visitAssignmentExpression(ctx: ExprParser.AssignmentExpressionContext?): VariantType {
        return ctx?.let { context ->
            val id: String? = context.ID()?.text
            id?.let { vn ->
                val value = visit(context.expression())
                variables[vn] = value
                value
            }
        } ?: StringType()
    }



}
