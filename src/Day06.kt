fun main() {
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 41)
    check(part2(testInput) == 6)

    val input = readInput("Day06")
    measureAndPrintResult {
        part1(input)
    }
    measureAndPrintResult {
            part2(input)
    }
}

private fun part1(input: List<String>): Int = walkGuard(findStart(input), input)
    .distinctBy { it.value.first }
    .count()

private fun part2(input: List<String>): Int {
    val start = findStart(input)
    return walkGuard(start, input)
        .mapTo(mutableSetOf()) { it.value.first }
        .minus(start.first)
        .count { obstacle ->
            val (p, dir) = walkGuard(start, input, obstacle).last().value
            (p + dir) inBoundsOf input
        }
}

private fun findStart(input: List<String>) = input.withIndex().firstNotNullOf { (y, line) ->
    lateinit var direction: Point
    val x = line.indexOfFirst {
        direction = when (it) {
            '>' -> Direction.Right
            'v' -> Direction.Down
            '<' -> Direction.Left
            '^' -> Direction.Up
            else -> return@indexOfFirst false
        }.asPoint()
        true
    }.takeUnless { it == -1 } ?: return@firstNotNullOf null
    Point(x, y) to direction
}

private fun walkGuard(start: Pair<Point, Point>, input: List<String>, obstacle: Point? = null) = bfs(start) { (p, direction) ->
    val next = p + direction
    val char = if (next == obstacle) '#' else input.getOrNull(next.y)?.getOrNull(next.x)
    when (char) {
        '#' -> listOf(p to direction.turnRight())
        '.', '^', '>', 'v', '<' -> listOf(next to direction)
        else -> emptyList()
    }
}
