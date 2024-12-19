fun main() {
    fun part1(input: List<String>): Int {
        val grid = input.toCharGrid().withDefault { '.' }
        val directions = Direction.cardinalPoints + Direction.diagonalPoints
        val match = "XMAS"
        return grid.keys.sumOf { p ->
            directions.count { direction ->
                (0..3).all { grid[p + (direction * it)] == match[it] }
            }
        }
    }

    fun part2(input: List<String>): Int {
        val grid = input.toCharGrid().withDefault { '.' }
        return grid.keys.sumOf { p ->
            Direction.diagonalPoints.count { diagonal ->
                listOf(diagonal, diagonal.turnRight()).all { direction ->
                    (-1..1).appendToString { grid.getValue(p + (direction * it)) } == "MAS"
                }
            }
        }
    }

    check(part1(readInput("Day04_test")) == 18)
    check(part2(readInput("Day04_test")) == 9)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
