package ru.tbank.education.school.lesson1

fun main() {
    val start = "amir"
    val mid = '-'
    val end = "kefir"

    val str = """
        My git username starts with $amir,
        has "$mid" in the middle
        and ends with $end
    """.trimIndent()
    println(str)

    var num = 0
    num += 1
    println(num)

    var n: Int? = null
    println(n)

    // theory

    val j = when (n) {
        !null -> 123
        is Int -> 321
    }

    // arrays
    val array = arrayOf(1, 2, 3)

    val doubleArray = arrayOf(
        arrayOf(1, 2 ,3)
        arrayOf(1, 2 ,3)
        arrayOf(1, 2 ,3)
    )
    // OR
    val doubleArray2 =
        Array(2) { Array(2) {0} } //[[0]*2]*2 in py
    //

    // for
    for (i in 2 .. 10 step 2) {
        println(i)
    }

    for (i in 6 downTo 0 step 2) {
        println(i)
    }

    for (elem in array) {
        println(elem)
    }
}
