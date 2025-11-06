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
}
