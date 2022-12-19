package com.github.ghmulti

private val blueprintLines = """
Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 2 ore. Each obsidian robot costs 3 ore and 14 clay. Each geode robot costs 2 ore and 7 obsidian.
Blueprint 2: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 8 clay. Each geode robot costs 3 ore and 12 obsidian.
""".trimIndent()

private data class Price(
    val ore: Int = 0,
    val clay: Int = 0,
    val obsidian: Int = 0,
)

private data class Blueprint(
    val id: Int,
    val priceForOreRobot: Price,
    val priceForClayRobot: Price,
    val priceForObsidianRobot: Price,
    val priceForGeodeRobot: Price,
)

context(Experiment)
private fun Price.enoughMinerals(): Boolean {
    return oreCollected >= ore && clayCollected >= clay && obsidianCollected >= obsidian
}

context(Experiment)
private fun Price.buyRobot() {
    oreCollected -= ore
    clayCollected -= clay
    obsidianCollected -= obsidian
}

private data class Experiment(
    val blueprint: Blueprint,
    var counter: Int = 1,
    var oreRobots: Int = 1,
    var oreCollected: Int = 0,
    var clayRobots: Int = 0,
    var clayCollected: Int = 0,
    var obsidianRobots: Int = 0,
    var obsidianCollected: Int = 0,
    var geodsRobots: Int = 0,
    var geodsCollected: Int = 0,
)

private fun Experiment.hasTime(): Boolean = counter <= 24

private fun Experiment.collectMinerals() {
    oreCollected += oreRobots
    clayCollected += clayRobots
    obsidianCollected += obsidianRobots
    geodsCollected += geodsRobots
}

private data class Effect(val before: Experiment.() -> Unit, val after: Experiment.() -> Unit)

private fun Experiment.assembleRobotsDecisions(): List<Effect> {
    if (blueprint.priceForGeodeRobot.enoughMinerals()) {
        return listOf(Effect(before = { blueprint.priceForGeodeRobot.buyRobot() }, after = { geodsRobots += 1 }))
    }
    val decisions = mutableListOf(Effect(before = {}, after = {}))
    if (blueprint.priceForObsidianRobot.enoughMinerals()) {
        decisions.add(Effect(before = { blueprint.priceForObsidianRobot.buyRobot() }, after = { obsidianRobots += 1 }))
    }
    if (blueprint.priceForOreRobot.enoughMinerals()) {
        decisions.add(Effect(before = { blueprint.priceForOreRobot.buyRobot() }, after = { oreRobots += 1 }))
    }
    if (blueprint.priceForClayRobot.enoughMinerals()) {
        decisions.add(Effect(before = { blueprint.priceForClayRobot.buyRobot() }, after = { clayRobots += 1 }))
    }
    return decisions
}

private fun Experiment.displayState() {
    println("Collected ore=$oreCollected [$oreRobots], clay=$clayCollected [$clayRobots], obsidian=$obsidianCollected [$obsidianRobots], geod=$geodsCollected [$geodsRobots]")
}

private fun runExperiment(experiment: Experiment): Experiment {
    val stack = ArrayDeque(listOf(experiment))
    val calculated = mutableMapOf<Int, Experiment>()
    val checked = mutableSetOf<Experiment>()
    while (stack.isNotEmpty()) {
        val top = stack.removeFirst()
        if (top.hasTime()) {
            val modifiedExperiment = top.assembleRobotsDecisions().map { (before, after) ->
                val localExperiment = top.copy()
                localExperiment.before()
                localExperiment.collectMinerals()
                localExperiment.counter += 1
                localExperiment.after()
                localExperiment
            }
                .filter { exp ->
                    !checked.contains(exp)
                }

            modifiedExperiment.forEach {
                stack.addFirst(it)
                calculated[it.counter] = it.takeIf { it.geodsCollected >= (calculated[it.counter]?.geodsCollected ?: 0) } ?: calculated.getValue(it.counter)
                checked.add(it)
            }
        }
    }
    return calculated.getValue(25)
}

fun main() {
    val bpLines = "day10010.txt".pathTo().toFile().readLines()
//    val bpLines = blueprintLines.lines()
    val blueprints = bpLines.map { line ->
        Blueprint(
            id = line.substringAfter("Blueprint ").substringBefore(":").trim().toInt(),
            priceForOreRobot = Price(
                ore = line.substringAfter("Each ore robot costs").substringBefore("ore").trim().toInt(),
            ),
            priceForClayRobot = Price(
                ore = line.substringAfter("Each clay robot costs").substringBefore("ore").trim().toInt(),
            ),
            priceForObsidianRobot = Price(
                ore = line.substringAfter("Each obsidian robot costs").substringBefore("ore").trim().toInt(),
                clay = line.substringAfter("Each obsidian robot costs").substringAfter("and").substringBefore("clay").trim().toInt()
            ),
            priceForGeodeRobot = Price(
                ore = line.substringAfter("Each geode robot costs").substringBefore("ore").trim().toInt(),
                obsidian = line.substringAfter("Each geode robot costs").substringAfter("and").substringBefore("obsidian").trim().toInt()
            )
        )
    }

//    blueprints.forEach { println(it) }

    val qualityLevels = blueprints.map { blueprint ->
        val bestExperiment = runExperiment(Experiment(blueprint))
        println("Max geods for ${blueprint.id}: ${bestExperiment.geodsCollected}")
        blueprint.id * bestExperiment.geodsCollected
    }
    println("Quality levels: $qualityLevels, sum = ${qualityLevels.sum()}")
}