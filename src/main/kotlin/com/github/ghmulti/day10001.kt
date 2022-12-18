package com.github.ghmulti

@Suppress("unused")
private val testCoords = """
2,2,2
1,2,2
3,2,2
2,1,2
2,3,2
2,2,1
2,2,3
2,2,4
2,2,6
1,2,5
3,2,5
2,1,5
2,3,5
""".trimIndent()

private data class Cube(val x: Int, val y: Int, val z: Int) {
    val vertexes: Set<Triple<Int, Int, Int>>
        get() = setOf(
            Triple(x, y, z),
            Triple(x+1, y, z),
            Triple(x, y+1, z),
            Triple(x+1, y+1, z),
            Triple(x, y, z+1),
            Triple(x+1, y, z+1),
            Triple(x, y+1, z+1),
            Triple(x+1, y+1, z+1),
        )
}

fun day10001() {
    val lines = "day10001.txt".pathTo().toFile().readLines()
    //val testlines = testCoords.lines()
    val cubes = lines.map { line ->
        val coords = line.split(",")
        Cube(coords[0].toInt(), coords[1].toInt(), coords[2].toInt())
    }

    val notCoveredSides = cubes.map { cube ->
        val adjacent = cubes.filter { it != cube }.count { it.vertexes.intersect(cube.vertexes).size == 4 }
        6 - adjacent
    }
    "${notCoveredSides.sum()} is the exterior surface area of your scanned lava droplet".cowsay("day 18")

    // bfs to find all surrounding cubes [obsidian x,y,z are less than 20]
    val startingCube = Cube(20, 20, 20)
    val queue = mutableListOf(startingCube)
    val visited = mutableSetOf<Cube>()
    while (queue.isNotEmpty()) {
        val first = queue.removeFirst()

        val nextOptions = listOf(
            Cube(1, 0, 0),
            Cube(-1, 0, 0),
            Cube(0, 1, 0),
            Cube(0, -1, 0),
            Cube(0, 0, 1),
            Cube(0, 0, -1),
        ).map { (x, y, z) ->
            Cube(first.x + x, first.y + y, first.z + z)
        }.filter { (x, y, z) ->
            // out of bound
            x in (-1..20) && y in (-1..20) && z in (-1..20)
        }.filter { t ->
            // visited
            !visited.contains(t)
        }.filter { t ->
            // avoid surface
            !cubes.contains(t)
        }

        queue.addAll(nextOptions)
        visited.addAll(nextOptions)
    }
    println("Found ${visited.size} cubes surrounding obsidian")

    // Calculate adjacent flats
    val adjacentFlats = cubes.map { cube ->
        listOf(
            cube.vertexes.groupBy { it.first }, // x-flats
            cube.vertexes.groupBy { it.second }, // y-flats
            cube.vertexes.groupBy { it.third }, // z-flats
        )
            .flatMap { map -> map.values.map { it.toSet() }}
            .count { slices ->
                // check whether any flats of visited outer cubes are adjacent to obsidian flats
                visited.any { v -> v.vertexes.intersect(slices).size == 4 }
            }
    }
    "${adjacentFlats.sum()} is the exterior surface area of your scanned lava droplet".cowsay("day 18")
}