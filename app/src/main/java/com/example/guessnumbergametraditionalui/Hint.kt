package com.example.guessnumbergametraditionalui


fun isEven(number: Int): Boolean { return number % 2 == 0}

fun isDivisibleByTen(number: Int): Boolean { return number % 10 == 0}

fun lastDigit(number: Int): Int {
    val stringDigit = number.toString()
    return stringDigit[stringDigit.length - 1].digitToInt()
}
