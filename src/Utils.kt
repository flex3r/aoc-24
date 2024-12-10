import java.math.BigInteger
import java.security.MessageDigest
import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.math.abs
import kotlin.time.measureTime

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readText().trim().lines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

inline fun <T> measureAndPrintResult(crossinline block: () -> T) {
    measureTime {
        println(block())
    }.also { println("Took $it") }
}

fun String.ints(delimiter: String = " "): List<Int> = split(delimiter).map(String::toInt)

fun String.longs(delimiter: String = " "): List<Long> = split(delimiter).map(String::toLong)

fun <T> List<T>.pair() = this[0] to this[1]

inline fun <T> Collection<T>.partitionBy(predicate: (T) -> Boolean): List<List<T>> {
    return fold(mutableListOf(mutableListOf<T>())) { acc, it ->
        when {
            predicate(it) -> acc.add(mutableListOf())
            else -> acc.last() += it
        }
        acc
    }
}

fun List<String>.toCharGrid(): Map<Point, Char> = flatMapIndexed { y, line ->
    line.mapIndexed { x, c ->
        Point(x, y) to c
    }
}.toMap()

inline fun <T> List<String>.toGrid(transform: (Char) -> T): Map<Point, T> = flatMapIndexed { y, line ->
    line.mapIndexed { x, c ->
        Point(x, y) to transform(c)
    }
}.toMap()

fun <T> Iterable<T>.appendToString(transform: ((T) -> Char)): String {
    val buffer = StringBuilder()
    for (c in this) {
        buffer.append(transform(c))
    }
    return buffer.toString()
}

data class Point(val x: Int, val y: Int) {
    val cardinal
        get() = listOf(
            this + Direction.Right,
            this + Direction.Down,
            this + Direction.Left,
            this + Direction.Up,
        )

    val diagonals
        get() = listOf(
            this + Direction.DownRight,
            this + Direction.DownLeft,
            this + Direction.UpLeft,
            this + Direction.UpRight,
        )

    fun turnLeft() = copy(x = y, y = -x)
    fun turnRight() = copy(x = -y, y = x)

    operator fun plus(other: Point) = copy(x = x + other.x, y = y + other.y)
    operator fun times(factor: Int) = copy(x = x * factor, y = y * factor)
    operator fun plus(direction: Direction) = when (direction) {
        Direction.Right -> copy(x = x + 1)
        Direction.Left -> copy(x = x - 1)
        Direction.Down -> copy(y = y + 1)
        Direction.Up -> copy(y = y - 1)
        Direction.DownRight -> copy(x = x + 1, y = y + 1)
        Direction.DownLeft -> copy(x = x - 1, y = y + 1)
        Direction.UpLeft -> copy(x = x - 1, y = y - 1)
        Direction.UpRight -> copy(x = x + 1, y = y - 1)
    }
}

enum class Direction {
    Right, Left, Down, Up, DownRight, DownLeft, UpLeft, UpRight;

    companion object {
        val cardinals = listOf(
            Right, Left, Down, Up
        ).map { it.asPoint() }

        val diagonals = listOf(
            DownRight, DownLeft, UpLeft, UpRight
        ).map { it.asPoint() }
    }
}

fun Direction.asPoint() = when (this) {
    Direction.Right -> Point(1, 0)
    Direction.Left -> Point(-1, 0)
    Direction.Down -> Point(0, 1)
    Direction.Up -> Point(0, -1)
    Direction.DownRight -> Point(1, 1)
    Direction.DownLeft -> Point(-1, 1)
    Direction.UpLeft -> Point(-1, -1)
    Direction.UpRight -> Point(1, -1)
}

fun <T> bfs(start: T, neighbors: (T) -> List<T>) = sequence {
    val queue = ArrayDeque(listOf(start.withIndex(index = 0)))
    val seen = mutableSetOf(start)
    while (queue.isNotEmpty()) {
        val (index, current) = queue.removeFirst().also { yield(it) }
        neighbors(current).forEach { neighbor ->
            if (seen.add(neighbor)) {
                queue.add(neighbor.withIndex(index = index + 1))
            }
        }
    }
}

fun <T> T.withIndex(index: Int) = IndexedValue(index, value = this)

infix fun Point.inBoundsOf(lines: List<String>): Boolean {
    return y in lines.indices && x in lines[0].indices
}

fun <T> combinations(values: List<T>, m: Int) = sequence {
    val n = values.size
    val result = MutableList(m) { values[0] }
    val stack = Stack<Int>()
    stack.push(0)
    while (stack.isNotEmpty()) {
        var resIndex = stack.lastIndex
        var arrIndex = stack.pop()

        while (arrIndex < n) {
            result[resIndex++] = values[arrIndex++]
            stack.push(arrIndex)

            if (resIndex == m) {
                yield(result.toList())
                break
            }
        }
    }
}