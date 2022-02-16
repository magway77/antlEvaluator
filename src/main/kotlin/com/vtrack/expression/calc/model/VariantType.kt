package com.vtrack.expression.calc.model

abstract class VariantType(val type: Type) :
    Comparable<VariantType> {

    enum class Type {
        INT,
        REAL,
        BOOL,
        STRING,
        LIST
    }

    var value: Any? = null

    override fun compareTo(other: VariantType): Int {
        return value?.let {
            if (other.isNull()) {
                1
            } else {
                value.toString().compareTo(other.value.toString())
            }
        } ?: if (other.isNull()) 0 else -1
    }

    fun isNull(): Boolean = value == null

    @Suppress("MemberVisibilityCanBePrivate")
    fun asIntOrNull(): Long? {
        return value?.let {
            when (it) {
                is Number -> it.toLong()
                is Boolean -> if (it) 1 else 0
                is String -> it.toLongOrNull()
                else -> null
            }
        }
    }

    fun asInt(default: Long = 0L): Long = asIntOrNull() ?: default

    @Suppress("MemberVisibilityCanBePrivate")
    fun asDoubleOrNull(): Double? {
        return value.let {
            when (it) {
                is Number -> it.toDouble()
                is Boolean -> if (it) 1.0 else 0.0
                is String -> it.toDoubleOrNull()
                else -> null
            }
        }
    }

    fun asDouble(default: Double = 0.0): Double = asDoubleOrNull() ?: default

    @Suppress("MemberVisibilityCanBePrivate")
    fun asStringOrNull(): String? {
        return value?.toString()
    }

    fun asString(default: String = ""): String = asStringOrNull() ?: default

    @Suppress("MemberVisibilityCanBePrivate")
    fun asBooleanOrNull(): Boolean? {
        return value?.let {
            when (it) {
                is Number -> 0 != it
                is Boolean -> it
                is String -> it != "0"
                else -> false
            }
        }
    }

    fun asBoolean(default: Boolean = false) = asBooleanOrNull() ?: default

    fun asListOrNull(): List<Any?>? {
        return value?.let {
            when (it) {
                is List<*> -> it
                else -> listOf(it)
            }
        }
    }

    fun asList(): List<Any?> = asListOrNull() ?: listOf()

    @Throws(UnsupportedOperationException::class)
    open fun plus(rightOperand: VariantType): VariantType =
        throw java.lang.UnsupportedOperationException("plus operation for type: ${this.type} and ${rightOperand.type}")

    @Throws(UnsupportedOperationException::class)
    open fun minus(rightOperand: VariantType): VariantType =
        throw java.lang.UnsupportedOperationException("minus operation for type: ${this.type} and ${rightOperand.type}")

    @Throws(UnsupportedOperationException::class)
    open fun multiple(rightOperand: VariantType): VariantType =
        throw java.lang.UnsupportedOperationException("multiple operation for type: ${this.type} and ${rightOperand.type}")

    @Throws(UnsupportedOperationException::class)
    open fun division(rightOperand: VariantType): VariantType =
        throw java.lang.UnsupportedOperationException("division operation for type: ${this.type} and ${rightOperand.type}")
}

class IntType :
    VariantType(Type.INT) {

    override fun plus(rightOperand: VariantType): VariantType {
        if (!isNull() && !rightOperand.isNull()) {
            return when (rightOperand.type) {
                Type.INT, Type.BOOL -> IntType().also { it.value = asInt() + rightOperand.asInt() }
                Type.REAL -> RealType().also { it.value = asDouble() + rightOperand.asDouble() }
                Type.STRING -> StringType().also { it.value = asString() + rightOperand.asString() }
                else -> super.plus(rightOperand)
            }
        }
        return StringType()
    }

    override fun minus(rightOperand: VariantType): VariantType {
        if (!isNull() && !rightOperand.isNull()) {
            return when (rightOperand.type) {
                Type.INT, Type.BOOL, Type.STRING -> IntType().also { it.value = asInt() - rightOperand.asInt() }
                Type.REAL -> RealType().also { it.value = asDouble() - rightOperand.asDouble() }
                else -> super.minus(rightOperand)
            }
        }
        return StringType()
    }

    override fun multiple(rightOperand: VariantType): VariantType {
        if (!isNull() && !rightOperand.isNull()) {
            return when (rightOperand.type) {
                Type.INT, Type.BOOL -> IntType().also { it.value = asInt() * rightOperand.asInt() }
                Type.REAL -> RealType().also { it.value = asDouble() * rightOperand.asDouble() }
                Type.STRING -> StringType().also { it.value = rightOperand.asString().repeat(asInt().toInt()) }
                else -> super.multiple(rightOperand)
            }
        }
        return StringType()
    }

    override fun division(rightOperand: VariantType): VariantType {
        if (!isNull() && !rightOperand.isNull()) {
            return when (rightOperand.type) {
                Type.INT, Type.BOOL, Type.STRING -> IntType().also { it.value = asInt() / rightOperand.asInt() }
                Type.REAL -> RealType().also { it.value = asDouble() / rightOperand.asDouble() }
                else -> super.division(rightOperand)
            }
        }
        return StringType()
    }
}

class RealType :
    VariantType(Type.REAL) {

    override fun plus(rightOperand: VariantType): VariantType {
        if (!isNull() && !rightOperand.isNull()) {
            return when (rightOperand.type) {
                Type.INT, Type.REAL, Type.BOOL -> RealType().also {
                    it.value = asDouble() + rightOperand.asDouble()
                }
                Type.STRING -> BoolType().also { it.value = asString() + rightOperand.asString() }
                else -> super.plus(rightOperand)
            }
        }
        return StringType()
    }

    override fun minus(rightOperand: VariantType): VariantType {
        if (!isNull() && !rightOperand.isNull()) {
            when (type) {
                Type.INT, Type.REAL, Type.BOOL, Type.STRING -> RealType().also { asDouble() - rightOperand.asDouble() }
                else -> super.minus(rightOperand)
            }
        }
        return StringType()
    }

    override fun multiple(rightOperand: VariantType): VariantType {
        if (!isNull() && !rightOperand.isNull()) {
            return when (rightOperand.type) {
                Type.INT, Type.REAL, Type.BOOL, Type.STRING -> RealType().also {
                    it.value = asDouble() * rightOperand.asDouble()
                }
                else -> super.multiple(rightOperand)
            }
        }
        return StringType()
    }

    override fun division(rightOperand: VariantType): VariantType {
        if (!isNull() && !rightOperand.isNull()) {
            return when (rightOperand.type) {
                Type.INT, Type.REAL, Type.BOOL, Type.STRING -> RealType().also {
                    it.value = asDouble() / rightOperand.asDouble()
                }
                else -> super.division(rightOperand)
            }
        }
        return StringType()
    }

}

class BoolType :
    VariantType(Type.BOOL) {

    override fun plus(rightOperand: VariantType): VariantType {
        if (!isNull() && !rightOperand.isNull()) {
            return when (rightOperand.type) {
                Type.INT, Type.REAL, Type.BOOL -> BoolType().also {
                    it.value = asBoolean() || rightOperand.asBoolean()
                }
                Type.STRING -> StringType().also { it.value = asString() + rightOperand.asString() }
                else -> super.plus(rightOperand)
            }
        }
        return StringType()
    }

    override fun multiple(rightOperand: VariantType): VariantType {
        if (!isNull() && !rightOperand.isNull()) {
            return when (rightOperand.type) {
                Type.BOOL -> BoolType().also {
                    it.value = asBoolean() && rightOperand.asBoolean()
                }
                Type.INT -> IntType().also {
                    it.value = asInt() * rightOperand.asInt()
                }
                Type.REAL, Type.STRING -> RealType().also {
                    it.value = asDouble() * rightOperand.asDouble()
                }
                else -> super.multiple(rightOperand)
            }
        }
        return StringType()
    }

    override fun division(rightOperand: VariantType): VariantType {
        if (!isNull() && !rightOperand.isNull()) {
            return when (rightOperand.type) {
                Type.INT, Type.BOOL -> IntType().also {
                    it.value = asInt() / rightOperand.asInt()
                }
                Type.REAL, Type.STRING -> RealType().also {
                    it.value = asDouble() / rightOperand.asDouble()
                }
                else -> super.division(rightOperand)
            }
        }
        return StringType()
    }

}

class StringType :
    VariantType(Type.STRING) {
    override fun plus(rightOperand: VariantType): VariantType {
        if (!isNull() && !rightOperand.isNull()) {
            return StringType().also { it.value = asString() + rightOperand.asString() }
        }
        return StringType()
    }

    override fun minus(rightOperand: VariantType): VariantType {
        if (!isNull() && !rightOperand.isNull()) {
            return when (rightOperand.type) {
                Type.INT, Type.BOOL -> IntType().also {
                    it.value = asInt() - rightOperand.asInt()
                }
                Type.REAL, Type.STRING -> RealType().also {
                    it.value = asDouble() - rightOperand.asDouble()
                }
                else -> super.minus(rightOperand)
            }
        }
        return StringType()
    }

    override fun multiple(rightOperand: VariantType): VariantType {
        if (!isNull() && !rightOperand.isNull()) {
            return when (rightOperand.type) {
                Type.INT, Type.BOOL -> IntType().also {
                    it.value = asInt() * rightOperand.asInt()
                }
                Type.REAL -> IntType().also {
                    it.value = asDouble() * rightOperand.asDouble()
                }
                else -> super.multiple(rightOperand)
            }
        }
        return StringType()
    }

    override fun division(rightOperand: VariantType): VariantType {
        if (!isNull() && !rightOperand.isNull()) {
            return RealType().also {
                it.value = asDouble() / rightOperand.asDouble()
            }
        }
        return StringType()
    }
}


class ListType :
    VariantType(Type.LIST) {

}
