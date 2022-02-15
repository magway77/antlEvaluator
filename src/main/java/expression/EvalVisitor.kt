package expression

import com.vtrack.expression.model.*
import com.vtrack.generated.antlr.ExprBaseVisitor
import com.vtrack.generated.antlr.ExprParser
import org.antlr.v4.runtime.tree.ErrorNode
import org.antlr.v4.runtime.tree.TerminalNode
import kotlin.reflect.full.isSubclassOf

class EvalVisitor :
    ExprBaseVisitor<VariantType>() {

    var variables: HashMap<String, VariantType> = HashMap()

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
        return visit(ctx!!.expression())
    }

    override fun visitIntValue(ctx: ExprParser.IntValueContext?): VariantType {
        return ctx!!.let {
            IntType().apply { value = ctx.INT().text.toLongOrNull() }
        }
    }

    override fun visitDoubleValue(ctx: ExprParser.DoubleValueContext?): VariantType {
        return ctx!!.let {
            RealType().apply { value = ctx.DOUBLE().text.toDoubleOrNull() }
        }
    }

    override fun visitStringValue(ctx: ExprParser.StringValueContext?): VariantType {
        return ctx!!.let {
            StringType().apply { value = ctx.STRING().text.replace("\"(.*)\"".toRegex(), "$1") }
        }
    }

    override fun visitBoolValue(ctx: ExprParser.BoolValueContext?): VariantType {
        return BoolType().apply { value = (ctx?.TRUE() != null) }
    }

    override fun visitParens(ctx: ExprParser.ParensContext?): VariantType {
        return visit(ctx!!.expression())
    }

    override fun visitMultiply(ctx: ExprParser.MultiplyContext?): VariantType {
        return ctx!!.let {
            visit(ctx.left).multiple(visit(ctx.right))
        }
    }

    override fun visitDivision(ctx: ExprParser.DivisionContext?): VariantType {
        return ctx!!.let {
            visit(ctx.left).division(visit(ctx.right))
        }
    }

    override fun visitAddition(ctx: ExprParser.AdditionContext?): VariantType {
        return ctx!!.let {
            visit(ctx.left).plus(visit(ctx.right))
        }
    }

    override fun visitSubstruction(ctx: ExprParser.SubstructionContext?): VariantType {
        return ctx!!.let {
            visit(ctx.left).minus(visit(ctx.right))
        }
    }

    override fun visitAssignment(ctx: ExprParser.AssignmentContext?): VariantType {
        return ctx!!.let {
            val id: String? = ctx.assignmentExpression().ID()?.text
            id!!.let { vn ->
                val value = visit(ctx.assignmentExpression().expression())
                variables[vn] = value
                value
            }
        }
    }

    override fun visitListCount(ctx: ExprParser.ListCountContext?): VariantType {
        return IntType().also { intType ->
            val list = variables.getOrPut(ctx!!.variable.text) { ListType() }
            if (!list.isNull()) {
                intType.value = (list.value as? List<*>)?.size
            }
        }
    }

    override fun visitListFilterByType(ctx: ExprParser.ListFilterByTypeContext?): VariantType {
        return ListType().also { listType ->
            val list = variables.getOrPut(ctx!!.variable.text) { ListType() }.value
            list?.let {
                if (list is List<*>) {
                    val typeName = ctx.typeName.text
                    val kClass = Class.forName(typeName).kotlin

                    listType.value = list.filter { item ->
                        item != null && kClass.isSubclassOf(item::class)
                    }
                }
            }
        }
    }
}
