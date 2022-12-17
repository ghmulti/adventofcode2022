package com.github.ghmulti

import java.util.Random


private data class Vertex(val code: String, val rate: Int, val leadsTo: List<String>)

private fun List<String>.parseVertexes(): List<Vertex> {
    return map { line ->
        Vertex(
            code = line.substringAfter("Valve ").take(2),
            rate = line.substringAfter("rate=").substringBefore(";").toInt(),
            leadsTo = line.substringAfter("to valve").drop(1).split(",").map { it.trim() }
        )
    }
}

private data class Volcano(
    var position: Vertex,
    val vertexes: List<Vertex>,
    val openedVertexes: MutableList<Vertex>,
    var timeLeft: Int,
    val queue: ArrayDeque<Vertex>,
    val selectedVertexes: MutableList<Vertex> = mutableListOf(),
)

private data class Evaluation(val target: Vertex, val pathTo: List<Vertex>, val evaluation: Int)
private data class VertexWithPath(val vertex: Vertex, val path: List<Vertex>)

private fun Volcano.shortest(source: Vertex, target: Vertex): VertexWithPath {
    val queue = mutableListOf(VertexWithPath(source, emptyList()))
    val visited = mutableSetOf<Vertex>()
    while (queue.isNotEmpty()) {
        val first = queue.removeFirst()
        if (first.vertex == target) {
            return first
        }
        val filteredOptions = first.vertex.leadsTo
            .map { leadsTo -> vertexes.first { v -> v.code == leadsTo } }
            .filter { !visited.contains(it) }
            .shuffled()

        queue.addAll(filteredOptions.map { VertexWithPath(it, first.path + listOf(it)) })
        visited.addAll(filteredOptions)
    }
    error("not found")
}

val random = Random()

private fun Volcano.evaluate(): List<Evaluation> {
    val chooseFrom = vertexes.filter { !openedVertexes.contains(it) }
//    val average = chooseFrom.map { it.rate }.average().toInt().takeIf { it > 0 } ?: 1
    return chooseFrom.map { vertex ->
        val shortest = shortest(source = position, target = vertex)
        Evaluation(
            target = vertex,
            pathTo = shortest.path + listOf(vertex), // one extra for opening
            //evaluation = vertex.rate * (timeLeft - shortest.path.size),
            evaluation = if (vertex.rate == 0) 0 else random.nextInt(vertex.rate) * (timeLeft - shortest.path.size) / shortest.path.size, // -> 1862 !!!!!!!!
            //evaluation = if (vertex.rate == 0) 0 else random.nextInt(vertex.rate) * (timeLeft - shortest.path.size), // -> 1855
            //evaluation = (vertex.rate+1) * (timeLeft - shortest.path.size),
        )
    }.sortedByDescending { it.evaluation }
}

private fun Volcano.move() {
    if (queue.isEmpty()) {
        val evaluation = evaluate()
        if (evaluation.isNotEmpty()) {
            val nextTarget = evaluation.firstOrNull { !selectedVertexes.contains(it.target) } ?: evaluation.first()
            selectedVertexes.add(nextTarget.target)
//            println("New target ${nextTarget.target} [path=${nextTarget.pathTo.map { it.code }}] [$timeLeft]")
            queue.addAll(nextTarget.pathTo)
        }
    }
    if (queue.isNotEmpty()) {
        val nextStep = queue.removeFirst()
        position = nextStep
        if (queue.isEmpty()) {
//            println("Opening valve $nextStep")
            openedVertexes.add(nextStep)
        } else {
//            println("Moving to $nextStep [$timeLeft]")
        }
    }
}

private fun Volcano.hasTime(): Boolean = timeLeft > 0

fun day01111() {
    val lines = "day01111.txt".pathTo().toFile().readLines()
    val vertexes = lines.parseVertexes().sortedBy { it.rate }
    //vertexes.forEach { println(it) }

    part1(vertexes) // -> 1862
    part2(vertexes)   // -> 2422
}

private fun part2(vertexes: List<Vertex>) {
    var max = 0
    (1..1000).forEach { _ ->
        val openedVertexes = mutableListOf<Vertex>()
        val selectedVertexes = mutableListOf<Vertex>()
        var score = 0
        val elephant = Volcano(
            position = vertexes.first { it.code == "AA" },
            vertexes = vertexes,
            openedVertexes = openedVertexes,
            timeLeft = 26,
            queue = ArrayDeque(),
            selectedVertexes = selectedVertexes,
        )
        val you = Volcano(
            position = vertexes.first { it.code == "AA" },
            vertexes = vertexes,
            openedVertexes = openedVertexes,
            timeLeft = 26,
            queue = ArrayDeque(),
            selectedVertexes = selectedVertexes,
        )
        while (you.hasTime()) {
            val rateSum = openedVertexes.sumOf { it.rate }
            score += rateSum
            you.move()
            elephant.move()
            you.timeLeft -= 1
        }
        if (score > max) {
            max = score
//            println(max)
        }
    }

    "$max is the most pressure you can release with elephant for 26 min".cowsay("day 16")
}

private fun part1(vertexes: List<Vertex>) {
    var max = 0
    (1..5000).forEach { _ ->
        var score = 0
        val volcano = Volcano(
            position = vertexes.first { it.code == "AA" },
            vertexes = vertexes,
            openedVertexes = mutableListOf(),
            timeLeft = 30,
            queue = ArrayDeque(),
        )
        //println(volcano)

        while (volcano.hasTime()) {
            val rateSum = volcano.openedVertexes.sumOf { it.rate }
            score += rateSum
//            println("=====[${volcano.timeLeft}]=====")
//            println("Releasing $rateSum pressure [${volcano.timeLeft}] [score=${volcano.score}]")
            volcano.move()
            volcano.timeLeft -= 1
        }

        //println(volcano.score)
        if (score > max) {
            max = score
            //println(max)
        }
    }

    "$max is the most pressure you can release for 30 min".cowsay("day 16")
//    println(max)
}