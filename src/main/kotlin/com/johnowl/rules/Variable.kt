package com.johnowl.rules

import java.security.InvalidParameterException

internal class Variable(val name: String, val values: Map<String, String>) {
    fun getValue(): String {

        var key = name
        if (name.startsWith("Number(")) {
            key = name.removePrefix("Number(").removeSuffix(")").trim()
        }

        if (name.startsWith("List(")) {
            key = name.removePrefix("List(").removeSuffix(")").trim()
        }

        return values[key] ?: throw InvalidParameterException("Variable $name undefined")
    }
}