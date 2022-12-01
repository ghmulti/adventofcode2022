package com.github.ghmulti

import java.nio.file.Files
import java.nio.file.Path


private fun chunks(filename: String): Sequence<List<Int>> = sequence {
    val lines = Files.lines(Path.of({}.javaClass.classLoader.getResource(filename)?.path ?: error("Unable to find path")))
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

fun day01() {
    val maxChunk = chunks("day01.txt").maxBy { it.sum() }
    "Elf with most calories: ${maxChunk.sum()}".cowsay("day01")

    val top3maxChunk = chunks("day01.txt").map { it.sum() }.sortedDescending().take(3).toList()
    "Top 3 elfs with most calories: ${top3maxChunk.sum()}".cowsay("day01")
}