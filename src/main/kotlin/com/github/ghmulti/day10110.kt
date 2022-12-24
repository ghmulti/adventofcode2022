package com.github.ghmulti

private val example = """
..............
..............
.......#......
.....###.#....
...#...#.#....
....#...##....
...#.###......
...##.#.##....
....#..#......
..............
..............
..............
""".trimIndent()

private enum class Compass { N, S, W, E }

private fun proposedDirection() = sequence<List<Compass>> {
    val deque = ArrayDeque(Compass.values().toList())
    while (true) {
        yield(deque.toList())
        val first = deque.removeFirst()
        deque.addLast(first)
    }
}

private fun List<String>.extendIfRequired(): List<String> {
    var newList = toMutableList()
    if (newList.first().contains("#")) {
        newList.add(0, (0 until newList.first().length).map { "." }.joinToString(""))
    }
    if (newList.last().contains("#")) {
        newList.add((0 until newList.first().length).map { "." }.joinToString(""))
    }
    if (newList.map { it.first() }.contains('#')) {
        newList = newList.map { ".$it" }.toMutableList()
    }
    if (newList.map { it.last() }.contains('#')) {
        newList = newList.map { "$it." }.toMutableList()
    }
    return newList
}

private data class ElvLocation(val line: Int, val position: Int)
private data class ElvLocationWithTarget(val elvLocation: ElvLocation, val target: ElvLocation)

private fun iterations(lines: List<String>, repeat: Int): List<String> {
    val directionIterator = proposedDirection().iterator()
//    println("=====Initial state=====")
//    lines.forEach { println(it) }
    return (1..repeat).fold(lines) { acc , index ->
        val extendedAcc = acc.extendIfRequired()
        val directionPriorities = directionIterator.next()
        println("=====Index $index=====")
//        println("Direction priorities: $directionPriorities")
//        extendedAcc.forEach { println(it) }

        val elvesLocations = extendedAcc.flatMapIndexed { lineIndex, line ->
            line.mapIndexedNotNull { chIndex, ch ->
                return@mapIndexedNotNull if (ch == '.') null else { ElvLocation(line = lineIndex, position = chIndex) }
            }
        }.toSet()

        // round 1
        val elvesLocationsWithTarget = elvesLocations.mapNotNull { loc ->
            val adjacent = listOf(
                ElvLocation(loc.line - 1, loc.position - 1),
                ElvLocation(loc.line - 1, loc.position),
                ElvLocation(loc.line - 1, loc.position + 1),
                ElvLocation(loc.line, loc.position - 1),
                ElvLocation(loc.line, loc.position + 1),
                ElvLocation(loc.line + 1, loc.position - 1),
                ElvLocation(loc.line + 1, loc.position),
                ElvLocation(loc.line + 1, loc.position + 1),
            )
            return@mapNotNull if (adjacent.none { elvesLocations.contains(it) }) {
                null
            } else {
                val priorityList = directionPriorities.map { direction ->
                   when (direction) {
                       Compass.N -> listOf(
                           ElvLocation(loc.line - 1, loc.position - 1),
                           ElvLocation(loc.line - 1, loc.position),
                           ElvLocation(loc.line - 1, loc.position + 1),
                       ) to ElvLocation(loc.line - 1, loc.position)
                       Compass.S -> listOf(
                           ElvLocation(loc.line + 1, loc.position - 1),
                           ElvLocation(loc.line + 1, loc.position),
                           ElvLocation(loc.line + 1, loc.position + 1),
                       ) to ElvLocation(loc.line + 1, loc.position)
                       Compass.W -> listOf(
                           ElvLocation(loc.line - 1, loc.position - 1),
                           ElvLocation(loc.line, loc.position - 1),
                           ElvLocation(loc.line + 1, loc.position - 1),
                       ) to ElvLocation(loc.line, loc.position - 1)
                       Compass.E -> listOf(
                           ElvLocation(loc.line - 1, loc.position + 1),
                           ElvLocation(loc.line, loc.position + 1),
                           ElvLocation(loc.line + 1, loc.position + 1),
                       ) to ElvLocation(loc.line, loc.position + 1)
                   }
                }

                val target = priorityList.firstNotNullOfOrNull { (listToCheck, selectedPosition) ->
                    if (listToCheck.all { !elvesLocations.contains(it) }) {
                        selectedPosition
                    } else {
                        null
                    }
                }

                target?.let { ElvLocationWithTarget(loc, target = target) }
            }
        }

        // round 2
        if (elvesLocationsWithTarget.isEmpty()) {
            throw RuntimeException("Finished")
        }
        val targetGroups = elvesLocationsWithTarget.groupBy { it.target }
        val targetGroupsWithoutCollision = targetGroups.filter { (_, lst) -> lst.size == 1 }.map { it.value.first() }

        val resultElvesLocations = elvesLocations.map { loc ->
            val firstMatch = targetGroupsWithoutCollision.firstOrNull { it.elvLocation == loc }
            firstMatch?.target ?: loc
        }

        val linesAmount = extendedAcc.size
        val rowLenght = extendedAcc.first().length
        val fold = (0 until linesAmount).map { lineIndex ->
            (0 until rowLenght).map { rowIndex ->
                if (resultElvesLocations.any { el -> el.line == lineIndex && el.position == rowIndex }) {
                    '#'
                } else {
                    '.'
                }
            }.joinToString("")
        }
//        println("After iteration")
//        fold.forEach { println(it) }
        return@fold fold
    }
}

fun main() {
//    val lines = example.lines()
    val lines = "day10110.txt".pathTo().toFile().readLines()
    val result = iterations(lines, 10)
    val cleanedUp = cleanup(result)
//    cleanedUp.forEach { println(it) }
    val emptyTiles = cleanedUp.sumOf { it.count { ch -> ch == '.' } }
    println("Empty tiles $emptyTiles")

    val result2 = iterations(lines, 1000)
    val cleanedUp2 = cleanup(result2)
}

private fun cleanup(result: List<String>): List<String> {
    return result
        .dropWhile { !it.contains("#") }
        .reversed().dropWhile { !it.contains("#") }.reversed()
        .let { rows ->
            var firstCol = rows.map { it.first() }.joinToString("")
            var target = rows
            while (!firstCol.contains("#")) {
                target = target.map { it.drop(1) }
                firstCol = target.map { it.first() }.joinToString("")
            }
            target
        }
        .let { rows ->
            var lastCol = rows.map { it.last() }.joinToString("")
            var target = rows
            while (!lastCol.contains("#")) {
                target = target.map { it.dropLast(1) }
                lastCol = target.map { it.last() }.joinToString("")
            }
            target
        }
}