import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val reports = parseReports(input)
        return reports.count(::validate)
    }

    fun part2(input: List<String>): Int {
        val reports = parseReports(input)
        return reports.count { validate(it) || dampened(it) }
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}

private fun parseReports(input: List<String>): List<List<Int>> = input.map { it.ints() }
private fun validate(report: List<Int>): Boolean = report
    .windowed(2)
    .all { (a, b) -> abs(a - b) in 1..3 }
        && (report.sorted() == report || report.sorted().reversed() == report)
private fun dampened(original: List<Int>): Boolean = original.indices.any {
    validate(original.toMutableList().apply { removeAt(it) })
}
