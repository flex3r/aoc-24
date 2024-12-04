import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText

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

fun String.ints(delimiter: String = " "): List<Int> = split(delimiter).map(String::toInt)

fun List<String>.toGrid(): Map<Point, Char> = flatMapIndexed { y, line ->
    line.mapIndexed { x, c ->
        Point(x, y) to c
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

    fun rotate() = copy(y, -x)
    fun counterRotate() = copy(-y, x)

    operator fun plus(other: Point) = copy(x = x + other.x, y = y + other.y)
    operator fun times(factor: Int) = copy(x = x * factor, y = y * factor)
    operator fun plus(direction: Direction) = when (direction) {
        Direction.Right -> copy(x = x + 1)
        Direction.Left -> copy(x = x - 1)
        Direction.Down -> copy(y = y + 1)
        Direction.Up -> copy(y = 1 - 1)
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