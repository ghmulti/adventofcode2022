package com.github.ghmulti

import kotlin.math.pow

private val example = """
1=-0-2
12111
2=0=
21
2=01
111
20012
112
1=-1=
1-12
12
1=
122
1=11-2
""".trimIndent()

private fun toSnafu(num: Long): String {
    var number = num
    var snafu = ""
    while (number != 0L) {
        val norm = number + 2
        val code = norm % 5
        val ch = when (code) {
            4L -> '2'
            3L -> '1'
            2L  -> '0'
            1L -> '-'
            0L -> '='
            else -> error("not expected")
        }
        snafu = ch + snafu
        number = norm / 5
    }
    return snafu
}
private fun String.fromSnafu(): Long {
    return chunked(1).reversed().mapIndexed { ind, char ->
        val mult = when (char) {
            "2" -> 2
            "1" -> 1
            "0" -> 0
            "-" -> -1
            "=" -> -2
            else -> error("not expected $char")
        }
        (mult * 5.0.pow(ind)).toLong()
    }.sum()
}
fun main() {
//    val lines = example.lines()
    val lines = "day11000.txt".pathTo().toFile().readLines()

    val mappedNumbers = lines.map { line -> line.fromSnafu()}
//    mappedNumbers.forEach { println(it) }
    val sumOfMapped = mappedNumbers.sum()
    println("Sum of snafu to 10: $sumOfMapped")
    println("Calculated sum to snafu: ${toSnafu(sumOfMapped)}")


//    println(convert(2022))
//    listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 15, 20, 2022, 12345, 314159265).map {
//        it to convert(it.toLong())
//    }.forEach { println(it) }
}