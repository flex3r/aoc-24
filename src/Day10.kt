fun main() {
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 36)
    check(part2(testInput) == 81)

    val input = readInput("Day10")
    measureAndPrintResult {
        part1(input)
    }
    measureAndPrintResult {
        part2(input)
    }
}

private fun part1(input: List<String>): Int = sumOfTrailHeads(input) { head, grid ->
    bfs(head) { point ->
        val height = grid.getValue(point)
        point.cardinal.mapNotNull { n ->
            val otherHeight = grid[n]
            when {
                otherHeight == null || otherHeight - height != 1 -> null
                else -> n
            }
        }
    }.count { it.index == 9 }
}

private fun part2(input: List<String>): Int = sumOfTrailHeads(input) { head, grid ->
    bfs(head to emptyList<Point>()) { (point, prev) ->
        val height = grid.getValue(point)
        point.cardinal.mapNotNull { n ->
            val otherHeight = grid[n]
            when {
                otherHeight == null || otherHeight - height != 1 -> null
                else -> n to prev.plus(point)
            }
        }
    }.count { it.index == 9 }
}

private inline fun sumOfTrailHeads(input: List<String>, transform: (Point, Map<Point, Int>) -> Int): Int = input.toGrid(Char::digitToInt)
    .let { grid ->
        grid.entries
            .filter { it.value == 0 }
            .sumOf { (p) -> transform(p, grid) }
    }