package com.johnowl.rules

import com.fasterxml.jackson.annotation.JsonProperty

data class TestCase(
    val name: String,
    val rule: String,
    val vars: String,
    @field:JsonProperty("expected_result")
    val expectedResult: String
)

class TestCaseList : ArrayList<TestCase>()

class TestCaseVariables : HashMap<String, Any>()