private val testInput = """
    AAAA
    BBCD
    BBCC
    EEEC
""".trimIndent().lines()

private val otherTestInput = """
    OOOOO
    OXOXO
    OOOOO
    OXOXO
    OOOOO
""".trimIndent().lines()

fun main() {
    check(part1(testInput) == 140)
    check(part1(otherTestInput) == 772)

    check(part2(testInput) == 80)
    check(part2(otherTestInput) == 436)

    val input = readInput("Day12")
    measureAndPrintResult {
        part1(input)
    }
    measureAndPrintResult {
        part2(input)
    }
}

private fun part1(input: List<String>) = findRegions(input.toCharGrid()).sumOf { it.size * perimeter(it) }

private fun part2(input: List<String>) = findRegions(input.toCharGrid()).sumOf { it.size * sides(it) }

private fun findRegions(grid: Map<Point, Char>): List<Set<Point>> = buildList {
    grid.entries.forEach { (point, plot) ->
        if (none { point in it }) this += findRegion(plot, point, grid)
    }
}

private fun findRegion(plot: Char, start: Point, grid: Map<Point, Char>): Set<Point> {
    val queue = ArrayDeque(listOf(start))
    val visited = mutableSetOf(start)
    while (queue.isNotEmpty()) {
        val current = queue.removeFirst()
        val neighbors = current.cardinal.filter { it !in visited && grid[it] == plot }
        visited += neighbors
        queue += neighbors
    }
    return visited
}

private fun perimeter(region: Set<Point>) = region.sumOf { plot -> 4 - plot.cardinal.count { it in region } }

private fun sides(region: Set<Point>) = Direction.cardinals.sumOf { dir ->
    val shifted = mutableMapOf<Point, Char>()
    region.forEach { p ->
        val outside = (p + dir).takeIf { it !in region } ?: return@forEach
        shifted[outside] = 's'
    }
    findRegions(shifted).size
}
