package com.github.ghmulti

import kotlin.math.abs

private fun calculateState(lines: Sequence<String>, length: Int): State {
    return lines.fold(State(
        snakePositions = (1..length).map { Position(0, 0) },
        tailVisitedPositions = mutableListOf(),
    )) { state, command ->
        val (direction, step) = command.split(" ").let { it.first() to it.last().toInt() }
        val moveFn = when (direction) {
            "U" -> { e: Position -> e.copy(y = e.y + 1) }
            "D" -> { e: Position -> e.copy(y = e.y - 1) }
            "R" -> { e: Position -> e.copy(x = e.x + 1) }
            "L" -> { e: Position -> e.copy(x = e.x - 1) }
            else -> error("invalid direction")
        }
        return@fold (1..step).fold(state) { acc, _ ->
            val newHeadPosition = moveFn(acc.headPosition)
            val newSnakePositions = acc.snakePositions.drop(1).fold(mutableListOf(newHeadPosition)) { snakeAcc, tailPosition ->
                val newTailPosition = tailPosition.calculateTailPosition(snakeAcc.last())
                snakeAcc.apply { add(newTailPosition) }
            }
            State(
                snakePositions = newSnakePositions,
                tailVisitedPositions = acc.tailVisitedPositions.apply {
                    add(newSnakePositions.last())
                }
            )
        }
    }
}

fun day1001() {
    val resultState = "day1001.txt".pathTo().toFile().useLines { lines ->
        calculateState(lines, 2)
    }
    "Number of visited tail positions for short rope: ${resultState.tailVisitedPositions.toSet().size}".cowsay("day 9")

    val resultStateV2 = "day1001.txt".pathTo().toFile().useLines { lines ->
        calculateState(lines, 10)
    }
    "Number of visited tail positions for long rope: ${resultStateV2.tailVisitedPositions.toSet().size}".cowsay("day 9")
}

private data class State(
    val snakePositions: List<Position>,
    val tailVisitedPositions: MutableList<Position>,
) {
    val headPosition: Position get() = snakePositions.first()
}

private data class Position(val x: Int, val y: Int)

private fun Position.calculateTailPosition(newHeadPosition: Position): Position {
    if (abs(x - newHeadPosition.x) < 2 && abs(y - newHeadPosition.y) < 2) {
        return this
    }
    val distX = abs(x - newHeadPosition.x) >= 2
    val distY = abs(y - newHeadPosition.y) >= 2
    val calculateX = { if (newHeadPosition.x > x) x + 1 else x - 1 }
    val calculateY = { if (newHeadPosition.y > y) y + 1 else y - 1 }
    return when {
        (distY || distX) && x != newHeadPosition.x && y != newHeadPosition.y -> Position(calculateX(), calculateY())
        distX -> copy(x = calculateX())
        distY -> copy(y = calculateY())
        else -> error("not expected")
    }
}