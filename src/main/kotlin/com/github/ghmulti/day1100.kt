package com.github.ghmulti

private fun GridState.buildOptions(lastX: Int, lastY: Int) = listOfNotNull(
    grid.getOrNull(lastX)?.getOrNull(lastY+1)?.let { GridPosition(lastX, lastY+1, it) }, // up
    grid.getOrNull(lastX)?.getOrNull(lastY-1)?.let { GridPosition(lastX, lastY-1, it) }, // down
    grid.getOrNull(lastX+1)?.getOrNull(lastY)?.let { GridPosition(lastX+1, lastY, it) }, // right
    grid.getOrNull(lastX-1)?.getOrNull(lastY)?.let { GridPosition(lastX-1, lastY, it) }, // left
)

private fun GridState.shortest(): Int {
    while (queue.isNotEmpty()) {
        val firstP = queue.removeFirst()
        if (firstP.gp == target) {
            return firstP.level
        }
        val filteredOptions = buildOptions(firstP.gp.x, firstP.gp.y)
            .filter { (_, _, ch) -> ch.code - firstP.gp.ch.code <= 1  }
            .filter { gp -> !visited.contains(gp) }
        queue.addAll(filteredOptions.map { GridPositionWithLevel(it, firstP.level + 1) })
        visited.addAll(filteredOptions)
    }
    return Int.MAX_VALUE
}

private fun List<GridPosition>.calculate(grid: List<List<Char>>, target: GridPosition): Int {
    return map { source ->
        GridState(
            grid = grid,
            target = target,
            queue = mutableListOf(GridPositionWithLevel(source, 0)),
            visited = mutableSetOf(source),
        )
    }.fold(Int.MAX_VALUE) { acc, state ->
        val shortest = state.shortest()
        acc.takeIf { acc < shortest } ?: shortest
    }
}

fun day1100() {
    val initialGrid = "day1100.txt".pathTo().toFile().readLines().map { it.toList() }

    val grid = initialGrid.map { row ->
        row.map { ch -> ch.takeIf { ch != 'S' } ?: 'a' }.map { ch -> ch.takeIf { ch != 'E' } ?: 'z' }
    }
    val target = initialGrid.mapIndexedNotNull { index, chars -> if (chars.contains('E')) index to chars.indexOf('E') else null }
        .firstOrNull()?.let { GridPosition(it.first, it.second, 'z') } ?: error("end not found")
    val sources1 = initialGrid.mapIndexedNotNull { index, chars -> if (chars.contains('S')) index to chars.indexOf('S') else null }
        .firstOrNull()?.let { listOf(GridPosition(it.first, it.second,'a')) } ?: error("start not found")

    val shortest1 = sources1.calculate(grid, target)
    "Fewest steps required to move from your current position to the location that should get the best signal $shortest1".cowsay("day 12")


    val sources2 = grid.flatMapIndexed { row, chars -> chars.mapIndexedNotNull { col, ch -> if (ch == 'a') GridPosition(row, col, 'a') else null } }
    val shortest2 = sources2.calculate(grid, target)
    "Fewest steps required to move starting from any square with elevation a to the location that should get the best signal $shortest2".cowsay("day 12")
}

private data class GridPosition(val x: Int, val y: Int, val ch: Char)
private data class GridPositionWithLevel(val gp: GridPosition, val level: Int)

private data class GridState(
    val grid: List<List<Char>>,
    val target: GridPosition,
    val visited: MutableSet<GridPosition>,
    val queue: MutableList<GridPositionWithLevel>,
)