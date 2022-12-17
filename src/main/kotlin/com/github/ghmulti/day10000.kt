package com.github.ghmulti

private data class Figure(
    val parts: List<Pair<Int, Int>>,
)

private const val MOVE_TEST = ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>"
private const val MOVE_PART1 = "><<<>><<<>>><<<>>>><>>><>>>><<>><<>><>>>><<>>><<>><<>>>><<>>><<>><<<<><<<<><><<>><<<<>>>><<><<<>>><>>><<<>>><>><<<<><<<><<>><<<<>>><<<>>><<<>>><<<<><<<>>>><<<<>><<<<>>><<><><>>>><<<>>>><><<<<><<<><>>>><<<>><<>><<>><><<<<><<<<>>><>>>><<>>><><<>><<><<<<>><<>><<>>>><<<<>><<><<><<<<>><<>>><<<<>>><<>><<><<<>>>><<<>><<<>>><>><<><<<<>><<<<><><<<<>>><<<>>><<<>>>><<<<>>><<>>><<<<>>><<<><<><>><>><<<>>><<<<>>>><<>>>><<<<>>><<>>><>><<<>>><<<<>><<<<><>>>><>><>>><<<<>>>><<>>><<<<>><>>>><<<<>><><<<><>>><>>>><>><<<>><<><<<><<<<>>>><>><><<<<><<<>>>><<<<>>>><<<<>><<>><<>>><<<>>>><>>>><<<><<<>>><>><<<<>>><<<>><<<>>>><<>>>><<<>>><<><<><<<<>><<>>><>>>><<>>>><<><<<<>>><<<<><<<<>>><<>>><>>><<>>>><<<<>>>><<><>>><<<<>><<><<>><<<<>><<<<>>><<<>><><<<><<<<>>>><<>>><><<>><<<>><<>>><<<<>><>><<<>><<<>>><>>><>>>><<<><>><<>>>><<<<>><<><<<<>><<<>><<>><<<>>>><><<<>>><>><<<><<<>>><<<><<<<>>><<>>><<<>>>><<<>><<>>><<>><<>>>><<>>>><>>>><<<<>><>>><<<><<<><<><<<<>>>><<>>><<<<><<<<>><>><<>><<<><<>>><>>>><>>>><<<<>>>><>>>><<<>><<<<>>><<<>>><>>><<><<>>>><<<><>>><<>>>><<<<><>>>><><<<<>>><<<>><>>><><<>>>><<<<>>><<<<><<>>><<>>>><>><<<<><<<><<<>>>><<><<<<>>>><<<>>><<<>><<>>><>>>><<<<>>>><<<<>><>><><<<><>>><>><>>>><<<<>>><<<<>><<<>><<<<>>>><<<><>>><<><<<>><<<>>>><<<>><>>>><<<<>>>><>>><<>>><<<>>><<<><>>>><>><<<<>>><>><<<><<<<>>><><<<<>>><<<<><<>>>><<<<>>><<<>><<<><><<>>>><>><<>>><>><<>>><<<<>><<<<><>><<<<>><<<><<>><>>>><<<>>>><<<<>>><<<<>>><>>><>>><<>>><<<<><<<<>><><<<>>><<>><<>>><>>><<<<>>><<<<>>><<<>>><<<<>>><<<<>>>><<>>><<<>><<<<><<><<>><>>>><<<<>>><<>><<>>><<<>>>><<<<>>><<<<><>>>><<<<>>>><<<><>><<<<>>><><<<><>>><<<<><<<<><>>><<<<>>><>>>><>>>><<>>>><<<>>><<>><<<<><<<>>>><<<<>>><<<<><<<<>><<<>><<>>><<<<>><>>>><<<<>><<>>><<<><<>>><<>><<>>>><<<<>>><<<<>>><<<<>>><>>>><>>><<><>><<<<>>>><>>><<>>><<<>><<<><<><<<>><<<>>>><>>><<>><<>>><<<<><<<>>><<<<>>>><<<>>><>>><<>>><<<<>>>><<><<>><>>><<>>>><>>>><>><<<>>>><<>>>><<<<>>><>>><<>>>><<<<>>>><><<<<>>>><<><<<<>>><<<<>>>><<>><>><<<<>>><<><<<>>>><<<>>><<<>>><<<<>>><<>><>>><<<>>>><>>>><<><>>><<<>>><>>><<<><<<><<>>><>>><<>>>><<><<<<>>>><<>><<<<>><<<<>>>><<<><>>><<><<<<>>>><<<>><<<>><>><>>>><>><><<>><<>><<<<><<><<>>>><>>><<<<><<>>>><<><<>>><<<<>><<<<><<<><>>><<>>>><<<<><<<<>>><<<>><<<<><<<<>>>><<>>>><<<<>>><<<><<<><<><<<<>>>><<<>>>><<<>>><<<<>><<<>>>><>>><<<<>><<>>>><<<>>>><>><<<<>>><<<<>>><<><<>>>><<<><<<<>>>><<<>><<>>>><<<>>>><<<><<<<>>><<<>>>><>><<<<>>><<>>>><><<<<>>>><<>><<<>><<<>><<<<>>>><<<<>>>><<<>>><<>>>><<<<><<<<>>><<<>>>><<<<>>><<<<>>><>>><<<<>><<>><>>>><<<><<<>>>><<<>>>><>>><<><><<<>>>><>>>><<<>>>><<>>>><><<<<>><>>><>>>><><<>>>><>>><<>>>><<<>><<>>>><<><<><<>><<<<>>><<<>>>><<>>><<<>>>><>><<<<>>><<>>><<<>>><<<>>>><<><>>><>>>><>>>><<<<>><<>>><><<>>><<>>><<>>><<>><><<<><<<<>>><<<<><<<<><<<<>><<<<><<<><<<<>>><<<<>>>><><<<>><>>><>>><>><<<>>>><<<>>>><<>>><<><>><<<>>><<<><>>><<<>>><<>><<>><<>>>><<<<>>><<<>>>><>><<>>>><<<<><<<>>><<>><<<><<<>>><>>>><<<<><<<>><>><<<>><>>>><<<<><>><<<<>><<>><>>>><<>><<<<>>>><>><>>>><<><<>><>>>><<<>>>><<<<>><>>><<>><<<<>><<<<><<<<>>>><>><<>><<<<><<<>>>><<<>><<>>>><<<>>>><>>>><<<>><<<>><<<><<>>><<>>>><<>>><<<>><<<>><<<>>><<>>><>><<><<<>><<><>><>><<<>>><<<<><<<><>>>><<<><>>><<<>>><<<<>>><<>>><<<<>>>><<>><<<>>>><<><<<><>>><<<>>><<<>><<<<>>><<>>><>><<<>><<>>><>>><<<>>><<<>>>><<<>>>><>>><<<<><>>>><><>>>><>><>>><>><<>><<<<>>>><><<<<>>><>>>><<>>><<<><<<>><<>><<<>>>><<>>>><><<<>>><<<<>><<<<><<>><>>><>><<<>>>><<<<>><<<>>>><<<<><<<>>><<>>>><<>><<<<>>>><<<<>>><<<>>><<>><<<>><>>>><<<>>>><<<<><>>><<><<<>>>><>>>><<>>>><>><<<><<>>><<<>>><<<<>>>><<<<>>><>><<>>><<<<>>>><<<<><<>><<>>><<<<>><<<<>><<><>>>><>>>><<>>>><<>>>><>><<<>>><<<>><<<<>>><<<>>><>><<>><<>><<<>><<<<><<<<><>>><<>>>><>>>><<<>>>><<<>><<><<><<<<>>>><<<<><<<>><<<><>>><<<<>><>><<><<<>><>>><<>>>><<><<>><<>><<<<><<>>><<<<>>><<<><<<<>><<<>>>><><<>>>><<>>>><<<>><<<<>>><<<<><><><<<>><<<<>>><<<<>>>><>>><<<>>>><<<<>><>><<<<>><<>><<<>><<<><<<><<<<>>>><><<>>>><>>><<>>>><>>>><<<<>>><<<>>><<<<><>>><><<<<>><<<<>>><<<<>>>><>>>><<<>>><<<<>>>><<<<>>><>>><<>><<>>>><<<<><<<<>><<>>><<><><<><<<<><<><<<><>>>><<<<>><<>>><>>><<><<>><<>>>><<<<><<<>>>><<>>>><<<><<<><>>><<<>>>><><<<>>>><><>>><<>>>><<<<>>><<<>>>><<<<>><<<>><<<<><<<><<>>>><>>><<<>>>><>>>><<<><<>>>><<<<>>>><>>><<<>><<>><<<<>>><<<>><>>>><<<<>>>><<><<<>>><<<<>><<<>>><><<<<>><<<<>><<><<>><>><><<<<><<>>><<<<>>><<>>><<<<>><<<<>><<><<<<>>><<>><<>>><><<<>>>><>>><<>>>><<<<>><<<<><<>>>><>>>><>>><<<<>>><>><>><<<<>><>>><<<>>>><<<><<<>><<<>>>><<>>><<<>>>><><>>>><<>><<<<>><<<<>>><<>><<>>><>><<<>><<<>>><<<<>>>><<<<>>>><>><<<<>>>><<><<<<>>><>>>><>>><<>>>><<<>>>><>>>><><<<><<>>>><><><<<<>>><<<<>><<<<><<><<<<>>><<<<>><<>>><<<>>><<<<>>><<<<><<<<>>>><<<<>>>><<<>><<>>><<<<><>>>><<>>>><<<<><<<><>>><<<>>>><>>><<<>><<<>><<<>>>><<>>><<><>><>>>><<<>>><<><<<>><<<<><>>><><<<<>><<<>><<<>>>><<><<<<>>><<<<>><<<>><<<>>><<>>>><>>><<>>><<<<><><<<>><<<><>><<>><<<<>>><<<>>>><<<<>>><<><<<<>>><<<>><<<>>>><<<<><><<>>><<<><<<>>><<<<>>>><>>>><<><<<><<<<>>>><<<><>>><<<<><<<<>><<>>>><<>><>>><>>><<>>><><><<<>>>><><>>><>>>><<<><<<<>><<<>>><<<<>><>>>><<><<><<<<>><><><<<>><<<<>>><<<<>>>><>><<<<>><>>>><>>><<<<><<<>>><<<<>><<<<>>>><<>>><<>><<<<>>>><<>>>><<<<>><><<<>>>><><<<<>><>>><<<<>><<<><<<<>>><<>>><<>>><<<>>>><>>>><>>><<>>>><<<<><><>>>><<<><><>>><>>>><<<<><<<<>>><<>>><<<<>>><>>>><<<<>>><<<>><<><>><<<<>>><<>>><<<>><><<<<>>>><<>>>><<<>>>><<>><<<>>>><<<>><<<<>>><>>>><<>>><<<<>><<<>>>><<<<>>>><<>><<<>>><<>><<><><>><<<<><<>><<<<>><<><<<>>>><<>>>><<<><>>>><><<>><<<<>>><<<>>><<<<>>><>>><<<>>><><>>>><<>>>><<<<><<><<>>>><<<>>>><<><<<<>>><<<<>>><<>><<<<><<<>>>><<<>>>><<<<>>>><<<<>>><<>><><>>>><<>><<<>>>><<<<>><<<<>>><<<<>>><><<<>>>><<<<>>><<>><<<><<<<>>><>>>><>>><<>>><<>><<<<>>><<<<>>><<<>><>>><<<>>><<>>>><<>>><<<><<<>><<><<<<>>>><>>>><>>><<>>><<<>>>><<>>>><<><<<>>>><<><<<>><>><<>><<<<>><<>><<<<><<<<><<>>><<<>>>><><<>>>><<<><<>>>><<<<><>><<>><<<>>><<<<>><>><<<<>><<<<>><<>>><<<<><<>>>><>>>><<>>>><>>><>>><<<<>>><><><>>><<<>><<<>><>>><><>><<<<>>>><<>>>><<<>><>>><>><<>>>><<<<>>>><<<>><<<>>><>><<<>>><<<<>><<<<>><<>>>><>><><<<>>><<<>>><><><<<>>>><<<<>><<<><<<<>>>><<<<>>><>><>>>><<<>><<<<><<<<>>>><<>><<<<>>><<><<>>>><<>>><<<>>>><<>>>><<>><<>>>><<<<>>>><<<>>><<>><<<>>><<>>>><<<<>>>><>>><<<<>>><<>>>><<<<><<<>>>><<>><>><<<>>><<<<>><>>>><<<<>>>><>>><<<<>>><>>>><<><<>>><<>><<<<>>>><<<>><<<>>><<<<>><<<>>><<<>><<<><<>><<>>>><<<<>><<<>>><<>><<<>>>><<>>>><<<<>>>><<<>>><<<<>>><<>>><<>>>><<<<><<<<>>>><<<<>><>><<<<>><<><<<<>>>><<>><<<<>>><<<><<<><><<<>>><<<<><>><<<>><<>>>><<>><><<><<>>><<<>><>>><<<<>><>>><><<<>>>><<<<>>>><<>><<>><<<<>>>><<>>>><<<<>><>><<>>><<<>><>><<><<>>>><<<<>><>>>><<<<>>><><>>><>><<<<><<<><<<<>>><<<<>><<<<>>>><>><>>>><<<<>>><>>><<<<>>><<<<>>><<>><<><<<>>><><<<>><>>>><>><>>>><<<<>>><<<<>>><><<<<>>><<>>>><<<<>>>><<<>>><<<>><<<<>><<<>>><<<<><><<<<><><<<>><<>>>><<<<>>><<<<>>>><<<><<<>>>><<>>>><<<<>>>><<>>><<>><<>><<<>>><><<<><<<<><>>><<<<>>>><<<<>>>><<>>><<<<>><<<>>><<>>>><><>>>><<<<>><<<><><<<<><>>><<>>><>><>>><<<>>>><<<>>><>>>><<>>><><<<><<<<><<>>>><>>>><<<<>>>><<<<>>>><<>>><>>><>><<>>><><<><<><<<>><>><><<><<>><<>>><<<>>><<<<>>>><><<><<><<>>><<<>>>><<><<<<>>>><<<>>><<>><><>>>><<<>>>><<>>>><<<>><<<><<>><<<<>>>><<<<>>>><<>>>><<<<>>><<>>><<>>>><<>><>><<<<>><<<<>>><>>>><<>><<>><>><>>>><<<<>>>><<<<>>><>>>><<>><<<><><<<><<<<>>><<<<>>><<<<>>><>>><><><>>>><>>>><<<<>>>><<<<>>>><<>>>><<<<>>>><<>><<<>>><<>>>><<><><>>>><>><<>><<<>>><<<<>>>><>>>><<>>>><><><<<<>>><<<>>>><<>>>><<>>><<>><<><>><<<>>><<<<>>><<<<>>><<>>><<<<>><<<<>><<<<>>><<<>><<<>>>><<<><><<>><>><<>><<<<>>><<<<><><<<>>>><<<<><<>>><<<>>><<<><<<<>><<><<<<>>>><<<<>>><<<<>>><<<<>><<><<>><<<>>><<<<>>>><>><>>><<<><<<<>>>><<<>>><<>><<>><<<<>>><<<>><<>>>><<>><><>>><<>>><<>><<><<<<>>>><>>><<><<>>>><<<>>><<>>><<>>><<<>>><<>>><<><<>>>><<<<>>>><<<>><<<>>><<>><<<>>>><<<<>>>><<<<><>>>><<<<>><>><<>>><>>><>>><<<<>>><<>><>><<>>><>><<>><><<<>>><<<><<<<>>><<<<>>>><<<<>>><<><<>>><<<<>><<<>><<<<>>><<<>><>>><<>>>><<<>>><<><<>><<>><<<>>>><><<<<>><<<>><<<><<<>>>><>>><<<>>>><<<><<>>><<<>>>><>>>><<<<>><<>>>><>>><<<<>>>><<<<>>><>><<<>><<>><<<><<<<>>><>>><<<>>>><><>>><<<<>>><>>>><<<>><<<<>><>>>><><<<>>>><><<<<>>><<>>>><>>><<<>>><<<><<<<>>><<<<><<<<>>>><<<<>><<>>>><<<<>>><>>>><<<>><<<><<<<><<<<>>>><<<>>><<<><>>>><<<>><<<>>><<<<>>>><<<>>>><<<>><<<>>><<>>>><<<<>>><<<><>>><>>>><>><<<>><<>><><<<<><<<>>><<<>><<<<>>>><<<>>>><<>>><<<<>>>><<<>>>><<<>>><<<>>>><>><>>>><<<>>>><<<<>>><<<<>><<<>><<<>>>><<>>><><<>><<<<>><<>>><<<<>>>><<<<>><<<<>>><>><<<>>><<>><<<<>><<<>>><<<><<>>>><<>>><><<<<><>>><<>>><<<<><<<<><>>><<>>>><<<>>>><<>>><<<<><<>>><<>>>><<<<>><<<>>><<<>><<<<>>><<<>><<<<>>><<>>>><>><<<<>>><<<>>>><<<<>><<<>>><<<<>><<><<><>>>><<<><>>>><<<>>><>>><>>>><<<>><<<<>><>><<<>>><<<<><>>>><<<<><<<<>>><<><<<>>>><<<><<><<<<>>>><<><<><<>><<<<>>>><<>>><>>><<<<>>>><<<<><<>><<<>>><<<<>>>><<<>>><>>>><<<<>>><<<>><<<>><<<>>>><<<>><<<<>>><><><<>>>><>>><<>>><<<>><<>>>><>>>><<<><>>>><>>>><<<>>>><<<>><<<<>>>><<<<><<<<>>><<>><<<>>><<<<>>>><>>>><<><<<<><<><>><<<>>><<<><<<<><<<<>>><<<<>>>><<<<>><<<<>>>><<>>><<<>>><<<>>>><<<>><<<>>>><<<<>>>><>>>><<><<>><<<<>><<>><<<<>>><>>>><<<<>><<>>><<>>>><<<><>><><<<<>>><<<><<>>>><<>>><<<<>>><<<>>><>>>><<>>><<<>>><<>><>><>>>><<><<<><<<<>>>><<<<><>>><>><<>>><<<><>>><<<<><>><<<<><<<<><<<<><>>>><<<>>><<<<>><<<<>>><<>>>><<>>>><>>>><<<>><<<<>><>>><<<><<>><><<>>><<<<>>><><<<<>>><>>><<>>><<>>>><><<<>>>><<>>><>>>><>>>><<<<>>><<>>>><<>>>><>><><<<<>>><<><<>>><<<<>>><<<<><<<<><<>>>><>>>><<<<>>>><<<<><<<<>>><<>>><<<><>>><<><<<>>>><<><>>>><><<>><<>>>><<>><<<><<<>>>><>>>><<<><>>><<>>><<<>>><<<<>>>><<<>><<<<><<<>>><<<<>><>>><<>>><<<<>><>><<<>>><><>><<<>>>><<>>>><<<><<<<>><<<<>>>><>><<><<>>>><<<>><>><<<<><>>><><<<<>>><><<<>>><<><<<>>>><><<>>><<><<>>><<<><<<<><<>><<<>>>><<<<>>>><<>>><<<><<<>>><>><<<<>><<>>><<<>><>>><<<><>><<>><<<>><<>><<>>><<<<>>>><>><<><<<<><>>><<<>>><<<<>><>><<>>>><<><<>><<>>><>>><<<>>><<><<<<><><<>>><<>>><<<><><<>>>><<<>><<<<>><<<>>><<<><>>><<>>><<>>>><><<>>><<>><>>>><<>><<<><>><<<>>><<<<>>><<>><<<>>>><<<>>><<>>><<<<>>>><<<>>>><<<>>>><>>>><<>>>><<<>>><<>>>><<>><<>>>><<<><><><<>>><<<>><<<><>>>><>>><<>><<<<>>><<<<>><<<>><>><<<<>><<<<><<<><<<<>><<<>>>><>>>><<>><<><<<>>><<<><<<>>><<>>>><<<>>>><>>>><<<><>>><<>>><<<<>>><<<<>>>><>>"

private fun moveSeq() = sequence {
    while (true) {
        yieldAll(MOVE_PART1.toList())
    }
}
private fun figureSeq() = sequence {
    while (true) {
        yieldAll(listOf(
            Figure(listOf(0 to 0, 0 to 1, 0 to 2, 0 to 3)),
            Figure(listOf(0 to 1, 1 to 0, 1 to 1, 1 to 2, 2 to 1)),
            Figure(listOf(0 to 0, 0 to 1, 0 to 2, 1 to 2, 2 to 2)),
            Figure(listOf(0 to 0, 1 to 0, 2 to 0, 3 to 0)),
            Figure(listOf(0 to 0, 0 to 1, 1 to 0, 1 to 1)),
        ))
    }
}

private data class FigureWithPosition(val figure: Figure, var position: Pair<Long, Long>)

private class Tetris {
    val movementIt = moveSeq().iterator()
    val figureIt = figureSeq().iterator()

    var figure: FigureWithPosition? = null
    val stoppedFigures = mutableSetOf<Pair<Long, Long>>()
    var counter = 0L
    var baseline = 0L
}

private fun FigureWithPosition.normalize(): List<Pair<Long, Long>> {
    return figure.parts.map { p -> p.first + position.first to p.second + position.second }
}

context(Tetris)
private fun Figure.locateFigure(): FigureWithPosition {
    counter += 1
    return FigureWithPosition(
        figure = this,
        position = baseline + 3 to 2
    )
}

private enum class MoveDir { LEFT, RIGHT, DOWN }

context(Tetris)
private fun FigureWithPosition.tryMove(moveDir: MoveDir): Boolean {
    val possibleMove = when (moveDir) {
        MoveDir.LEFT -> position.copy(second = position.second - 1)
        MoveDir.RIGHT -> position.copy(second = position.second + 1)
        MoveDir.DOWN -> position.copy(first = position.first - 1)
    }
    if (possibleMove.first < 0) {
        return false // borders
    }
    val possiblePosition = copy(position = possibleMove)
    if (possiblePosition.normalize().maxOf { it.second } > 6 || possiblePosition.normalize().minOf { it.second } < 0) {
        return false // borders
    }
    val collision = figure.parts.any { p ->
        val targetX = p.first + possibleMove.first
        val targetY = p.second + possibleMove.second
        stoppedFigures.contains(targetX to targetY)
    }
    if (!collision) {
        position = possibleMove
    }
    return !collision
}

private fun Tetris.run() {
    if (figure == null) {
        figure = figureIt.next().locateFigure()
    }
    val currentFigure = figure ?: error("failure")
    //draw("Before gas movement")
    when (movementIt.next()) {
        '>' -> currentFigure.tryMove(MoveDir.RIGHT)
        '<' -> currentFigure.tryMove(MoveDir.LEFT)
        else -> error("not expected")
    }
    //draw("After gas movement")

    if (!currentFigure.tryMove(MoveDir.DOWN)) {
        stoppedFigures.addAll(currentFigure.normalize())
        figure = null
        baseline = stoppedFigures.maxOf { it.first } + 1
    }
}

private fun Tetris.draw(prefix: String) {
    val lines = (maxOf(baseline+3+(figure?.figure?.parts?.maxOf { it.first } ?: 0), 6)downTo 0).map { row ->
        (0L..6L).joinToString(" ") { col ->
            val active = figure?.normalize() ?: emptyList()
            when {
                row to col in stoppedFigures -> "#"
                row to col in active -> "@"
                else -> "."
            }
        }
    }
    println(prefix)
    lines.forEach { line -> println(line) }
}

fun main() {
    val tetris = Tetris()
    while (tetris.counter <= 2022) {
        //println("====${tetris.counter}====")
        tetris.run()
    }
    println(tetris.baseline)

//    val tetris2 = Tetris()
//    while (tetris2.counter <= 1_000_000_000_000) {
//        //println("====${tetris.counter}====")
//        tetris2.run()
//    }
//    println(tetris2.baseline)

}