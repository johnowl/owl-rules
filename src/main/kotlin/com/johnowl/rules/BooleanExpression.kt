package com.johnowl.rules

import java.security.InvalidParameterException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

internal sealed class BooleanExpression {
    abstract fun resolve(): Boolean
}

internal object TRUE : BooleanExpression() {
    override fun resolve() = true
}

internal object FALSE : BooleanExpression() {
    override fun resolve() = false
}

internal open class Number(val value: Int) : Value<Int>, BooleanExpression() {

    constructor(variable: Variable) : this(variable.getValue().toInt())

    override fun resolve() = value == 1
    override fun get() = value
}

internal class Text(val value: String) : Value<String>, BooleanExpression() {

    constructor(variable: Variable) : this(variable.getValue())

    override fun resolve() = value == "true"
    override fun get() = value
}

internal class CurrentTime {
    fun toNumber(): Number {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HHmmss")
        return Number(current.format(formatter).toInt())
    }
}

internal class Version(private val value: String) {

    init {
        if (!value.matches(Regex("""^Version\([0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\)$"""))) {
            throw InvalidParameterException("Version should be in the format [0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}")
        }
    }

    fun toNumber(): Number {
        val formattedValue = value
            .removePrefix("Version(")
            .removeSuffix(")")
            .split(".")
            .joinToString(separator = "", prefix = "", postfix = "") { "000$it".takeLast(3) }

        return Number(formattedValue.toInt())
    }
}

internal class Not(val body: BooleanExpression) : BooleanExpression() {
    override fun resolve() = !body.resolve()
}

internal class And(val left: BooleanExpression, val right: BooleanExpression) : BooleanExpression() {
    override fun resolve() = left.resolve() && right.resolve()
}

internal class Or(val left: BooleanExpression, val right: BooleanExpression) : BooleanExpression() {
    override fun resolve() = left.resolve() || right.resolve()
}

internal class GreaterThan(val left: BooleanExpression, val right: BooleanExpression) : BooleanExpression() {
    override fun resolve(): Boolean {

        if (left is Number && right is Number)
            return left.value > right.value

        if (left is Text && right is Text)
            return left.value > right.value

        throw InvalidParameterException("BooleanExpression must be Text or Number")
    }
}

internal class GreaterThanOrEquals(val left: BooleanExpression, val right: BooleanExpression) : BooleanExpression() {
    override fun resolve(): Boolean {

        if (left is Number && right is Number)
            return left.value >= right.value

        if (left is Text && right is Text)
            return left.value >= right.value

        throw InvalidParameterException("BooleanExpression must be Text or Number")
    }
}

internal class LessThan(val left: BooleanExpression, val right: BooleanExpression) : BooleanExpression() {
    override fun resolve(): Boolean {

        if (left is Number && right is Number)
            return left.value < right.value

        if (left is Text && right is Text)
            return left.value < right.value

        throw InvalidParameterException("BooleanExpression must be Text or Number")
    }
}

internal class LessThanOrEquals(val left: BooleanExpression, val right: BooleanExpression) : BooleanExpression() {
    override fun resolve(): Boolean {

        if (left is Number && right is Number)
            return left.value <= right.value

        if (left is Text && right is Text)
            return left.value <= right.value

        throw InvalidParameterException("BooleanExpression must be Text or Number")
    }
}

internal class Equals(val left: BooleanExpression, val right: BooleanExpression) : BooleanExpression() {
    override fun resolve(): Boolean {

        if (left is Number && right is Number)
            return left.value == right.value

        if (left is Text && right is Text)
            return left.value == right.value

        throw InvalidParameterException("BooleanExpression must be Text or Number")
    }
}

internal class NotEquals(val left: BooleanExpression, val right: BooleanExpression) : BooleanExpression() {
    override fun resolve(): Boolean {

        if (left is Number && right is Number)
            return left.value != right.value

        if (left is Text && right is Text)
            return left.value != right.value

        throw InvalidParameterException("BooleanExpression must be Text or Number")
    }
}

internal class BooleanExpressionList(var list: List<BooleanExpression>) : BooleanExpression() {

    constructor(variable: Variable) : this(emptyList()) {
        this.list = createListFromString(variable.getValue())
    }

    private fun createListFromString(str: String): List<BooleanExpression> {

        var list = arrayListOf<BooleanExpression>()

        for (item in str.removePrefix("[").removeSuffix("]").split(" ")) {
            if (item.startsWith("Number(")) {
                list.add(Number(item.removePrefix("Number(").removeSuffix(")").toInt()))
            }

            if (item.startsWith("'")) {
                list.add(Text(item.removePrefix("'").removeSuffix("'")))
            }
        }

        return list
    }

    override fun resolve() = false
    fun get() = list
}

internal open class Contains(val left: BooleanExpression, val right: BooleanExpression) : BooleanExpression() {
    override fun resolve(): Boolean {

        if (!(right is Number || right is Text))
            throw InvalidParameterException("The left side must be Text or Number")

        if (left !is BooleanExpressionList)
            throw InvalidParameterException("The right side must be BooleanExpressionList")

        for (item in left.list) {
            if (item is Number) {
                if (item.value == (right as Number).value) {
                    return true
                }
            }

            if (item is Text) {
                if (item.value == (right as Text).value) {
                    return true
                }
            }
        }

        return false
    }
}

internal class NotContains(left: BooleanExpression, right: BooleanExpression) : Contains(left, right) {
    override fun resolve() = !super.resolve()
}