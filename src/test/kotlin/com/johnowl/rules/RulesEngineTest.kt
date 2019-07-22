package com.johnowl.rules

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

class RulesEngineTest {

    private val engine = RulesEngine()

    @Test
    fun `should return true for literal true whithout variables`() {
        assert(engine.check("true", emptyMap()))
    }

    @Test
    fun `should return false for literal false whithout variables`() {
        assertFalse(engine.check("false", emptyMap()))
    }

    @Test
    fun `should return true for text equals variable`() {
        assert(engine.check("name = 'johnowl'", mapOf("name" to "johnowl")))
    }

    @Test
    fun `should return true for text not equals variable`() {
        assert(engine.check("name != 'johnowl'", mapOf("name" to "mary")))
    }

    @Test
    fun `should return true for number equals variable`() {
        assert(engine.check("Number(number) = 9999", mapOf("number" to 9999)))
    }

    @Test
    fun `should return true for number not equals variable`() {
        assert(engine.check("Number(number) != 9999", mapOf("number" to 1)))
    }

    @Test
    fun `should return true for list that contains text without variables`() {
        assert(engine.check("[1 2 3 4] CONTAINS 1", emptyMap()))
    }

    @Test
    fun `should return true for list that contains variable`() {
        assert(engine.check("[1 2 3 4] CONTAINS Number(var1)",mapOf("var1" to 1)))
    }

    @Test
    fun `should return true for variable numeric list that contains variable`() {
        assert(engine.check("List(var0) CONTAINS Number(var1)",mapOf("var1" to 1, "var0" to listOf(1, 2, 3))))
    }

    @Test
    fun `should return true for variable text list that contains variable`() {
        assert(engine.check("List(var0) CONTAINS var1",mapOf("var1" to "beta", "var0" to listOf("alpha", "beta"))))
    }


    @Test
    fun `should return true for variable mixed list that contains text variable`() {
        assert(engine.check("List(var0) CONTAINS var1",mapOf("var1" to "beta", "var0" to listOf("alpha", "beta", 1, 2))))
    }

}