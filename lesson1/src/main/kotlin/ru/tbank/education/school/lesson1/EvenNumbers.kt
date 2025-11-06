package ru.tbank.education.school.lesson1

fun sumEvenNumbers(numbers: Array<Int>): Int {
    var count = 0
    for (i in numbers) {
        if (i % 2 == 0) {
            count += i
        }
    }
    return count
}

fun sumEvenNumbers2(numbers: Array<Int>): Int =
    numbers.filter{ it % 2 == 0}.sum()

fun main() {
    var array = arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    println(sumEvenNumbers2(array))
}