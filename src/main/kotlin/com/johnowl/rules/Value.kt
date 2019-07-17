package com.johnowl.rules

internal interface Value<T> {
    fun get(): T
}