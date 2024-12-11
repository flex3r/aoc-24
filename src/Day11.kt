fun main() {
    check(part1("125 17") == 55312L)

    val input = readInput("Day11").first()
    measureAndPrintResult {
        part1(input)
    }
    measureAndPrintResult {
        part2(input)
    }
}

private fun part1(input: String) = solve(input.longs(), 25)

private fun part2(input: String) = solve(input.longs(), 75)

private fun solve(stones: List<Long>, steps: Int) = stones.sumOf { blink(it, steps) }

private val cache = mutableMapOf<Pair<Long, Int>, Long>()
private fun blink(stone: Long, remaining: Int): Long {
    if (remaining == 0) return 1
    val cached = cache[stone to remaining]
    if (cached != null) return cached

    val step = when {
        stone == 0L -> blink(1, remaining - 1)
        stone.toString().length % 2 == 0 -> {
            val string = stone.toString()
            val half = string.length / 2
            blink(string.take(half).toLong(), remaining - 1) + blink(string.drop(half).toLong(), remaining - 1)
        }

        else -> blink(stone * 2024, remaining - 1)
    }

    cache[stone to remaining] = step
    return step
}
