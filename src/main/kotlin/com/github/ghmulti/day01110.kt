package com.github.ghmulti

import kotlin.math.abs

private data class Pos(val x: Long, val y: Long)
private data class Area(val sensor: Pos, val distance: Long, val beacon: Pos)

private fun manhattanDistance(p1: Pos, p2: Pos): Long {
    return abs(p1.x - p2.x) + abs(p1.y - p2.y)
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
        println("Applicable areas $applicableAreas, possibleMinX=$possibleMinX, possibleMaxX=$possibleMaxX")
        val places = (minOf(possibleMinX, minX)..maxOf(possibleMaxX, maxX)).mapNotNull { x ->
            val coversBySensors = applicableAreas.any { area -> manhattanDistance(Pos(x, y), area.sensor) <= area.distance }
            val coversByBeacons = applicableAreas.any { area -> area.beacon == Pos(x, y) }
            when {
                coversByBeacons -> null
                coversBySensors -> Pos(x, y)
                else -> null
            }
        }
        println("Diff ${maxOf(possibleMaxX, maxX) - minOf(possibleMinX, minX) - places.size} [${maxOf(possibleMaxX, maxX) - minOf(possibleMinX, minX)}]")
        return places
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

fun main(){
    val sab = "day01110.txt".pathTo().toFile().useLines { lines ->
        lines.map { line -> line.mapToSensorAndBeacon() }.toList()
    }
    //println(sab)
    val cave = Cave(sab)
    println("MinX=${cave.minX},MaxX=${cave.maxX},MinY=${cave.minY},MaxY=${cave.maxY}")
//    cave.drawMatrix()
//    val coveredPositionsSample = cave.calculatePositionsWithoutBeacon(10)
//    println("Impossible positions for row: ${coveredPositionsSample.size}")
    val coveredPositions = cave.calculatePositionsWithoutBeacon(2_000_000)
    println("Impossible positions for row: ${coveredPositions.size}") // 5508234
}