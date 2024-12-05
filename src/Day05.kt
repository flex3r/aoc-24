fun main() {
    fun part1(input: List<String>): Int {
        val (rules, updates) = parse(input)
        return updates
            .filter { isValid(it, rules) }
            .sumOf { it[it.size / 2] }
    }

    fun part2(input: List<String>): Int {
        val (rules, updates) = parse(input)
        return updates
            .filterNot { isValid(it, rules) }
            .map { reorder(it, rules) }
            .sumOf { it[it.size / 2] }
    }

    check(part1(readInput("Day05_test")) == 143)
    check(part2(readInput("Day05_test")) == 123)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}

private fun parse(input: List<String>) = input.partitionBy(String::isEmpty).let {
    Pair(
        first = it[0].mapTo(mutableSetOf()) { it.ints("|").pair() },
        second = it[1].map { it.ints(",") },
    )
}

private fun isValid(update: List<Int>, rules: Set<Pair<Int, Int>>) = update.zipWithNext().all { it in rules }

private fun reorder(update: List<Int>, rules: Set<Pair<Int, Int>>) = update.sortedWith { a, b ->
    when {
        Pair(a, b) in rules -> -1
        else -> 1
    }
}
