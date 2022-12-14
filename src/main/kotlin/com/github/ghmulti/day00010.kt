package com.github.ghmulti

private fun Char.toPriority() = when {
    this.isUpperCase() -> code - 'A'.code + 26 + 1
    else -> code - 'a'.code + 1
}

private fun String.toPriority(): Int {
    val (part1, part2) = substring(0, length/2) to substring(length/2, length)
    //println("source $this (${this.length}) part1 $part1 (${part1.length}), part2 $part2 (${part2.length})")
    val intersect = part1.toCharArray().toSet().intersect(part2.toCharArray().toSet()).first()
    //println("intersection ${intersect.code}, ${'a'.code}, ${'z'.code}, ${'A'.code}, ${'Z'.code}, ${'a'.toPriority()}, ${'A'.toPriority()}")
    return intersect.toPriority()
}

private fun List<String>.toPriority(): Int {
    val intersect = reduce { s1, s2 -> s1.toCharArray().toSet().intersect(s2.toCharArray().toSet()).joinToString("") }.first()
    //println("source $this, intersection $intersect")
    return intersect.toPriority()
}

fun day00010() {
    val sumOfPriorities = "day00010.txt".pathTo().toFile().useLines { lines ->
        lines.sumOf { line -> line.toPriority() }
    }
    "Sum of priorities by locating in both compartments of rucksack is $sumOfPriorities".cowsay("day3")

    val sumOfPrioritiesByBadges = "day00010.txt".pathTo().toFile().useLines { lines ->
        lines.chunked(3)
            .sumOf { chunk -> chunk.toPriority() }
    }
    "Sum of priorities by badges for elf group is $sumOfPrioritiesByBadges".cowsay("day3")
}