package com.github.ghmulti


private fun chunks(filename: String): Sequence<List<Int>> = sequence {
    filename.pathTo().toFile().useLines { lines ->
        val chunk = mutableListOf<Int>()
        for (line in lines) {
            if (line.isBlank()) {
                yield(chunk.toList())
                chunk.clear()
            } else {
                chunk.add(line.toInt())
            }
        }
    }
}

fun day00000() {
    val maxChunk = chunks("day00000.txt").maxBy { it.sum() }
    "Elf with most calories: ${maxChunk.sum()}".cowsay("day01")

    val top3maxChunk = chunks("day00000.txt").map { it.sum() }.sortedDescending().take(3).toList()
    "Top 3 elfs with most calories: ${top3maxChunk.sum()}".cowsay("day01")
}