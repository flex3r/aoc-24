import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val (left, right) = parseLocations(input)
        return left.zip(right).sumOf { abs(it.first - it.second) }
    }

    fun part2(input: List<String>): Int {
        val (left, right) = parseLocations(input)
        val counted = right.groupingBy { it }.eachCount()
        return left.sumOf { a -> a * counted.getOrDefault(a, 0) }
    }

    val testInput = readInput("Day01_test")
    check(part1(testInput) == 11)
    check(part2(testInput) == 31)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}

private fun parseLocations(input: List<String>): Pair<List<Int>, List<Int>> {
    val left = mutableListOf<Int>()
    val right = mutableListOf<Int>()
    input.forEach { line ->
        val (first, second) = line.split("   ")
        left += first.toInt()
        right += second.toInt()
    }
    return left.sorted() to right.sorted()
}