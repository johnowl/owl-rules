package com.johnowl.rules

import com.github.h0tk3y.betterParse.grammar.parseToEnd

object RulesEngine {

    fun check(expression: String, variables: Map<String, String>): Boolean {
        val evaluator = RulesEvaluator(variables).parseToEnd(expression)
        return evaluator.resolve()
    }

}