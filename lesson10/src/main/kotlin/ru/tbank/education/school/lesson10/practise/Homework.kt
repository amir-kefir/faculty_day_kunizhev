package ru.tbank.education.school.lesson10.practise

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

data class Rec(
    val dt: String,
    val id: Int,
    val status: String
)

private val fmtOut: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

private val reA = Regex(
    """^\s*(\d{4}-\d{2}-\d{2})\s+(\d{2}:\d{2})\s*\|\s*ID\s*:\s*(\d+)\s*\|\s*STATUS\s*:\s*(sent|delivered)\s*$""",
    setOf(RegexOption.IGNORE_CASE)
)

private val reB = Regex(
    """^\s*TS\s*=\s*(\d{2})/(\d{2})/(\d{4})\s*-\s*(\d{2}):(\d{2})\s*;\s*status\s*=\s*(sent|delivered)\s*;\s*#\s*(\d+)\s*$""",
    setOf(RegexOption.IGNORE_CASE)
)

private val reC = Regex(
    """^\s*\[\s*(\d{2})\.(\d{2})\.(\d{4})\s+(\d{2}):(\d{2})\s*\]\s*(sent|delivered)\s*\(\s*id\s*:\s*(\d+)\s*\)\s*$""",
    setOf(RegexOption.IGNORE_CASE)
)

fun normalize(line: String): Rec? {
    val s = line.trim()

    reA.matchEntire(s)?.let { m ->
        val date = m.groupValues[1]
        val time = m.groupValues[2]
        val id = m.groupValues[3].toInt()
        val status = m.groupValues[4].lowercase(Locale.getDefault())
        return Rec("$date $time", id, status)
    }

    reB.matchEntire(s)?.let { m ->
        val dd = m.groupValues[1]
        val mm = m.groupValues[2]
        val yyyy = m.groupValues[3]
        val hh = m.groupValues[4]
        val min = m.groupValues[5]
        val status = m.groupValues[6].lowercase(Locale.getDefault())
        val id = m.groupValues[7].toInt()
        return Rec("$yyyy-$mm-$dd $hh:$min", id, status)
    }

    reC.matchEntire(s)?.let { m ->
        val dd = m.groupValues[1]
        val mm = m.groupValues[2]
        val yyyy = m.groupValues[3]
        val hh = m.groupValues[4]
        val min = m.groupValues[5]
        val status = m.groupValues[6].lowercase(Locale.getDefault())
        val id = m.groupValues[7].toInt()
        return Rec("$yyyy-$mm-$dd $hh:$min", id, status)
    }

    return null
}

fun parseDt(dt: String): LocalDateTime {
    val f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    return LocalDateTime.parse(dt, f)
}

fun main() {
    val logs = listOf(
        "2026-01-22 09:14 | ID:042 | STATUS:sent",
        "TS=22/01/2026-09:27; status=delivered; #042",
        "2026-01-22 09:10 | ID:043 | STATUS:sent",
        "2026-01-22 09:18 | ID:043 | STATUS:delivered",
        "TS=22/01/2026-09:05; status=sent; #044",
        "[22.01.2026 09:40] delivered (id:044)",
        "2026-01-22 09:20 | ID:045 | STATUS:sent",
        "[22.01.2026 09:33] delivered (id:045)",
        "   ts=22/01/2026-09:50; STATUS=Sent; #046   ",
        " [22.01.2026 10:05]   DELIVERED   (ID:046) "
    )

    val normalized = mutableListOf<Rec>()
    val badLines = mutableListOf<String>()

    for (line in logs) {
        val rec = normalize(line)
        if (rec == null) badLines.add(line) else normalized.add(rec)
    }

    val byId = normalized.groupBy { it.id }

    val durations = mutableListOf<Pair<Int, Long>>()
    val incomplete = mutableListOf<Int>()
    val timeErrors = mutableListOf<Int>()

    val deliveredHours = IntArray(24)

    val dupes = mutableMapOf<Int, Pair<Int, Int>>()

    for ((id, events) in byId) {
        val sentList = events.filter { it.status == "sent" }
        val delList = events.filter { it.status == "delivered" }

        if (sentList.size > 1 || delList.size > 1) {
            dupes[id] = Pair(sentList.size, delList.size)
        }

        if (sentList.isEmpty() || delList.isEmpty()) {
            incomplete.add(id)
            continue
        }

        val sentTime = sentList.minOf { parseDt(it.dt) }
        val delTime = delList.minOf { parseDt(it.dt) }

        if (delTime.isBefore(sentTime)) {
            timeErrors.add(id)
            continue
        }

        val minutes = java.time.Duration.between(sentTime, delTime).toMinutes()
        durations.add(Pair(id, minutes))

        deliveredHours[delTime.hour]++
    }

    val durationsSorted = durations.sortedByDescending { it.second }
    val longest = durationsSorted.firstOrNull()
    val violators = durationsSorted.filter { it.second > 20 }

    println("Нормализованные записи:")
    normalized.forEach { println(it) }

    println("\nБитые строки:")
    badLines.forEach { println(it) }

    println("\nДлительности (id, минуты) по убыванию:")
    println(durationsSorted)

    println("\nСамый долгий заказ:")
    println(longest)

    println("\nНарушители правила (> 20 минут):")
    println(violators)

    println("\nНеполные id (не хватает sent/delivered):")
    println(incomplete.sorted())

    println("\nОшибки времени (delivered раньше sent):")
    println(timeErrors.sorted())

    println("\nB) Час с максимумом delivered:")
    val bestHour = deliveredHours.indices.maxByOrNull { deliveredHours[it] }
    if (bestHour != null && deliveredHours[bestHour] > 0) {
        println("$bestHour час, delivered событий: ${deliveredHours[bestHour]}")
    } else {
        println("нет delivered")
    }

    println("\nC) Дубли (id -> sentCount, deliveredCount) если >1:")
    println(dupes)
}