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
