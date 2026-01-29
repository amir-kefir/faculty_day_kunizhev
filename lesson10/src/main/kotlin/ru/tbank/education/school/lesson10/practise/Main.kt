import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter

fun main() {
    task1()
    println()
    task2()
    println()
    task3()
    println()
    task4()
    println()
    task5()
    println()
    task6()
    println()
    task7()
    println()
    task8()
}

/*
1) Строки + регулярные выражения
["Name: Ivan, score=17", ...]
Извлечь имя и score, собрать пары, вывести победителя.
*/
fun task1() {
    val lines = listOf(
        "Name: Ivan, score=17",
        "Name: Olga, score=23",
        "Name: Max, score=5"
    )

    val re = Regex("""^Name:\s*([A-Za-z]+)\s*,\s*score=(\d+)\s*$""")

    val pairs: List<Pair<String, Int>> = lines.mapNotNull { s ->
        val m = re.find(s) ?: return@mapNotNull null
        val name = m.groupValues[1]
        val score = m.groupValues[2].toInt()
        name to score
    }

    println("Task 1 pairs: $pairs")

    val best = pairs.maxByOrNull { it.second }
    if (best != null) {
        println("Task 1 best: ${best.first} (${best.second})")
    } else {
        println("Task 1: no valid lines")
    }
}

/*
2) Даты + коллекции
["2026-01-22", ...]
Преобразовать в даты, отсортировать, посчитать сколько в январе 2026.
*/
fun task2() {
    val dateStrings = listOf(
        "2026-01-22",
        "2026-02-01",
        "2025-12-31",
        "2026-01-05"
    )

    val fmt = DateTimeFormatter.ISO_LOCAL_DATE

    val dates = dateStrings.map { LocalDate.parse(it, fmt) }.sorted()

    println("Task 2 sorted dates: ${dates.joinToString { it.format(fmt) }}")

    val countJan2026 = dates.count { it.year == 2026 && it.month == Month.JANUARY }
    println("Task 2 count in Jan 2026: $countJan2026")
}

/*
3) Коллекции + строки
"apple orange apple banana orange apple"
Частоты слов, вывести слова с частотой > 1 по алфавиту.
*/
fun task3() {
    val text = "apple orange apple banana orange apple"

    val words = text.trim().split(Regex("""\s+""")).filter { it.isNotEmpty() }

    val freq = mutableMapOf<String, Int>()
    for (w in words) {
        freq[w] = (freq[w] ?: 0) + 1
    }

    println("Task 3 freq: $freq")

    val repeated = freq
        .filter { (_, c) -> c > 1 }
        .keys
        .sorted()

    println("Task 3 repeated words: ${repeated.joinToString(", ")}")
}

fun task4() {

    val data = listOf("A-123", "B-7", "AA-12", "C-001", "D-99x")
    val result = mutableListOf<String>()

    val regex = Regex("[A-Z]-[0-9]{1,3}")

    for (s in data) {
        if (regex.containsMatchIn(s)) {
            result.add(s)
        }
    }

    println(result)
}

fun task5() {
    val data = listOf(" Hello world ", "A B C", " one")
    val result = mutableListOf<String>()

    for (i in data) {
        result.add( i.trim().replace("\\s+".toRegex(), " ") )
    }

    println(result)
}

fun task6() {
    val datePairs = listOf(
        Pair("2026-01-01", "2026-01-10"),
        Pair("2025-12-31", "2026-01-01"),
        Pair("2026-02-01", "2026-01-22")
    )

    val result = mutableListOf<Long>()

    for ((firstStr, secondStr) in datePairs) {
        val firstDate = LocalDate.parse(firstStr)
        val secondDate = LocalDate.parse(secondStr)

        val difference = secondDate.toEpochDay() - firstDate.toEpochDay()
        result.add(difference)
    }

    println(result)
}

fun task7() {
    val data = listOf("math:Ivan", "bio:Olga", "math:Max", "bio:Ivan", "cs:Olga")
    val result = mutableMapOf<String, MutableList<String>>()

    for (entry in data) {
        val parts = entry.split(":")
        val subject = parts[0]
        val student = parts[1]

        if (!result.containsKey(subject)) {
            result[subject] = mutableListOf()
        }

        result[subject]?.add(student)
    }

    println(result)
}

fun task8() {
    val strings = listOf(
        "Start at 2026/01/22 09:14",
        "No time here",
        "End: 22-01-2026 18:05"
    )

    val result = mutableListOf<String>()

    val pattern = Regex("""(\d{4})/(\d{2})/(\d{2}) (\d{2}:\d{2})|(\d{2})-(\d{2})-(\d{4}) (\d{2}:\d{2})""")

    for (str in strings) {
        val match = pattern.find(str)

        if (match != null) {
            val groups = match.groups

            if (groups[1] != null) {
                val year = groups[1]?.value ?: ""
                val month = groups[2]?.value ?: ""
                val day = groups[3]?.value ?: ""
                val time = groups[4]?.value ?: ""
                result.add("$year-$month-$day $time")
            } else {
                val day = groups[5]?.value ?: ""
                val month = groups[6]?.value ?: ""
                val year = groups[7]?.value ?: ""
                val time = groups[8]?.value ?: ""
                result.add("$year-$month-$day $time")
            }
        }
    }

    println(result)
}