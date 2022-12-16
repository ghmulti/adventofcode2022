package com.github.ghmulti

import kotlin.math.abs

private data class Pos(val x: Long, val y: Long)
private data class Area(val sensor: Pos, val distance: Long, val beacon: Pos)

private fun manhattanDistance(p1: Pos, p2: Pos): Long {
    return abs(p1.x - p2.x) + abs(p1.y - p2.y)
}

private fun calculateXForDistance(p1: Pos, y: Long, distance: Long): Pair<Long, Long>? {
    if (abs(p1.y - y) > distance) {
        return null
    }
    val res = distance - abs(p1.y - y)
    val v1 = p1.x - res
    val v2 = p1.x + res
    return minOf(v1, v2) to maxOf(v1, v2)
}

private fun String.mapToSensorAndBeacon(): Pair<Pos, Pos> {
    val poss = split(":").map { s ->
        Pos(
            x = s.substringAfter("x=").substringBefore(", y=").toLong(),
            y = s.substringAfter("y=").toLong(),
        )
    }
    return poss.first() to poss.last()
}

private class Cave(sab: List<Pair<Pos, Pos>>) {
    val minX = sab.flatMap { listOf(it.first, it.second) }.minOf { it.x }
    val maxX = sab.flatMap { listOf(it.first, it.second) }.maxOf { it.x }
    val minY = sab.flatMap { listOf(it.first, it.second) }.minOf { it.y }
    val maxY = sab.flatMap { listOf(it.first, it.second) }.maxOf { it.y }

    val sensors = sab.map { it.first }
    val beacons = sab.map { it.second }

    val areas = sab.map { (sensor, beacon) ->
        val dist = manhattanDistance(sensor, beacon)
        return@map Area(sensor, dist, beacon)
    }

    fun calculatePositionsWithoutBeacon(y: Long): List<Pos> {
        val applicableAreas = areas.filter { area -> y in (area.sensor.y - area.distance .. area.sensor.y + area.distance) }
        val possibleMinX = applicableAreas.minOf { it.sensor.x - it.distance }
        val possibleMaxX = applicableAreas.maxOf { it.sensor.x + it.distance }
//        println("Applicable areas $applicableAreas, possibleMinX=$possibleMinX, possibleMaxX=$possibleMaxX")
        val places = (minOf(possibleMinX, minX)..maxOf(possibleMaxX, maxX)).mapNotNull { x ->
            val coversBySensors = applicableAreas.any { area -> manhattanDistance(Pos(x, y), area.sensor) <= area.distance }
            val coversByBeacons = applicableAreas.any { area -> area.beacon == Pos(x, y) }
            when {
                coversByBeacons -> null
                coversBySensors -> Pos(x, y)
                else -> null
            }
        }
//        println("Diff ${maxOf(possibleMaxX, maxX) - minOf(possibleMinX, minX) - places.size} [${maxOf(possibleMaxX, maxX) - minOf(possibleMinX, minX)}]")
        return places
    }

    private data class LimitState(val from: Long, val to: Long, val missing: Pos? = null)

    fun calculatePositionWithinV2(mxY: Long): Pos {
//        println("Searching within ${maxOf(0, minY)}..${minOf(maxY, mxY)}")
        (maxOf(0, minY)..minOf(maxY, mxY)).forEach { y ->
            val limits = areas.mapNotNull { area -> calculateXForDistance(area.sensor, y, area.distance) }.sortedBy { it.first }
            val limit = limits.fold(LimitState(0,0)) { acc, (x1, x2) ->
                acc.copy(
                    from = minOf(x1, acc.from),
                    to = maxOf(x2, acc.to),
                    missing = if (acc.to < x1) return Pos(acc.to + 1, y) else null
                )
            }
            limit.missing
        }
        error("not found")
    }

    private fun buildMatrix(): List<List<String>> {
        //val rowToCheck = calculatePositionsWithoutBeacon(11)
        return (minY..maxY).map { y ->
            (minX..maxX).map { x ->
                val c = Pos(x, y)
                when {
                    sensors.contains(c) -> "S"
                    //sensorArea.contains(c) -> "#"
                    beacons.contains(c) -> "B"
                    //rowToCheck.contains(c) -> "x"
                    manhattanDistance(Pos(8, 7), c) <= 9 -> "?"
                    else -> "."
                }
            }
        }
    }

    fun drawMatrix() = buildMatrix().forEachIndexed { ind, row -> println(ind.toString().padStart(2, '0') + " " + row.joinToString(" ")) }
}

fun day01110(){
//    val sab = "day01110_test.txt".pathTo().toFile().useLines { lines ->
//        lines.map { line -> line.mapToSensorAndBeacon() }.toList()
//    }
//    val cave = Cave(sab)
//    cave.drawMatrix()
//    val coveredPositionsSample = cave.calculatePositionsWithoutBeacon(10)
//    println("Impossible positions for row 10: ${coveredPositionsSample.size}")
//    val position = cave.calculatePositionWithinV2(20)
//    println("Distress beacon position: $position, tuning frequency ${4000000 * position.x + position.y}")
//    //println(calculateXForDistance(Pos(8,7), 16, 9))

    val sab = "day01110.txt".pathTo().toFile().useLines { lines ->
        lines.map { line -> line.mapToSensorAndBeacon() }.toList()
    }
    val cave = Cave(sab)
    val coveredPositions = cave.calculatePositionsWithoutBeacon(2_000_000)
    "Impossible positions for row 2_000_000: ${coveredPositions.size}".cowsay("day 15")
    val position = cave.calculatePositionWithinV2(4_000_000)
    "Distress beacon position: $position, tuning frequency ${4000000 * position.x + position.y}".cowsay("day 15")
}