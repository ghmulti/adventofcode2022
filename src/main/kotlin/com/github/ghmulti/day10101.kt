package com.github.ghmulti

val testMap = """
        ...#
        .#..
        #...
        ....
...#.......#
........#...
..#....#....
..........#.
        ...#....
        .....#..
        .#......
        ......#.

10R5L5R10L4R5L5
""".trimIndent()

private data class Tile(val symbol: Char, val walkable: Boolean, val isWall: Boolean, val isEmpty: Boolean)

private enum class Direction(val code: Int) { UP(3), RIGHT(0), DOWN(1), LEFT(2) }
private data class PositionWithDirection(val position: Pair<Int, Int>, val direction: Direction)

private data class Journey(
    val tiles: List<List<Tile>>,
    var position: Pair<Int, Int>,
    var direction: Direction,
    val instructions: ArrayDeque<String>,
    val path: MutableList<PositionWithDirection>
)

context(Journey)
private fun Pair<Int, Int>.change(length: Int): Pair<Int, Int> {
    val fn = { y: Int, x: Int ->
        when (direction) {
            Direction.DOWN -> if (y + 1 <= tiles.size - 1) y + 1 to x else 0 to x
            Direction.UP -> if (y - 1 >= 0) y - 1 to x else tiles.size - 1 to x
            Direction.LEFT -> if (x - 1 >= 0) y to x - 1 else y to tiles[y].size - 1
            Direction.RIGHT -> if (x + 1 <= tiles[y].size - 1) y to x + 1 else y to 0
        }
    }
    val targetPosition = (1..length).fold(this) { (y, x), _ ->
        var newCoords = fn(y, x)
        while (tiles[newCoords.first][newCoords.second].isEmpty) {
            newCoords = fn(newCoords.first, newCoords.second)
        }
        if (tiles[newCoords.first][newCoords.second].isWall) {
            return@fold y to x
        }
        path.add(PositionWithDirection(newCoords, direction))
        newCoords
    }
    return targetPosition
}

private fun Journey.move() {
    val instruction = instructions.removeFirst()
    when {
        instruction.toIntOrNull() != null -> {
            position = position.change(instruction.toInt())
        }
        instruction == "R" -> direction = when (direction) {
            Direction.DOWN -> Direction.LEFT
            Direction.LEFT -> Direction.UP
            Direction.UP -> Direction.RIGHT
            Direction.RIGHT -> Direction.DOWN
        }
        instruction == "L" -> direction = when (direction) {
            Direction.DOWN -> Direction.RIGHT
            Direction.RIGHT -> Direction.UP
            Direction.UP -> Direction.LEFT
            Direction.LEFT -> Direction.DOWN
        }
    }
}

private fun Journey.draw() {
    val lines = tiles.mapIndexed { row, tilesRow ->
        tilesRow.mapIndexed { column, tile ->
            val pathWithDirection = path.firstOrNull { it.position == row to column }
            when {
                position == row to column -> "x"
                pathWithDirection?.direction == Direction.DOWN -> "v"
                pathWithDirection?.direction == Direction.UP -> "^"
                pathWithDirection?.direction == Direction.LEFT -> "<"
                pathWithDirection?.direction == Direction.RIGHT -> ">"
                else -> tile.symbol
            }
        }
    }
    lines.forEach { println(it.joinToString("")) }
}

fun main() {
//    println(testMap)
//    val lines = testMap.lines()
    val lines = "day10101.txt".pathTo().toFile().readLines()
    val width = lines.first().length
    val linesNormalized = lines.map { line ->
        if (line.length < width) {
            line + " ".repeat(width - line.length)
        } else {
            line
        }
    }
    val tiles = linesNormalized.takeWhile { it.isNotBlank() }.map { line ->
        line.map { symbol ->
            Tile(
                symbol = symbol,
                walkable = symbol == '.',
                isWall = symbol == '#',
                isEmpty = symbol == ' ',
            )
        }
    }

    val pathLine = lines.dropWhile { it.isNotEmpty() }.drop(1).first()
    val path = sequence {
        var part = ""
        pathLine.forEach { ch ->
            if (ch == 'R' || ch == 'L') {
                if (part.isNotEmpty()) {
                    yield(part)
                    part = ""
                }
                yield(ch.toString())
            } else {
                part += ch
            }
        }
        if (part.isNotEmpty()) {
            yield(part)
        }
    }.toList()

    val defaultPosition = tiles.first().indexOfFirst { it.walkable }.let { 0 to it }
    val journey = Journey(
        tiles = tiles,
        position = defaultPosition,
        direction = Direction.RIGHT,
        instructions = ArrayDeque(path),
        path = mutableListOf(PositionWithDirection(defaultPosition, Direction.RIGHT)),
    )
//    println("Initial state")
//    journey.draw()

    while (journey.instructions.isNotEmpty()) {
        println("Next instruction: ${journey.instructions.first()}")
        journey.move()
//        journey.draw()
    }

    val result1 = 1000 * (journey.position.first + 1) + 4 * (journey.position.second + 1) + journey.direction.code
    "The final password is: 1000 * ${journey.position.first + 1} + 4 * ${journey.position.second + 1} + ${journey.direction.code} = $result1".cowsay("day 22")
}