package com.github.ghmulti

import java.math.BigInteger

private fun defineMonkeys(): Sequence<Monkey> = sequence {
    "day1011_test.txt".pathTo().toFile().useLines { lines ->
        val monkeys = lines.chunked(7).map { monkeyLines ->
            Monkey(
                worryLevels = monkeyLines[1].substringAfter("Starting items:").split(",").map { it.trim().toLong() }.let { ArrayDeque(it) },
                operation = { old ->
                    when {
                        monkeyLines[2].contains("*") -> {
                            val secondArg = if (monkeyLines[2].substringAfter("*").contains("old")) old else monkeyLines[2].substringAfter("*").trim().toLong()
                            old * secondArg
                        }
                        monkeyLines[2].contains("+") -> {
                            val secondArg = if (monkeyLines[2].substringAfter("+").contains("old")) old else monkeyLines[2].substringAfter("+").trim().toLong()
                            old + secondArg
                        }
                        else -> error("unexpected")
                    }
                },
                test = { level ->
                    val divisibleBy = monkeyLines[3].substringAfter("divisible by").trim().toInt()
                    val targetIndex = if (level % divisibleBy == 0L) 4 else 5
                    monkeyLines[targetIndex].substringAfter("throw to monkey").trim().toInt()
                },
                dividableBy = monkeyLines[3].substringAfter("divisible by").trim().toInt(),
                counter = 0,
            )
        }
        yieldAll(monkeys)
    }
}

private data class Monkey(
    val worryLevels: ArrayDeque<Long>,
    var counter: Long,
    val operation: (old: Long) -> Long,
    val test: (level: Long) -> NewMonkeyIndex,
    val dividableBy: Int,
)

typealias NewMonkeyIndex = Int

fun main() {
//    val result = (1..20).fold(defineMonkeys().toList()) { monkeys, round ->
//        monkeys.forEachIndexed { index, monkey ->
//            while (monkey.worryLevels.isNotEmpty()) {
//                val level = monkey.worryLevels.removeFirst()
//                val newLevel = (monkey.operation(level) / 3)
//                val targetMonkey = monkey.test(newLevel)
//                //println("Monkey[$index] targetMonkey for newLevel=$newLevel is $targetMonkey [level=$level]")
//                monkeys[targetMonkey].worryLevels.addLast(newLevel)
//                monkey.counter += 1
//            }
//        }
//        //println("Worry levels after round $round")
//        //monkeys.map { it.worryLevels }.forEach { println(it) }
//        monkeys
//    }
//    val monkeyBusinessLevel = result.map { it.counter }.sortedDescending().take(2).let { it.first() * it.last() }
//    "Level of monkey business $monkeyBusinessLevel".cowsay("day 11")

    val result2 = (1..1000).fold(defineMonkeys().toList()) { monkeys, round ->
        monkeys.forEachIndexed { index, monkey ->
            val targetMonkeys = mutableListOf<Int>()
            while (monkey.worryLevels.isNotEmpty()) {
                val level = monkey.worryLevels.removeFirst()
                val newLevel = monkey.operation(level)
                val targetMonkey = monkey.test(newLevel)
                //println("Monkey[$index] targetMonkey for newLevel=$newLevel is $targetMonkey [level=$level]")
                monkeys[targetMonkey].worryLevels.addLast(newLevel)
                monkey.counter += 1
                targetMonkeys.add(targetMonkey)
            }
            println("Monkey[$index]: $targetMonkeys")
        }
        println("Round $round ===")
        if (round == 1000 || round == 1 || round == 20 ) {
            //println("${monkeys.map { it.counter }}")
        }
        //println("Worry levels after round $round")
        //monkeys.map { it.worryLevels }.forEach { println(it) }
        monkeys
    }
    val monkeyBusinessLevel2 = result2.map { it.counter }.sortedDescending().take(2).let { it.first() * it.last() }
    "Level of monkey business $monkeyBusinessLevel2".cowsay("day 11")
}