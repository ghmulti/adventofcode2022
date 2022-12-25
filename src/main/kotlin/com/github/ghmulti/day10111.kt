package com.github.ghmulti

import kotlin.math.abs
import kotlin.math.sqrt

private val example = """
#.######
#>>.<^<#
#.<..<<#
#>v.><>#
#<^v^^>#
######.#
""".trimIndent()

private enum class EDirection { UP, RIGHT, LEFT, DOWN }
private data class EPosition(val row: Int, val column: Int, val direction: EDirection? = null)

private data class Expedition(
    val position: EPosition,
    val walls: List<EPosition>,
    val blizzards: List<EPosition>,
    val path: List<EPosition>,
) {
    val lastNonWallColumnIndex: Int get() = walls.maxOf { it.column } - 1
    val lastNonWallRowIndex: Int get() = walls.maxOf { it.row } - 1
}

private fun EPosition.hitWall(walls: List<EPosition>): Boolean {
    return walls.any { it.column == column && it.row == row }
}

private fun EPosition.hitSomething(something: List<EPosition>): Boolean {
    return something.any { it.column == column && it.row == row }
}

private fun defaultPosition(lines: List<String>): EPosition {
    return lines.first().indexOfFirst { it == '.' }.let { EPosition(row = 0, column = it) }
}

private fun targetPosition(lines: List<String>): EPosition {
    return lines.last().indexOfFirst { it == '.' }.let { EPosition(row = lines.size - 1, column = it) }
}

private fun Expedition.blizzardsStep(): List<EPosition> {
    return blizzards.map { b ->
        when (b.direction ?: error("not expected")) {
            EDirection.RIGHT -> {
                val newPosition = b.copy(column = b.column + 1)
                if (newPosition.hitWall(walls)) {
                    b.copy(column = 1)
                } else {
                    newPosition
                }
            }
            EDirection.DOWN -> {
                val newPosition = b.copy(row = b.row + 1)
                if (newPosition.hitWall(walls)) {
                    b.copy(row = 1)
                } else {
                    newPosition
                }
            }
            EDirection.LEFT -> {
                val newPosition = b.copy(column = b.column - 1)
                if (newPosition.hitWall(walls)) {
                    b.copy(column = lastNonWallColumnIndex)
                } else {
                    newPosition
                }
            }
            EDirection.UP -> {
                val newPosition = b.copy(row = b.row - 1)
                if (newPosition.hitWall(walls)) {
                    b.copy(row = lastNonWallRowIndex)
                } else {
                    newPosition
                }
            }
        }
    }
}

private fun findWalls(lines: List<String>): List<EPosition> {
    return lines.flatMapIndexed { row, line ->
        line.mapIndexedNotNull { column, ch ->
            if (ch == '#') {
                EPosition(row = row, column = column)
            } else {
                null
            }
        }
    }
}

private fun findBlizzards(lines: List<String>): List<EPosition> {
    return lines.flatMapIndexed { row, line ->
        line.mapIndexedNotNull { column, ch ->
            when (ch) {
                '>' -> EPosition(row = row, column = column, direction = EDirection.RIGHT)
                '<' -> EPosition(row = row, column = column, direction = EDirection.LEFT)
                '^' -> EPosition(row = row, column = column, direction = EDirection.UP)
                'v' -> EPosition(row = row, column = column, direction = EDirection.DOWN)
                else -> null
            }
        }
    }
}

private fun EPosition.checkPossiblePosition(
    walls: List<EPosition>,
    blizzards: List<EPosition>,
): Boolean {
    return !hitSomething(walls) && !hitSomething(blizzards) && row >= 0
}

private fun Expedition.findPositionsToGo(): List<Expedition> {
    val blizzardPositionsNextStep = blizzardsStep()
    val possiblePositions = listOfNotNull(
        position.copy(column = position.column + 1).takeIf { it.checkPossiblePosition(walls, blizzardPositionsNextStep) },
        position.copy(row = position.row + 1).takeIf { it.checkPossiblePosition(walls, blizzardPositionsNextStep) },
        position.copy(row = position.row - 1).takeIf { it.checkPossiblePosition(walls, blizzardPositionsNextStep) },
        position.copy(column = position.column - 1).takeIf { it.checkPossiblePosition(walls, blizzardPositionsNextStep) },
        position.takeIf { it.checkPossiblePosition(walls, blizzardPositionsNextStep) },
    )
    return possiblePositions.map { positionNextStep ->
        copy(
            position = positionNextStep,
            blizzards = blizzardPositionsNextStep,
            path = path + listOf(positionNextStep),
        )
    }
}

private data class PositionWithBlizzards(val position: EPosition, val blizzards: List<EPosition>)

private fun List<EPosition>.adjacentBlizzards(position: EPosition): List<EPosition> {
    return filter { abs(it.column - position.column) <= 2 && abs(it.row - position.row) <= 2 }
}

private fun expeditionBfs(expedition: Expedition, target: EPosition): Expedition {
    val queue = ArrayDeque(listOf(expedition))
    val visited = mutableSetOf<PositionWithBlizzards>()
    while (queue.isNotEmpty()) {
        val first = queue.removeFirst()
//        println("Checking path [current path=${first.path.size}], current position ${first.position}")
        if (first.position == target) {
            println("Reached target!")
            return first
        }
        val nextOptions = first.findPositionsToGo()
            .filter { !visited.contains(PositionWithBlizzards(it.position, it.blizzards.adjacentBlizzards(it.position))) }
//            .sortedBy { (target.row - it.position.row) + (target.column - it.position.column) }
//        println("Found ${nextOptions.size} possible moves")
//        nextOptions.forEach { queue.addFirst(it) }
        queue.addAll(nextOptions)
        visited.addAll(nextOptions.map { PositionWithBlizzards(it.position, it.blizzards.adjacentBlizzards(it.position)) })
    }
    error("not found")
}

fun main() {
//    val lines = example.lines()
    val lines = "day10111.txt".pathTo().toFile().readLines()

    val defaultPosition = defaultPosition(lines)
    val targetPosition = targetPosition(lines)
    val expedition = Expedition(
        position = defaultPosition,
        walls = findWalls(lines),
        blizzards = findBlizzards(lines),
        path = mutableListOf(),
    )

    println("Default position: ${expedition.position}")
//    println("Walls: ${expedition.walls}")
//    println("Blizzards: ${expedition.blizzards}")

    val firstExpedition = expeditionBfs(expedition, targetPosition)
    println("Finished first expedition, shortest path ${firstExpedition.path.size}")
//    bfsExpedition.path.forEach { println(it) }

    val expeditionBack = firstExpedition.copy(
        path = mutableListOf(),
    )
    val secondExpedition = expeditionBfs(expeditionBack, defaultPosition)
    println("Finished second expedition, shortest path ${secondExpedition.path.size}")

    val expeditionBackAndForth = secondExpedition.copy(
        path = mutableListOf()
    )
    val thirdExpedition = expeditionBfs(expeditionBackAndForth, targetPosition)
    println("Finished third expedition, shortest path ${thirdExpedition.path.size}")

    val sum = firstExpedition.path.size + secondExpedition.path.size + thirdExpedition.path.size
    "$sum is the fewest number of minutes required to reach the goal, go back to the start, then reach the goal again"
        .cowsay("day 24")
}