package com.github.ghmulti

private typealias OverlappingFunc = (IntRange, IntRange) -> Boolean

private fun withinOverlappingFunc(r1: IntRange, r2: IntRange): Boolean = within(r1, r2) || within(r2, r1)
private fun intersectsOverlappingFunc(r1: IntRange, r2: IntRange): Boolean = intersects(r1, r2) || intersects(r2, r1) || withinOverlappingFunc(r1, r2)

private fun within(r1: IntRange, r2: IntRange) = r2.first <= r1.first && r2.last >= r1.last
private fun intersects(r1: IntRange, r2: IntRange) = r1.first <= r2.first && r1.last >= r2.first

private fun String.parseRange(): IntRange = split("-").let { it.first().toInt()..it.last().toInt() }

private fun String.overlapsBy(overlappingFunc: OverlappingFunc): Boolean {
    val (range1, range2) = split(",").let { it.first().parseRange() to it.last().parseRange() }
    //println("range1 $range1, range2 $range2 (${overlappingFunc(range1, range2)})")
    return overlappingFunc(range1, range2)
}

fun day0100() {
    val fullyOverlappingAssignments = "day0100.txt".pathTo().toFile().useLines { lines ->
        lines.sumOf { line -> 1.takeIf { line.overlapsBy(::withinOverlappingFunc) } ?: 0 }
    }
    "$fullyOverlappingAssignments assignments has fully overlapping ranges".cowsay("day4")

    val partiallyOverlappingAssignments = "day0100.txt".pathTo().toFile().useLines { lines ->
        lines.sumOf { line -> 1.takeIf { line.overlapsBy(::intersectsOverlappingFunc) } ?: 0 }
    }
    "$partiallyOverlappingAssignments assignments has partially overlapping ranges".cowsay("day4")
}