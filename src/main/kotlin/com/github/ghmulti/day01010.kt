package com.github.ghmulti

private fun defineMonkeys(): Sequence<Monkey> = sequence {
    "day01010.txt".pathTo().toFile().useLines { lines ->
        val monkeys = lines.chunked(7).map { monkeyLines ->
            Monkey(
                worryLevels = monkeyLines[1].substringAfter("Starting items:").split(",").map { it.trim().toLong() }.let { ArrayDeque(it) },
                operation = { old ->
                    when {
                        monkeyLines[2].contains("*") -> {
                            val containsOld = monkeyLines[2].substringAfter("*").contains("old")
                            (old * old).takeIf { containsOld } ?: (old * monkeyLines[2].substringAfter("*").trim().toLong())
                        }
                        monkeyLines[2].contains("+") -> {
                            val containsOld = monkeyLines[2].substringAfter("+").contains("old")
                            (old + old).takeIf { containsOld } ?: (old + monkeyLines[2].substringAfter("+").trim().toLong())
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

fun day01010() {
    val result = (1..20).fold(defineMonkeys().toList()) { monkeys, round ->
        monkeys.forEach { monkey ->
            while (monkey.worryLevels.isNotEmpty()) {
                val level = monkey.worryLevels.removeFirst()
                val newLevel = (monkey.operation(level) / 3)
                val targetMonkey = monkey.test(newLevel)
                monkeys[targetMonkey].worryLevels.addLast(newLevel)
                monkey.counter += 1
            }
        }
        monkeys
    }
    val monkeyBusinessLevel = result.map { it.counter }.sortedDescending().take(2).let { it.first() * it.last() }
    "Level of monkey business $monkeyBusinessLevel".cowsay("day 11")

    val result2 = (1..10_000).fold(defineMonkeys().toList()) { monkeys, round ->
        val dd = monkeys.fold(1) { acc, monk -> acc * monk.dividableBy}
        monkeys.forEach { monkey ->
            while (monkey.worryLevels.isNotEmpty()) {
                val level = monkey.worryLevels.removeFirst()
                val newLevel = monkey.operation(level) % dd
                val targetMonkey = monkey.test(newLevel)
                monkeys[targetMonkey].worryLevels.addLast(newLevel)
                monkey.counter += 1
            }
        }
        monkeys
    }
    val monkeyBusinessLevel2 = result2.map { it.counter }.sortedDescending().take(2).let { it.first() * it.last() }
    "Level of monkey business v2 $monkeyBusinessLevel2".cowsay("day 11")
}