package com.github.ghmulti

fun day01001() {
    val state = "day01001.txt".pathTo().toFile().useLines { lines ->
        lines.fold(SignalState(
            x = 1,
            steps = mutableListOf()
        )) { acc, line ->
            when {
                line == "noop" -> {
                    acc.apply {
                        steps.add(x)
                    }
                }
                line.startsWith("addx") -> {
                    val (_, value) = line.split(" ").let { it.first() to it.last().toInt() }
                    val currentX = acc.x
                    acc.copy(
                        x = currentX + value,
                    ).apply {
                        steps.add(currentX)
                        steps.add(currentX)
                    }
                }
                else -> error("unexpected command")
            }
        }
    }

    val entries = state.steps.mapIndexed { index, e -> index + 1 to e }
        .drop(19).chunked(40).map { it.first().let { it.first *  it.second } }
    "Sum of six signal strengths: ${entries.sum()}".cowsay("day 10")

    val crtLetters = state.steps.mapIndexed { index, x ->
        val crtLineIndex = index % 40
        return@mapIndexed if (crtLineIndex == x - 1 || crtLineIndex == x || crtLineIndex == x + 1) "#" else "."
    }.chunked(40).joinToString("\n") { it.joinToString("") }
    "CRT display: \n$crtLetters".cowsay("day 10")
}

private data class SignalState(
    val x: Int,
    val steps: MutableList<Int>
)