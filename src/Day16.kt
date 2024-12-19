import java.util.*

private val testInput = """
###############
#.......#....E#
#.#.###.#.###.#
#.....#.#...#.#
#.###.#####.#.#
#.#.#.......#.#
#.#.#####.###.#
#...........#.#
###.#.#####.#.#
#...#.....#.#.#
#.#.#.###.#.#.#
#.....#...#.#.#
#.###.#.#.#.#.#
#S..#.....#...#
###############
""".trimIndent().lines()

fun main() {
    check(part1(testInput) == 7036)
    check(part2(testInput) == 45)

    val input = readInput("Day16")
    measureAndPrintResult {
        part1(input)
    }
    measureAndPrintResult {
        part2(input)
    }
}


private fun part1(input: List<String>): Int {
    val grid = input.toCharGrid()
    var minCost = Int.MAX_VALUE
    dijkstra(grid) {
        if (it.cost > minCost) return@dijkstra true
        minCost = it.cost
        false
    }

    return minCost
}

private fun part2(input: List<String>): Int {
    val grid = input.toCharGrid()
    val seats = mutableSetOf<Point>()
    var minCost = Int.MAX_VALUE
    dijkstra(grid) {
        if (it.cost > minCost) return@dijkstra true
        minCost = it.cost
        seats += it.path
        false
    }
    return seats.size
}

private inline fun dijkstra(grid: Map<Point, Char>, onEndReached: (Reindeer) -> Boolean) {
    val start = grid.entries.first { it.value == 'S' }.key
    val end = grid.entries.first { it.value == 'E' }.key
    val seen = mutableMapOf<Pair<Point, Direction>, Int>().withDefault { Int.MAX_VALUE }
    val queue = PriorityQueue(compareBy(Reindeer::cost))
    queue += Reindeer(listOf(start), Direction.Right, 0)
    while (queue.isNotEmpty()) {
        val current = queue.poll()
        val (path, direction, cost) = current
        val entry = path.last() to direction
        val best = seen.getValue(entry)
        if (cost < best) seen[entry] = cost
        if (path.last() == end && onEndReached(current)) return
        if (cost <= best) current.next(grid).forEach(queue::add)
    }
}

private data class Reindeer(val path: List<Point>, val direction: Direction, val cost: Int) {
    fun next(grid: Map<Point, Char>): List<Reindeer> = (Direction.cardinals - direction.opposite)
        .filterNot { grid[path.last() + it] == '#' }
        .map {
            Reindeer(
                path = path + (path.last() + it),
                direction = it,
                cost = cost + if (it == direction) 1 else 1001
            )
        }
}