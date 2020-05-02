package com.johnowl.rules

import com.github.h0tk3y.betterParse.combinators.*
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.grammar.parser
import com.github.h0tk3y.betterParse.parser.Parser

internal class RulesEvaluator() : Grammar<BooleanExpression>() {

    private var values = mapOf<String, String>()

    constructor(values: Map<String, String>) : this() {
        this.values = values
    }

    private val TRU by token("true")
    private val FALS by token("false")
    private val NUMBER by token("\\d+")
    private val LPAR by token("\\(")
    private val RPAR by token("\\)")
    private val LSQR by token("\\[")
    private val RSQR by token("\\]")
    private val NEQ by token("!=")
    private val NOT by token("NOT")
    private val AND by token("AND")
    private val OR by token("OR")
    private val GTE by token(">=")
    private val GT by token(">")
    private val LTE by token("<=")
    private val LT by token("<")
    private val EQ by token("=")
    private val CONTAINS by token("CONTAINS")
    private val NOT_CONTAINS by token("NOT CONTAINS")

    private val TEXT by token("'\\w+'")
    private val CURRENT_TIME by token("CurrentTime\\(\\)")
    private val VAR_TO_NUMBER by token("Number\\([a-zA-Z]\\w+\\)")
    private val VAR_TO_LIST by token("List\\([a-zA-Z]\\w+\\)")
    private val VAR by token("[a-zA-Z][a-zA-Z0-9]+")

    private val WHITESPACE by token("\\s+", ignore = true)

    private val NEGATION by -NOT * parser(this::term) map { Not(it) }
    private val BRACED_EXPRESION by -LPAR * parser(this::rootParser) * -RPAR
    private val LIST by -LSQR * separatedTerms(parser(this::term), WHITESPACE, acceptZero = true) * -RSQR

    private val term: Parser<BooleanExpression> by
        (TRU asJust TRUE) or
        (FALS asJust FALSE) or
        (TEXT map { Text(it.text.trim('\'')) }) or
        (NUMBER map { Number(it.text.toInt()) }) or
        (VAR map { Text(Variable(it.text, values)) }) or
        (CURRENT_TIME map { CurrentTime().toNumber() }) or
        (VAR_TO_NUMBER map { Number(Variable(it.text, values)) }) or
        (VAR_TO_LIST map { BooleanExpressionList(Variable(it.text, values)) }) or
        NEGATION or
        BRACED_EXPRESION or
        (LIST map { BooleanExpressionList(it) })

    private val andChain by leftAssociative(term, AND) { a, _, b -> And(a, b) }
    private val orChain by leftAssociative(andChain, OR) { a, _, b -> Or(a, b) }
    private val gtChain by leftAssociative(orChain, GT) { a, _, b -> GreaterThan(a, b) }
    private val gteChain by leftAssociative(gtChain, GTE) { a, _, b -> GreaterThanOrEquals(a, b) }
    private val ltChain by leftAssociative(gteChain, LT) { a, _, b -> LessThan(a, b) }
    private val lteChain by leftAssociative(ltChain, LTE) { a, _, b -> LessThanOrEquals(a, b) }
    private val eqChain by leftAssociative(lteChain, EQ) { a, _, b -> Equals(a, b) }
    private val neqChain by leftAssociative(eqChain, NEQ) { a, _, b -> NotEquals(a, b) }
    private val containsChain by leftAssociative(neqChain, CONTAINS) { a, _, b -> Contains(a, b) }
    private val notContainsChain by leftAssociative(containsChain, NOT_CONTAINS) { a, _, b -> NotContains(a, b) }

    override val rootParser by notContainsChain
}