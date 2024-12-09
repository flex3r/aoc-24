import java.util.*

fun main() {
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 1928L)
    check(part2(testInput) == 2858L)

    val input = readInput("Day09")
    measureAndPrintResult {
        part1(input)
    }
    measureAndPrintResult {
        part2(input)
    }
}

private fun part1(input: List<String>): Long {
    val layout = mutableListOf<Type>()
    input.first().forEachIndexed { index, value ->
        val type = when (index % 2) {
            0 -> Type.Block(id = index / 2L)
            else -> Type.Space
        }
        layout += (0..<value.digitToInt()).map { type }
    }

    for (i in layout.lastIndex downTo 0) {
        if (layout[i] == Type.Space) continue
        val j = layout.indexOfFirst { it == Type.Space }
        if (j == -1 || j >= i) break
        Collections.swap(layout, j, i)
    }

    return layout.withIndex().sumOf { (idx, block) ->
        idx * ((block as? Type.Block)?.id ?: return@sumOf 0)
    }
}

private fun part2(input: List<String>): Long {
    val layout = mutableListOf<Chunk>()
    input.first().forEachIndexed { index, value ->
        layout += when (index % 2) {
            0 -> Chunk(Type.Block(id = index / 2L), value.digitToInt())
            else -> Chunk(Type.Space, value.digitToInt())
        }
    }

    layout
        .mapNotNull { (it.type as? Type.Block)?.id }
        .reversed()
        .forEach { id ->
            val chunk = layout.last { (it.type as? Type.Block)?.id == id }
            val chunkIdx = layout.indexOf(chunk)
            val space = layout.firstOrNull { it.type == Type.Space && it.length >= chunk.length } ?: return@forEach
            val spaceIdx = layout.indexOf(space)
            if (spaceIdx >= chunkIdx) return@forEach

            if (chunk.length == space.length) {
                layout[chunkIdx] = space
                layout[spaceIdx] = chunk
                return@forEach
            }

            layout[spaceIdx] = chunk
            layout[chunkIdx] = space.copy(length = chunk.length)
            layout.add(spaceIdx + 1, space.copy(length = space.length - chunk.length))
        }

    return layout.flatMap { (0..<it.length).map { _ -> it.type } }
        .withIndex()
        .sumOf { (idx, it) ->
            when (it) {
                is Type.Block -> it.id * idx
                Type.Space -> 0L
            }
        }
}


private sealed interface Type {
    data class Block(val id: Long) : Type
    data object Space : Type
}

private data class Chunk(val type: Type, val length: Int)
