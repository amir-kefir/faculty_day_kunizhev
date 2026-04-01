import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

data class Log(
    val dt: String,
    val id: Int,
    val status: String
)

fun normalize(line: String): Log? {
    val l = line.trim().lowercase()

    val regexA = Regex("(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}).*id:(\\d+).*status:(sent|delivered)")
    val regexB = Regex("ts=(\\d{2})/(\\d{2})/(\\d{4})-(\\d{2}:\\d{2}).*status=(sent|delivered).*#(\\d+)")
    val regexC = Regex("\\[(\\d{2})\\.(\\d{2})\\.(\\d{4}) (\\d{2}:\\d{2})]\\s*(sent|delivered).*\\(id:(\\d+)\\)")

    regexA.find(l)?.let {
        val (dt, id, status) = it.destructured
        return Log(dt, id.toInt(), status)
    }

    regexB.find(l)?.let {
        val (d, m, y, time, status, id) = it.destructured
        return Log("$y-$m-$d $time", id.toInt(), status)
    }

    regexC.find(l)?.let {
        val (d, m, y, time, status, id) = it.destructured
        return Log("$y-$m-$d $time", id.toInt(), status)
    }

    return null
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

    val normalized = mutableListOf<Log>()
    val broken = mutableListOf<String>()

    logs.forEach {
        val n = normalize(it)
        if (n != null) normalized.add(n)
        else broken.add(it)
    }

    println("Normalized: $normalized")
    println("Broken: $broken")

    val grouped = normalized.groupBy { it.id }

    val results = mutableListOf<Pair<Int, Long>>()
    val incomplete = mutableListOf<Int>()
    val errors = mutableListOf<Int>()

    for ((id, events) in grouped) {
        val sent = events.find { it.status == "sent" }
        val delivered = events.find { it.status == "delivered" }

        if (sent == null || delivered == null) {
            incomplete.add(id)
            continue
        }

        val t1 = LocalDateTime.parse(sent.dt.replace(" ", "T"))
        val t2 = LocalDateTime.parse(delivered.dt.replace(" ", "T"))

        val minutes = ChronoUnit.MINUTES.between(t1, t2)

        if (minutes < 0) {
            errors.add(id)
        } else {
            results.add(id to minutes)
        }
    }

    val sorted = results.sortedByDescending { it.second }

    println("Delevery times: $sorted")
    println("Longest: ${sorted.firstOrNull()}")

    val viol = sorted.filter { it.second > 20 }
    println("Viol (>20 min): $viol")

    println("Incomplete IDs: $incomplete")
    println("Time errors: $errors")
}

main()