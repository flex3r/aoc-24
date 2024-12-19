private val testInput = """
##########
#..O..O.O#
#......O.#
#.OO..O.O#
#..O@..O.#
#O#..O...#
#O..O..O.#
#.OO.O.OO#
#....O...#
##########

<vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
<<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
>^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
<><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
""".trimIndent().lines()

fun main() {
    check(part1(testInput) == 10092)
    check(part2(testInput) == 9021L)

    val input = readInput("Day15")
    measureAndPrintResult {
        part1(input)
    }
    measureAndPrintResult {
        part2(input)
    }
}


private fun part1(input: List<String>): Int {
    val (grid, moves) = parse(input)

    moves.forEach { move ->
        val toMove = mutableListOf<Point>()
        var current = grid.entries.first { (_, c) -> c == '@' }.key
        if (grid[current + move] == '#') return@forEach
        while (grid[current] == 'O' || grid[current] == '@') {
            toMove += current
            current = current + move
        }
        if (grid[current] == '.') toMove.reverse() else return@forEach
        toMove.forEach { grid[it + move] = grid.getValue(it) }
        grid[toMove.last()] = '.'
    }

    return grid.filter { it.value == 'O' }
        .keys
        .sumOf {
            it.y * 100 + it.x
        }
}

private fun part2(input: List<String>): Long {
    val (grid, moves) = parse(input, wide = true)
    moves.forEach { move ->
        val toMove = move(move, grid).ifEmpty { return@forEach }
        when (move) {
            Direction.Left, Direction.Right -> {
                toMove.forEach { grid[it + move] = grid.getValue(it) }
                grid[toMove.last()] = '.'
            }
            else -> {
                val robot = grid.entries.first { (_, c) -> c == '@' }.key
                toMove.forEach {
                    val prev = it - move
                    val prevC = grid.getValue(prev)
                    grid[it] = when {
                        prev !in toMove -> if (prevC == '@') '@' else '.'
                        prevC == '#' -> '.'
                        else -> prevC
                    }
                }
                grid[robot] = '.'
            }
        }
    }

    return grid.filter { it.value == '[' }
        .keys
        .sumOf {
            it.y * 100L + it.x
        }
}

private fun move(move: Direction, grid: Map<Point, Char>): Set<Point> {
    val toMove = mutableSetOf<Point>()
    var current = grid.entries.first { (_, c) -> c == '@' }.key
    if (grid[current + move] == '#') return emptySet()
    when (move) {
        Direction.Left, Direction.Right -> {
            while (grid[current] == '[' ||grid[current] == ']' || grid[current] == '@') {
                toMove += current
                current = current + move
            }
            return if (grid[current] == '.') toMove.reversed().toSet() else emptySet()
        }
        else -> {
            val toMove = mutableListOf<Point>()
            var movingRow = listOf(current)
            while(true) {
                if (movingRow.any { grid[it + move] == '#'}) return emptySet()
                toMove += movingRow
                if (movingRow.all { grid[it + move] == '.' }) {
                    toMove += toMove.mapNotNull { (it + move).takeIf { grid[it] == '.' } }
                    val result = toMove.drop(1).sortedBy(Point::y)
                    return if (move == Direction.Down) result.reversed().toSet() else result.toSet()
                }
                movingRow = movingRow.flatMap {
                    val new = it + move
                    val c = grid.getValue(new)
                    when (c) {
                        '[' -> listOf(new, new + Direction.Right)
                        ']' -> listOf(new, new + Direction.Left)
                        else -> emptyList()
                    }
                }.distinct()
            }
        }
    }
}

private fun parse(input: List<String>, wide: Boolean = false): Pair<MutableMap<Point, Char>, List<Direction>> {
    val (first, second) = input.partitionBy(String::isEmpty)
    val moves = second.flatMap { line ->
        line.map {
            when (it) {
                '^' -> Direction.Up
                '>' -> Direction.Right
                'v' -> Direction.Down
                else -> Direction.Left
            }
        }
    }
    if (!wide) return first.toCharGrid().toMutableMap() to moves

    val map = mutableMapOf<Point, Char>()
    input.forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            val left = Point(x * 2, y)
            val right = Point(x * 2 + 1, y)
            when (c) {
                '.', '#' -> {
                    map[left] = c
                    map[right] = c
                }

                'O' -> {
                    map[left] = '['
                    map[right] = ']'
                }

                '@' -> {
                    map[left] = c
                    map[right] = '.'
                }
            }
        }
    }
    return map to moves
}