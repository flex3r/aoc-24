import kotlin.math.max

private val testInput = """
Register A: 729
Register B: 0
Register C: 0

Program: 0,1,5,4,3,0
""".trimIndent().lines()

fun main() {
    check(part1(testInput) == "4,6,3,5,6,3,5,2,1,0")
    val input = readInput("Day17")
    measureAndPrintResult {
        part1(input)
    }
    measureAndPrintResult {
        part2(input)
    }
}


private fun part1(input: List<String>): String {
    val (a, program) = input.partitionBy(String::isEmpty).let { (first, second) ->
        first.first().substringAfter(": ").toLong() to second.first().substringAfter(": ").ints(delimiter = ",")
    }
    return run(program, a).joinToString(",")
}

private fun part2(input: List<String>): Long {
    val program = input.last().substringAfter(": ").ints(delimiter = ",")
    fun find(a: Long): Long? = (a..a + 8).firstNotNullOfOrNull {
        when (val out = run(program, it)) {
            program.takeLast(out.size) -> it.takeIf { program == out } ?: find(max(it shl 3, 8))
            else -> null
        }
    }
    return find(a = 0)!!
}

private fun run(program: List<Int>, a: Long): List<Int> {
    val registers = longArrayOf(a, 0, 0)

    fun combo(op: Int) = when (op) {
        in 0..3 -> op.toLong()
        else -> registers[op - 4]
    }

    var p = 0
    val output = mutableListOf<Int>()
    while (p in program.indices && p + 1 in program.indices) {
        val op = program[p]
        val operand = program[p + 1]
        when (op) {
            0 -> registers[0] = registers[0] shr combo(operand).toInt()
            1 -> registers[1] = registers[1] xor operand.toLong()
            2 -> registers[1] = combo(operand) % 8
            3 -> if (registers[0] != 0L) p = operand - 2
            4 -> registers[1] = registers[1] xor registers[2]
            5 -> output += (combo(operand) % 8).toInt()
            6 -> registers[1] = registers[0] shr combo(operand).toInt()
            7 -> registers[2] = registers[0] shr combo(operand).toInt()
        }
        p += 2
    }
    return output
}
