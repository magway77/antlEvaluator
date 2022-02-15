import com.vtrack.expression.model.*
import com.vtrack.generated.antlr.ExprLexer
import com.vtrack.generated.antlr.ExprParser
import expression.EvalVisitor
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class EvalVisitorTest {

    fun calculateStatement(statement: String): VariantType {
        val lexer = ExprLexer(CharStreams.fromString(statement))
        val tokens = CommonTokenStream(lexer)
        val parser = ExprParser(tokens)
        return EvalVisitor().visit(parser.statement())
    }

    @Test
    internal fun test_test_assignment() {
        var result: VariantType = calculateStatement("b = true")
        Assertions.assertNotNull(result.value)
        Assertions.assertTrue(result is BoolType)
        Assertions.assertFalse(result.isNull())
        Assertions.assertTrue(result.value as Boolean)

        result = calculateStatement("b = 5")
        Assertions.assertTrue(result is IntType)
        Assertions.assertFalse(result.isNull())
        Assertions.assertEquals(5L, result.value)

        result = calculateStatement("b = 3.2")
        Assertions.assertTrue(result is RealType)
        Assertions.assertFalse(result.isNull())
        Assertions.assertEquals(3.2, result.value)

        result = calculateStatement("b = \"vasya\"")
        Assertions.assertTrue(result is StringType)
        Assertions.assertFalse(result.isNull())
        Assertions.assertEquals("vasya", result.value)
    }

    @Test
    internal fun test_test_addition() {
        var result: VariantType = calculateStatement("3 + 2")
        Assertions.assertTrue(result is IntType)
        Assertions.assertFalse(result.isNull())
        Assertions.assertEquals(5L, result.value)
    }

    @Test
    internal fun test_test_subtraction() {
        var result: VariantType = calculateStatement("3 - 2")
        Assertions.assertTrue(result is IntType)
        Assertions.assertFalse(result.isNull())
        Assertions.assertEquals(1L, result.value)
    }

    @Test
    internal fun test_division() {
        var result: VariantType = calculateStatement("9 / 2")
        Assertions.assertTrue(result is IntType)
        Assertions.assertFalse(result.isNull())
        Assertions.assertEquals(4L, result.value)
    }

    @Test
    internal fun test_testParens() {
        var result: VariantType = calculateStatement(" 2 + 3 * 2")
        Assertions.assertNotNull(result.value)
        Assertions.assertTrue(result is IntType)
        Assertions.assertFalse(result.isNull())
        Assertions.assertEquals(8L, result.value)

        result = calculateStatement(" (2 + 3) * 2")
        Assertions.assertNotNull(result.value)
        Assertions.assertTrue(result is IntType)
        Assertions.assertFalse(result.isNull())
        Assertions.assertEquals(10L, result.value)

    }

    /*


    @Test
    internal fun test_test_expr() {
        var mapExpr = listOf<String>(
            "a = 5",
            "b = 6",
            "(a+b)*2"
        )
        val expression = mapExpr.joinToString(separator = "\n")
        val lexer = ExprLexer(CharStreams.fromString(expression))
        val tokens = CommonTokenStream(lexer)
        val parser = ExprParser(tokens)
        val tree = parser.prog()
        val visitor = EvalVisitor()
        val visit = visitor.visit(tree)
        Assertions.assertEquals(22, visit)
    }
*/

}
