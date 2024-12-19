import java.math.BigInteger

private val testInput = """
p=0,4 v=3,-3
p=6,3 v=-1,-3
p=10,3 v=-1,2
p=2,0 v=2,-1
p=0,0 v=1,3
p=3,0 v=-2,-2
p=7,6 v=-1,-3
p=3,0 v=-1,-2
p=9,3 v=2,3
p=7,3 v=-1,2
p=2,4 v=2,-3
p=9,5 v=-3,-3
""".trimIndent().lines()

fun main() {
    check(part1(testInput, width = 11, height = 7) == 12)

    val input = readInput("Day14")
    measureAndPrintResult {
        part1(input)
    }
    measureAndPrintResult {
        part2(input)
    }
}


private fun part1(input: List<String>, width: Int = 101, height: Int = 103): Int {
    val moved = (0..<100).fold(parse(input)) { acc, i ->
        acc.map { it.move(width, height) }
    }
    var (q1, q2, q3, q4) = listOf(0, 0, 0, 0)
    val halfWidth = width / 2
    val halfHeight = height / 2
    moved.forEach { (it, _) ->
        when {
            it.x == halfWidth || it.y == halfHeight -> return@forEach
            it.x in 0..<halfWidth && it.y in 0..<halfHeight -> q1++
            it.x in 0..<halfWidth && it.y in halfHeight + 1..height -> q2++
            it.x in halfWidth + 1..width && it.y in 0..<halfHeight -> q3++
            else -> q4++
        }
    }
    return q1 * q2 * q3 * q4
}

private fun part2(input: List<String>, width: Int = 101, height: Int = 103): Int {
    var robots = parse(input)
    var idx = 0
    while (true) {
        idx++
        robots = robots.map { it.move(width, height) }
        if (robots.mapTo(mutableSetOf(), Robot::position).size == robots.size) {
            break
        }
    }
    print(width, height, robots)
    return idx
}

private fun print(width: Int, height: Int, robots: List<Robot>) {
    (0..<height).forEach { y ->
        (0..<width).forEach { x ->
            when {
                robots.any { it.position.x == x && it.position.y == y } -> print("X")
                else -> print(".")
            }
        }
        println()
    }
    println()
}

private fun parse(input: List<String>): List<Robot> = input.map { line ->
    val (p, v) = line.split(" ").map { it.substringAfter('=').ints(",").let { (a, b) -> Point(a, b) } }
    Robot(p, v)
}

private data class Robot(val position: Point, val velocity: Point) {
    fun move(width: Int, height: Int): Robot {
        val new = position + velocity
        return copy(position = Point(new.x.mod(width), new.y.mod(height)))
    }
}
