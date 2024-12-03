fun main() {
    fun part1(input: List<String>): Int {
        val regex = "mul\\((\\d+),(\\d+)\\)".toRegex()
        return sumMuls(input, regex)
    }

    fun part2(input: List<String>): Int {
        val regex = "mul\\((\\d+),(\\d+)\\)|do\\(\\)|don't\\(\\)".toRegex()
        return sumMuls(input, regex)
    }

    check(part1(readInput("Day03_test")) == 161)
    check(part2(readInput("Day03_test2")) == 48)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}

private fun sumMuls(input: List<String>, regex: Regex): Int {
    var enabled = true
    return regex.findAll(input.joinToString(separator = "")).sumOf { match ->
        when (match.value) {
            "do()" -> enabled = true
            "don't()" -> enabled = false
            else -> if (enabled) return@sumOf match.groupValues[1].toInt() * match.groupValues[2].toInt()
        }
        0
    }
}