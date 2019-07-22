package com.johnowl.rules

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class VariablesParserTest {

    private val parser = VariablesParser()

    @Test
    fun `should add Int as Number`() {
        val variables = parser.parse(mapOf("number" to 100))

        assertEquals("100", variables["number"])
    }

    @Test
    fun `should add String as Text`() {
        val variables = parser.parse(mapOf("name" to "johnowl"))

        assertEquals("johnowl", variables["name"])
    }

    @Test
    fun `should add list itens as Text`() {
        val variables = parser.parse(mapOf("roles" to listOf("beta", "alpha")))

        assertEquals("['beta' 'alpha']", variables["roles"])
    }

    @Test
    fun `should add list itens as Numbers`() {
        val variables = parser.parse(mapOf("numbers" to listOf(20, 50, 30)))

        assertEquals("[Number(20) Number(50) Number(30)]", variables["numbers"])
    }

    @Test
    fun `should add list itens as Numbers and Text`() {
        val variables = parser.parse(mapOf("things" to listOf(20, 50, 30, "text1", 200, "text2")))

        assertEquals("[Number(20) Number(50) Number(30) 'text1' Number(200) 'text2']", variables["things"])
    }

}