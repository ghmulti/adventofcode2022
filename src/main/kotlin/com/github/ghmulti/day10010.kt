package com.github.ghmulti

import java.util.Random

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

private val rand = Random()

private fun Experiment.assembleRobots(): List<Experiment.() -> Unit> {
    val effects = mutableListOf<Experiment.() -> Unit>()
    if (blueprint.priceForGeodeRobot.enoughMinerals()) {
//        println("Starting construction Geode robot")
        blueprint.priceForGeodeRobot.buyRobot()
        effects.add { geodsRobots += 1 }
        return effects
    }
    if (blueprint.priceForObsidianRobot.enoughMinerals()) {
//        println("Starting construction Obsidian robot")
        blueprint.priceForObsidianRobot.buyRobot()
        effects.add { obsidianRobots += 1 }
    }
    if (blueprint.priceForOreRobot.enoughMinerals()) {
//                println("Starting construction Ore robot")
        blueprint.priceForOreRobot.buyRobot()
        effects.add { oreRobots += 1 }
    }

    if (blueprint.priceForClayRobot.enoughMinerals()) {
//                println("Starting construction Clay robot")
        blueprint.priceForClayRobot.buyRobot()
        effects.add { clayRobots += 1 }
    }
    return effects
}

private fun Experiment.displayState() {
    println("Collected ore=$oreCollected [$oreRobots], clay=$clayCollected [$clayRobots], obsidian=$obsidianCollected [$obsidianRobots], geod=$geodsCollected [$geodsRobots]")
}

private fun runExperiment(blueprint: Blueprint): Int {
    val experiment = Experiment(blueprint)
    while (experiment.hasTime()) {
//        println("=====Minute ${experiment.counter}=====")
        val effects = experiment.assembleRobots()
        experiment.collectMinerals()
//        experiment.displayState()
        experiment.counter += 1
        effects.forEach { it.invoke(experiment) }
    }
    return experiment.geodsCollected
}

fun main() {
    val blueprints = blueprintLines.lines().map { line ->
        Blueprint(
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

    blueprints.map { blueprint ->
        val maxGeods = (1..100000).maxOf {
            runExperiment(blueprint)
        }
        println(maxGeods)
    }
}