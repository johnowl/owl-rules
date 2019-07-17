package com.johnowl.rules

import com.github.h0tk3y.betterParse.grammar.parseToEnd
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.security.InvalidParameterException

class RulesEvaluatorTest {

    @Test
    fun `should return true for literal true`() {
        val expr = "true"
        val evaluator = RulesEvaluator().parseToEnd(expr)
        assert(evaluator.resolve())
    }

    @Test
    fun `should return false for literal false`() {
        val expr = "false"
        val evaluator = RulesEvaluator().parseToEnd(expr)
        assertFalse(evaluator.resolve())
    }

    @Test
    fun `should return true when compare 2 eq 2`() {
        val expr = "2 = 2"
        val evaluator = RulesEvaluator().parseToEnd(expr)
        assert(evaluator.resolve())
    }

    @Test
    fun `should return false when compare 2 eq 3`() {
        val expr = "2 = 3"
        val evaluator = RulesEvaluator().parseToEnd(expr)
        assertFalse(evaluator.resolve())
    }

    @Test
    fun `should return true when compare 2 not eq 3`() {
        val expr = "2 != 3"
        val evaluator = RulesEvaluator().parseToEnd(expr)
        assert(evaluator.resolve())
    }

    @Test
    fun `should return true when compare 2 lt 3`() {
        val expr = "2 < 3"
        val evaluator = RulesEvaluator().parseToEnd(expr)
        assert(evaluator.resolve())
    }

    @Test
    fun `should return false when compare 2 lt 1`() {
        val expr = "2 < 1"
        val evaluator = RulesEvaluator().parseToEnd(expr)
        assertFalse(evaluator.resolve())
    }

    @Test
    fun `should return false when compare 2 lt 2`() {
        val expr = "2 < 2"
        val evaluator = RulesEvaluator().parseToEnd(expr)
        assertFalse(evaluator.resolve())
    }

    @Test
    fun `should return true when compare 2 lte 2`() {
        val expr = "2 <= 2"
        val evaluator = RulesEvaluator().parseToEnd(expr)
        assert(evaluator.resolve())
    }

    @Test
    fun `should return true when compare 2 lte 3`() {
        val expr = "2 <= 3"
        val evaluator = RulesEvaluator().parseToEnd(expr)
        assert(evaluator.resolve())
    }

    @Test
    fun `should return false when compare 2 lte 1`() {
        val expr = "2 <= 1"
        val evaluator = RulesEvaluator().parseToEnd(expr)
        assertFalse(evaluator.resolve())
    }

    @Test
    fun `should return false when compare 20 gt 30`() {
        val expr = "20 > 30"
        val evaluator = RulesEvaluator().parseToEnd(expr)
        assertFalse(evaluator.resolve())
    }

    @Test
    fun `should return true when compare 32 gt 30`() {
        val expr = "32 > 30"
        val evaluator = RulesEvaluator().parseToEnd(expr)
        assert(evaluator.resolve())
    }

    @Test
    fun `should return false when compare 30 gt 30`() {
        val expr = "30 > 30"
        val evaluator = RulesEvaluator().parseToEnd(expr)
        assertFalse(evaluator.resolve())
    }

    @Test
    fun `should return true when compare 30 gte 30`() {
        val expr = "30 >= 30"
        val evaluator = RulesEvaluator().parseToEnd(expr)
        assert(evaluator.resolve())
    }

    @Test
    fun `should return false when compare 29 gte 30`() {
        val expr = "29 >= 30"
        val evaluator = RulesEvaluator().parseToEnd(expr)
        assertFalse(evaluator.resolve())
    }

    @Test
    fun `should return true when compare 290 gte 30`() {
        val expr = "290 >= 30"
        val evaluator = RulesEvaluator().parseToEnd(expr)
        assert(evaluator.resolve())
    }

    @Test
    fun `should return true when compare 'john' eq 'john'`() {
        val expr = "'john' = 'john'"
        val evaluator = RulesEvaluator().parseToEnd(expr)
        assert(evaluator.resolve())
    }

    @Test
    fun `should return false when compare 'john' eq 'owl'`() {
        val expr = "'john' = 'owl'"
        val evaluator = RulesEvaluator().parseToEnd(expr)
        assertFalse(evaluator.resolve())
    }

    @Test
    fun `should return true when compare 'john' not eq 'owl'`() {
        val expr = "'john' != 'owl'"
        val evaluator = RulesEvaluator().parseToEnd(expr)
        assert(evaluator.resolve())
    }

    @Test
    fun `should return false when negate literal true`() {
        val expr = "NOT true"
        val evaluator = RulesEvaluator().parseToEnd(expr)
        assertFalse(evaluator.resolve())
    }

    @Test
    fun `should return false when negate literal true with parentheses`() {
        val expr = "NOT true"
        val evaluator = RulesEvaluator().parseToEnd(expr)
        assertFalse(evaluator.resolve())
    }

    @Test
    fun `should return true when negate literal false`() {
        val expr = "NOT false"
        val evaluator = RulesEvaluator().parseToEnd(expr)
        assert(evaluator.resolve())
    }

    @Test
    fun `should return true when negate literal false with parentheses`() {
        val expr = "NOT (false)"
        val evaluator = RulesEvaluator().parseToEnd(expr)
        assert(evaluator.resolve())
    }

    @Test
    fun `should return true when compare 1 eq 1 with parentheses`() {
        val expr = "(1 = 1)"
        val evaluator = RulesEvaluator().parseToEnd(expr)
        assert(evaluator.resolve())
    }

    @Test
    fun `should return false when compare 1 eq 2 with parentheses`() {
        val expr = "(1 = 2)"
        val evaluator = RulesEvaluator().parseToEnd(expr)
        assertFalse(evaluator.resolve())
    }

    @Test
    fun `should return true when compare 1 lt 2 with parentheses`() {
        val expr = "(1 < 2)"
        val evaluator = RulesEvaluator().parseToEnd(expr)
        assert(evaluator.resolve())
    }

    @Test
    fun `should return true when literal true AND literal true`() {
        val expr = "true AND true"
        val evaluator = RulesEvaluator().parseToEnd(expr)
        assert(evaluator.resolve())
    }

    @Test
    fun `should return false when literal true AND literal false`() {
        val expr = "true AND false"
        val evaluator = RulesEvaluator().parseToEnd(expr)
        assertFalse(evaluator.resolve())
    }

    @Test
    fun `should return false when literal false AND literal true`() {
        val expr = "true AND false"
        val evaluator = RulesEvaluator().parseToEnd(expr)
        assertFalse(evaluator.resolve())
    }

    @Test
    fun `should return false when literal false AND literal false`() {
        val expr = "false AND false"
        val evaluator = RulesEvaluator().parseToEnd(expr)
        assertFalse(evaluator.resolve())
    }

    @Test
    fun `should return true when literal true OR literal true`() {
        val expr = "true OR true"
        val evaluator = RulesEvaluator().parseToEnd(expr)
        assert(evaluator.resolve())
    }

    @Test
    fun `should return true when literal true OR literal false`() {
        val expr = "true OR false"
        val evaluator = RulesEvaluator().parseToEnd(expr)
        assert(evaluator.resolve())
    }

    @Test
    fun `should return true when literal false OR literal true`() {
        val expr = "false OR true"
        val evaluator = RulesEvaluator().parseToEnd(expr)
        assert(evaluator.resolve())
    }

    @Test
    fun `should return true when true expression AND true expression`() {
        val expr = "(1 = 1) AND (2 = 2)"
        val evaluator = RulesEvaluator().parseToEnd(expr)
        assert(evaluator.resolve())
    }

    @Test
    fun `should return false when true expression AND false expression`() {
        val expr = "(1 = 1) AND (2 > 20)"
        val evaluator = RulesEvaluator().parseToEnd(expr)
        assertFalse(evaluator.resolve())
    }

    @Test
    fun `should return true when true expression OR true expression`() {
        val expr = "(1 = 1) OR (2 < 20)"
        val evaluator = RulesEvaluator().parseToEnd(expr)
        assert(evaluator.resolve())
    }

    @Test
    fun `should return true when true expression OR false expression`() {
        val expr = "(1 = 1) OR (2 = 20)"
        val evaluator = RulesEvaluator().parseToEnd(expr)
        assert(evaluator.resolve())
    }

    @Test
    fun `should return true when false expression OR true expression`() {
        val expr = "(1 != 1) OR (2 < 20)"
        val evaluator = RulesEvaluator().parseToEnd(expr)
        assert(evaluator.resolve())
    }

    @Test
    fun `should return false when NEGATE false expression OR true expression`() {
        val expr = "NOT ((1 != 1) OR (2 < 20))"
        val evaluator = RulesEvaluator().parseToEnd(expr)
        assertFalse(evaluator.resolve())
    }

    @Test
    fun `should return true when NEGATE false expression OR false expression`() {
        val expr = "NOT ((1 != 1) OR (2 > 20))"
        val evaluator = RulesEvaluator().parseToEnd(expr)
        assert(evaluator.resolve())
    }

    @Test
    fun `should return true when text variable equals value`() {
        val expr = "name = 'johnowl'"
        val variables = mapOf("name" to "johnowl")
        val evaluator = RulesEvaluator(variables).parseToEnd(expr)
        assert(evaluator.resolve())
    }

    @Test
    fun `should return false when text variable not equals value`() {
        val expr = "name = 'johnowl'"
        val variables = mapOf("name" to "owl")
        val evaluator = RulesEvaluator(variables).parseToEnd(expr)
        assertFalse(evaluator.resolve())
    }

    @Test
    fun `should return true when numeric variable equals value`() {
        val expr = "Number(version) = 10"
        val variables = mapOf("version" to "10")
        val evaluator = RulesEvaluator(variables).parseToEnd(expr)
        assert(evaluator.resolve())
    }

    @Test
    fun `should return true when numeric variable not equals value`() {
        val expr = "Number(version) != 10"
        val variables = mapOf("version" to "1")
        val evaluator = RulesEvaluator(variables).parseToEnd(expr)
        assert(evaluator.resolve())
    }

    @Test
    fun `should return true when numeric variable gt value`() {
        val expr = "Number(version) > 10"
        val variables = mapOf("version" to "20")
        val evaluator = RulesEvaluator(variables).parseToEnd(expr)
        assert(evaluator.resolve())
    }

    @Test
    fun `should return true when numeric variable gte value`() {
        val expr = "Number(version) >= 10"
        val variables = mapOf("version" to "10")
        val evaluator = RulesEvaluator(variables).parseToEnd(expr)
        assert(evaluator.resolve())
    }

    @Test
    fun `should return true when numeric variable lt value`() {
        val expr = "Number(version) < 10"
        val variables = mapOf("version" to "2")
        val evaluator = RulesEvaluator(variables).parseToEnd(expr)
        assert(evaluator.resolve())
    }

    @Test
    fun `should return true when numeric variable lte value`() {
        val expr = "Number(version) <= 1"
        val variables = mapOf("version" to "1")
        val evaluator = RulesEvaluator(variables).parseToEnd(expr)
        assert(evaluator.resolve())
    }

    @Test
    fun `should resolve complex rules`() {

        val rules = listOf("(NOT (platform = 'ios')) AND (Number(customer) = 99)",
            "((platform = 'android') OR (platform = 'ios')) AND (Number(version) > 0)",
            "((Number(customer) > 10) AND (Number(customer) < 100)) AND (platform = 'android')")


        val variables = mapOf("version" to "1", "platform" to "android", "customer" to "99")
        val evaluator = RulesEvaluator(variables)

        for(rule in rules) {
            assert(evaluator.parseToEnd(rule).resolve())
        }
    }

    @Test
    fun `should throw exception when try to convert not numeric value`() {
        val expr = "Number(version) < 10"
        val variables = mapOf("version" to "aaa")

        assertThrows<NumberFormatException> {
            RulesEvaluator(variables).parseToEnd(expr)
        }
    }

    @Test
    fun `should throw exception when try to convert not integer value`() {
        val expr = "Number(version) < 10"
        val variables = mapOf("version" to "10.0")

        assertThrows<NumberFormatException> {
            RulesEvaluator(variables).parseToEnd(expr)
        }
    }

    @Test
    fun `should throw exception when invalid syntax`() {
        val invalidRules = listOf("NOT 1 = 1",
            "1 = 1 AND 1 = 2",
            "1 = 1 AND (1 = 2 OR 1 = 1)",
            "(1 = 1) OR 1 = 2")

        for(invalidRule in invalidRules) {
            assertThrows<InvalidParameterException> {
                RulesEvaluator().parseToEnd(invalidRule).resolve()
            }
        }
    }


    @Test
    fun `should return true when list contains numeric value`() {
        val expr = "[1 2 3 4] CONTAINS 2"
        val evaluator = RulesEvaluator().parseToEnd(expr)
        assert(evaluator.resolve())
    }

    @Test
    fun `should return false when list does not contain numeric value`() {
        val expr = "[1 2 3 4] CONTAINS 5"
        val evaluator = RulesEvaluator().parseToEnd(expr)
        assertFalse(evaluator.resolve())
    }

    @Test
    fun `should return true when list contains text value`() {
        val expr = "['beta' 'alpha' 'has_reward_program'] CONTAINS 'alpha'"
        val evaluator = RulesEvaluator().parseToEnd(expr)
        assert(evaluator.resolve())
    }

    @Test
    fun `should return false when list does not contain text value`() {
        val expr = "['beta' 'alpha' 'has_reward_program'] CONTAINS 'canary'"
        val evaluator = RulesEvaluator().parseToEnd(expr)
        assertFalse(evaluator.resolve())
    }

    @Test
    fun `should return false when mixed list with text and variables does not contain text value`() {
        val expr = "['beta' 'alpha' 'has_reward_program' var1] CONTAINS 'canary'"

        val variables = mapOf("var1" to "blue")

        val evaluator = RulesEvaluator(variables).parseToEnd(expr)
        assertFalse(evaluator.resolve())
    }

    @Test
    fun `should return true when mixed list with text and variables contains text value`() {
        val expr = "['beta' 'alpha' 'has_reward_program' var1] CONTAINS 'blue'"

        val variables = mapOf("var1" to "blue")

        val evaluator = RulesEvaluator(variables).parseToEnd(expr)
        assert(evaluator.resolve())
    }

    @Test
    fun `should resolve complex rules with lists`() {

        val rules = listOf("(NOT (platform = 'ios')) AND (Number(customer) = 99) AND ([1 2 3] CONTAINS 1)",
            "(NOT (['blue' 'green' 'canary'] CONTAINS 'yellow') AND ((platform = 'android') OR (platform = 'ios')) AND (Number(version) > 0))",
            "((Number(customer) > 10) AND (Number(customer) < 100)) AND (List(platformList) CONTAINS 'android')")


        val variables = mapOf("version" to "1", "platform" to "android", "customer" to "99", "platformList" to "['android' 'ios']")
        val evaluator = RulesEvaluator(variables)

        for(rule in rules) {
            assert(evaluator.parseToEnd(rule).resolve())
        }
    }
}