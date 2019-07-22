package com.johnowl.rules

internal class VariablesParser {

    fun parse(vars: Map<String, Any>): Map<String, String> {

        val variables = mutableMapOf<String, String>()

        for (v in vars) {

            val value = v.value
            val key = v.key

            if (value is String) {
                addText(key, value, variables)
            }

            if(value is Int) {
                addNumber(key, value, variables)
            }

            if(value is List<*>) {
                addList(key, value as List<Any>, variables)
            }
        }

        return variables
    }

    private fun addText(name: String, value: String, variables: MutableMap<String, String>) {
        variables[name] = "$value"
    }

    private fun addNumber(name: String, value: Int, variables: MutableMap<String, String>) {
        variables[name] = "$value"
    }

    private fun addList(name: String, value: List<Any>, variables: MutableMap<String, String>) {
        variables[name] = value
            .mapNotNull { if (it is Int) "Number($it)" else "'$it'" }
            .joinToString(" ", "[", "]")

    }
}