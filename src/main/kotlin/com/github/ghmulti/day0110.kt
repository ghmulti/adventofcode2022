package com.github.ghmulti

private fun String.findStartOf(size: Int) =
    windowed(size = size, step = 1)
        .takeWhile { str -> str.toSet().size < size }
        .let { it.size + size }

fun day0110() {
    val line = "day0110.txt".pathTo().toFile().readLines().first()
    "Start-of-packet index is ${line.findStartOf(4)}".cowsay("day6")
    "Start-of-message index is ${line.findStartOf(14)}".cowsay("day6")
}