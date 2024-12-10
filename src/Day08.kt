fun main() {
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 14)
    check(part2(testInput) == 34)

    val input = readInput("Day08")
    measureAndPrintResult {
        part1(input)
    }
    measureAndPrintResult {
        part2(input)
    }
}

private fun part1(input: List<String>): Int {
    val nodes = mutableSetOf<Point>()
    antennas(input) { a, d ->
        val node = a + d
        if (node inBoundsOf input) {
            nodes += node
        }
    }
    return nodes.size
}

private fun part2(input: List<String>): Int {
    val nodes = mutableSetOf<Point>()
    antennas(input) { a, d ->
        input.indices.forEach {
            val node = a + (d * it)
            if (!node.inBoundsOf(input)) {
                return@forEach
            }
            nodes += node
        }
    }
    return nodes.size
}

private fun antennas(input: List<String>, block: (a: Point, d: Point) -> Unit) {
    input.toCharGrid()
        .entries
        .filter { it.value != '.' }
        .groupBy({ it.value }, { it.key })
        .values
        .forEach { antennas ->
            combinations(antennas, 2).forEach { (a, b) ->
                val d1 = Point(x = a.x - b.x, y = a.y - b.y)
                val d2 = Point(x = b.x - a.x, y = b.y - a.y)
                block(a, d1)
                block(b, d2)
            }
        }
}