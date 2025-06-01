package com.hotela.util

fun interface TimeProvider<T> {
    fun now(): T
}
