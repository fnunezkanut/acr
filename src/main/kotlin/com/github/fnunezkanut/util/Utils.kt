package com.github.fnunezkanut.util

import java.sql.ResultSet
import java.util.*

//kotlin extension function to mask passwords
fun String.masked(len: Int = 5): String {
    val sb = StringBuilder(this)
    (0 until length - len).forEach { sb.setCharAt(it, '*') }
    return sb.toString()
}

//nullsafe JDBC result set extension method
fun ResultSet.kString(column: String): String {
    return this.getString(column) ?: ""
}

//generates random string, useful in tests
fun ClosedRange<Char>.randomString(length: Int) =
    (1..length)
        .map { (Random().nextInt(endInclusive.toInt() - start.toInt()) + start.toInt()).toChar() }
        .joinToString("")
