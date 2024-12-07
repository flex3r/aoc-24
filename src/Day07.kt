fun main() {
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 3749L)
    check(part2(testInput) == 11387L)

    val input = readInput("Day07")
    measureAndPrintResult {
        part1(input)
    }
    measureAndPrintResult {
        part2(input)
    }
}

private fun part1(input: List<String>) = solve(parse(input), listOf(Op.Add, Op.Multiply))

private fun part2(input: List<String>) = solve(parse(input), Op.entries)

private typealias Equation = Pair<Long, List<Long>>

private fun parse(input: List<String>) = input.map { line ->
    val (result, rest) = line.split(": ", limit = 2)
    result.toLong() to rest.longs()
}

private fun solve(equations: List<Equation>, ops: List<Op>) = equations.sumOf { (result, numbers) ->
    fun isSolvable(acc: Long, index: Int): Boolean = when {
        acc > result -> false
        index >= numbers.lastIndex -> acc == result
        else -> ops.any { isSolvable(it.eval(acc, numbers[index + 1]), index + 1) }
    }
    result.takeIf { isSolvable(numbers.first(), index = 0) } ?: 0
}

enum class Op {
    Add, Multiply, Concat;

    fun eval(acc: Long, value: Long): Long = when (this) {
        Add -> acc + value
        Multiply -> acc * value
        Concat -> "$acc$value".toLong()
    }
}
