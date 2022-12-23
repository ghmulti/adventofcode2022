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
    val path: MutableList<PositionWithDirection>,
    val cubeSize: Int,
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

private fun cubeSide(position: Pair<Int, Int>, cubeSize: Int): Int = when {
    position.first >= 0 && position.first < cubeSize && position.second >= cubeSize && position.second < cubeSize * 2  -> 1
    position.first >= 0 && position.first < cubeSize && position.second >= cubeSize * 2 && position.second < cubeSize * 3 -> 2
    position.first >= cubeSize && position.first < cubeSize * 2 && position.second >= cubeSize && position.second < cubeSize * 2 -> 3
    position.first >= cubeSize * 2 && position.first < cubeSize * 3 && position.second >= 0 && position.second < cubeSize -> 4
    position.first >= cubeSize * 2 && position.first < cubeSize * 3 && position.second >= cubeSize && position.second < cubeSize * 2 -> 5
    position.first >= cubeSize * 3 && position.first < cubeSize * 4 && position.second >= 0 && position.second < cubeSize -> 6
    else -> 100
}

private fun Journey.calculateNewPosition(pwd: PositionWithDirection): PositionWithDirection {
    val (y, x) = pwd.position
    val cubeSide = cubeSide(pwd.position, cubeSize)
    return when (pwd.direction) {
        Direction.DOWN -> {
            val newPosition = pwd.position.first + 1 to pwd.position.second
            val newPositionSide = cubeSide(newPosition, cubeSize)
            if (newPositionSide == cubeSide) {
                pwd.copy(position = newPosition)
            } else {
                when (cubeSide) {
                    2 -> PositionWithDirection( cubeSize + x % cubeSize to cubeSize * 2 - 1, Direction.LEFT)
                    5 -> PositionWithDirection( cubeSize * 3 + x % cubeSize to cubeSize - 1, Direction.LEFT)
                    6 -> PositionWithDirection(0 to cubeSize * 2 + x % cubeSize, Direction.DOWN)

                    1, 3, 4 -> pwd.copy(position = y + 1 to x)
                    else -> error("not expected")
                }
            }
        }
        Direction.UP -> {
            val newPosition = pwd.position.first - 1 to pwd.position.second
            val newPositionSide = cubeSide(newPosition, cubeSize)
            if (newPositionSide == cubeSide) {
                pwd.copy(position = newPosition)
            } else {
                when (cubeSide) {
                    1 -> PositionWithDirection( cubeSize * 3 + x % cubeSize to 0,Direction.RIGHT)
                    2 -> PositionWithDirection( cubeSize * 4 - 1 to x % cubeSize, Direction.UP)
                    4 -> PositionWithDirection( cubeSize + x % cubeSize to cubeSize, Direction.RIGHT)

                    5, 3, 6 -> pwd.copy(position = y - 1 to x)
                    else -> error("not expected")
                }
            }
        }
        Direction.LEFT -> {
            val newPosition = pwd.position.first to pwd.position.second - 1
            val newPositionSide = cubeSide(newPosition, cubeSize)
            if (newPositionSide == cubeSide) {
                pwd.copy(position = newPosition)
            } else {
                when (cubeSide) {
                    1 -> PositionWithDirection( cubeSize * 3 - y % cubeSize - 1 to 0, Direction.RIGHT)
                    3 -> PositionWithDirection(  cubeSize * 2 to y % cubeSize, Direction.DOWN)
                    4 -> PositionWithDirection(cubeSize - y % cubeSize - 1 to cubeSize, Direction.RIGHT)
                    6 -> PositionWithDirection( 0 to cubeSize + y % cubeSize ,Direction.DOWN)

                    5, 2 -> pwd.copy(position = y to x - 1)
                    else -> error("not expected")
                }
            }
        }
        Direction.RIGHT -> {
            val newPosition = pwd.position.first to pwd.position.second + 1
            val newPositionSide = cubeSide(newPosition, cubeSize)
            if (newPositionSide == cubeSide) {
                pwd.copy(position = newPosition)
            } else {
                when (cubeSide) {
                    2 -> PositionWithDirection(cubeSize * 3 - y % cubeSize - 1 to cubeSize * 2 - 1, Direction.LEFT)
                    3 -> PositionWithDirection(cubeSize - 1 to cubeSize * 2 + y % cubeSize, Direction.UP)
                    5 -> PositionWithDirection(cubeSize - y % cubeSize - 1 to cubeSize * 3 - 1, Direction.LEFT)
                    6 -> PositionWithDirection( cubeSize * 3 - 1 to cubeSize + y % cubeSize, Direction.UP)

                    1, 4 -> pwd.copy(position = y to x + 1)
                    else -> error("not expected")
                }
            }
        }
    }
}

context(Journey)
private fun Pair<Int, Int>.change2(length: Int): PositionWithDirection {
    val targetPosition = (1..length).fold(PositionWithDirection(this, direction)) { pwd, _ ->
        val newPositionWithDirection = calculateNewPosition(pwd)
        if (tiles[newPositionWithDirection.position.first][newPositionWithDirection.position.second].isWall) {
            return@fold pwd
        }
        path.add(newPositionWithDirection)
//        println("Moving from $pwd to $newPositionWithDirection [current side ${cubeSide(pwd.position, cubeSize)}] -> [target side ${cubeSide(newPositionWithDirection.position, cubeSize)}]")
        newPositionWithDirection
    }
    return targetPosition
}

private fun Journey.move(isPart1: Boolean = true) {
    val instruction = instructions.removeFirst()
    when {
        instruction.toIntOrNull() != null -> {
            if (isPart1) {
                position = position.change(instruction.toInt())
            } else {
                val (newPosition, newDirection) = position.change2(instruction.toInt())
                position = newPosition
                direction = newDirection
            }
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

private fun List<List<Tile>>.rotate90(): List<List<Tile>> = (indices).map { ind -> reversed().map { it[ind] } }

fun day10101() {
    val (lines, cubeSize) = "day10101.txt".pathTo().toFile().readLines() to 50
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
        cubeSize = cubeSize
    )
//    println("Initial state")
//    journey.draw()

    while (journey.instructions.isNotEmpty()) {
//        println("Next instruction: ${journey.instructions.first()}")
        journey.move()
//        journey.draw()
    }

    val result1 = 1000 * (journey.position.first + 1) + 4 * (journey.position.second + 1) + journey.direction.code
    "The final password is: 1000 * ${journey.position.first + 1} + 4 * ${journey.position.second + 1} + ${journey.direction.code} = $result1".cowsay("day 22")

    val journey2 = Journey(
        tiles = tiles,
        position = defaultPosition,
        direction = Direction.RIGHT,
        instructions = ArrayDeque(path),
        path = mutableListOf(PositionWithDirection(defaultPosition, Direction.RIGHT)),
        cubeSize = cubeSize,
    )

//    println("Initial state")
//    journey2.draw()

    while (journey2.instructions.isNotEmpty()) {
//        println("Next instruction: ${journey2.instructions.first()}")
        journey2.move(false)
//        journey2.draw()
    }

    val result2 = 1000 * (journey2.position.first + 1) + 4 * (journey2.position.second + 1) + journey2.direction.code
    "The final password is: 1000 * ${journey2.position.first + 1} + 4 * ${journey2.position.second + 1} + ${journey2.direction.code} = $result2".cowsay("day 22")

    // 150699 - too high, 78489 - too low, 129472 - too low

}
