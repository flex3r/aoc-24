import java.math.BigInteger

private val testInput = """
    Button A: X+94, Y+34
    Button B: X+22, Y+67
    Prize: X=8400, Y=5400

    Button A: X+26, Y+66
    Button B: X+67, Y+21
    Prize: X=12748, Y=12176

    Button A: X+17, Y+86
    Button B: X+84, Y+37
    Prize: X=7870, Y=6450

    Button A: X+69, Y+23
    Button B: X+27, Y+71
    Prize: X=18641, Y=10279
""".trimIndent().lines()

fun main() {
    check(part1(testInput) == 480L)

    val input = readInput("Day13")
    measureAndPrintResult {
        part1(input)
    }
    measureAndPrintResult {
        part2(input)
    }
}


private fun part1(input: List<String>): Long = parse(input)
    .mapNotNull { solve(it) }
    .sumOf { it.first * 3L + it.second * 1L }

private fun part2(input: List<String>): Long = parse(input)
    .mapNotNull { solve(it, prizeAdjustment = 10000000000000) }
    .sumOf { it.first * 3L + it.second * 1L }

private fun solve(configuration: Configuration, prizeAdjustment: Long = 0): Pair<Long, Long>? {
    val (ax, ay) = configuration.a
    val (bx, by) = configuration.b
    val (px, py) = configuration.prize.let { (a, b) -> a + prizeAdjustment to b + prizeAdjustment }

    val determinant = (ax * by - bx * ay)
    if (determinant == 0L) return null

    val a1Det = px * by - bx * py
    val a2Det = ax * py - px * ay
    return when {
        a1Det % determinant != 0L || a2Det % determinant != 0L -> null
        else -> a1Det / determinant to a2Det / determinant
    }
}

private fun parse(input: List<String>): List<Configuration> {
    fun parseLine(input: String, delim: Char) = input.split(", ", limit = 2).map { it.substringAfter(delim).toLong() }.pair()
    return input.partitionBy(String::isEmpty).map {
        Configuration(
            a = parseLine(it[0], delim = '+'),
            b = parseLine(it[1], delim = '+'),
            prize = parseLine(it[2], delim = '=')
        )
    }
}

data class Configuration(val a: Pair<Long, Long>, val b: Pair<Long, Long>, val prize: Pair<Long, Long>)
