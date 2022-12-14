package com.github.ghmulti

@Suppress("unused")
private val sample = """
498,4 -> 498,6 -> 496,6
503,4 -> 502,4 -> 502,9 -> 494,9
""".trimIndent()

private data class Coord(val x: Int, val y: Int)
private val SAND_LATCH = Coord(500, 0)

private class Slice(
    val paths: List<List<Coord>>,
    val minX: Int = paths.flatten().minOf { it.x },
    val maxX: Int = paths.flatten().maxOf { it.x },
    val maxY: Int = paths.flatten().maxOf { it.y },
) {

    val sand = mutableListOf<Coord>()

    val stones = paths.flatMap { path: List<Coord> ->
        path.windowed(size = 2, step = 1).flatMap { lst ->
            val c1 = lst.first()
            val c2 = lst.last()
            (minOf(c1.x, c2.x) .. maxOf(c1.x, c2.x)).flatMap { x ->
                (minOf(c1.y, c2.y) .. maxOf(c1.y, c2.y)).map { y ->
                    Coord(x, y)
                }
            }
        }
    }

    fun buildMatrix(): List<List<String>> {
        return (0..maxY).map { y ->
            (minX..maxX).map { x ->
                val c = Coord(x, y)
                when {
                    stones.contains(c) -> "#"
                    sand.contains(c) -> "o"
                    c == SAND_LATCH -> "+"
                    else -> "."
                }
            }
        }
    }
}

context(Slice)
private fun Coord.withinSlice(): Boolean {
    return x in minX..maxX && y in 0 .. maxY && this != SAND_LATCH
}

private fun Slice.isFree(cord: Coord): Boolean = !stones.contains(cord) && !sand.contains(cord)

context(Slice)
private fun Coord.move(): Coord = when {
    isFree(Coord(x, y+1)) -> Coord(x, y+1) // bottom
    isFree(Coord(x-1, y+1)) -> Coord(x-1, y+1) // left bottom
    isFree(Coord(x+1, y+1)) -> Coord(x+1, y+1) // right bottom
    else -> this
}

private fun Slice.dropSand(): Coord? {
    var start = SAND_LATCH.copy()
    while (true) {
        val nextPosition = start.move()
        when {
            nextPosition == start -> return nextPosition.takeIf { c -> c.withinSlice() }?.also { sand.add(it) }
            !nextPosition.withinSlice() -> return null
            else -> start = nextPosition // continue
        }
    }
}

@Suppress("unused")
private fun Slice.drawMatrix() = buildMatrix().forEach { println(it.joinToString("")) }

fun day01101() {
    //val src = sample.lines()
    val src = "day01101.txt".pathTo().toFile().readLines()
    val paths = src.map { line ->
        line.split(" -> ").map { coord -> coord.split(",").let { c -> Coord(c.first().toInt(), c.last().toInt()) } }
    }
    val slice = Slice(paths)
    while (slice.dropSand() != null) {
        //slice.drawMatrix()
    }
    //slice.drawMatrix()
    "${slice.sand.size} units of sand come to rest before sand starts flowing into the abyss below".cowsay("day 14") // 1072

    //part2(paths)
}

@Suppress("unused")
private fun part2(paths: List<List<Coord>>) {
    // takes looooooong time
    val maxY: Int = paths.flatten().maxOf { it.y } + 2
    val minX: Int = paths.flatten().minOf { it.x }
    val maxX: Int = paths.flatten().maxOf { it.x }
    val length = maxX - minX
    val slice2 = Slice(paths = paths + listOf(listOf(Coord(SAND_LATCH.x - maxOf(length, maxY), maxY), Coord(SAND_LATCH.x + maxOf(length, maxY), maxY))))
    while (slice2.dropSand() != null) {
        //slice.drawMatrix()
    }
    //slice.drawMatrix()
    "${slice2.sand.size + 1} units of sand come to rest before the source of the sand becomes blocked".cowsay("day 14") // 24659
}