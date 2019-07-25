package com.johnowl.rules

import com.github.h0tk3y.betterParse.grammar.parseToEnd

class RulesEngine {

    private val parser = VariablesParser()

    fun check(expression: String, variables: Map<String, Any>): Boolean {
        val vars = parser.parse(variables)
        val evaluator = RulesEvaluator(vars).parseToEnd(expression)
        return evaluator.resolve()
    }
}