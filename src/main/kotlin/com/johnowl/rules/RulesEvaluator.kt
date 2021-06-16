package com.johnowl.rules

import com.github.h0tk3y.betterParse.combinators.*
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.grammar.parser
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken
import com.github.h0tk3y.betterParse.parser.Parser

internal class RulesEvaluator() : Grammar<BooleanExpression>() {

    private var values = mapOf<String, String>()

    constructor(values: Map<String, String>) : this() {
        this.values = values
    }

    private val TRU by literalToken("true")
    private val FALS by literalToken("false")
    private val NUMBER by regexToken("\\d+")
    private val LPAR by literalToken("(")
    private val RPAR by literalToken(")")
    private val LSQR by literalToken("[")
    private val RSQR by literalToken("]")
    private val NEQ by literalToken("!=")
    private val NOT by literalToken("NOT")
    private val AND by literalToken("AND")
    private val OR by literalToken("OR")
    private val GTE by literalToken(">=")
    private val GT by literalToken(">")
    private val LTE by literalToken("<=")
    private val LT by literalToken("<")
    private val EQ by literalToken("=")
    private val CONTAINS by literalToken("CONTAINS")
    private val NOT_CONTAINS by literalToken("NOT CONTAINS")

    private val TEXT by regexToken("'[^']+'")
    private val CURRENT_TIME by literalToken("CurrentTime()")
    private val VAR_TO_NUMBER by regexToken("Number\\([a-zA-Z]\\w+\\)")
    private val VAR_TO_LIST by regexToken("List\\([a-zA-Z]\\w+\\)")
    private val VAR by regexToken("[a-zA-Z][a-zA-Z0-9]+")

    private val WHITESPACE by regexToken("\\s+", ignore = true)

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