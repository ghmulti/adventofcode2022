package com.github.ghmulti

private enum class GameEntry(val score: Int) {
    ROCK(1){
        override fun against(entry: GameEntry): Int = when (entry) {
            PAPER -> 0
            ROCK -> 3
            SCISSORS -> 6
        }
        override fun winloosePair(): Pair<GameEntry, GameEntry> = SCISSORS to PAPER
    },
    PAPER(2) {
        override fun against(entry: GameEntry): Int = when (entry) {
            SCISSORS -> 0
            PAPER -> 3
            ROCK -> 6
        }
        override fun winloosePair(): Pair<GameEntry, GameEntry> = ROCK to SCISSORS
    },
    SCISSORS(3) {
        override fun against(entry: GameEntry): Int = when (entry) {
            ROCK -> 0
            SCISSORS -> 3
            PAPER -> 6
        }
        override fun winloosePair(): Pair<GameEntry, GameEntry> = PAPER to ROCK
    };

    abstract fun against(entry: GameEntry): Int
    abstract fun winloosePair(): Pair<GameEntry, GameEntry>
}

private fun String.toEntry() = when (this) {
    "A", "X" -> GameEntry.ROCK
    "B", "Y" -> GameEntry.PAPER
    "C", "Z" -> GameEntry.SCISSORS
    else -> error("Unknown code")
}

private fun String.parseRow1() = this.split(" ").let { it.first().toEntry() to it.last().toEntry() }

private fun String.calculateFigure(entry: GameEntry): GameEntry = when (this) {
    "X" -> entry.winloosePair().first
    "Y" -> entry
    "Z" -> entry.winloosePair().second
    else -> error("Unknown code")
}

private fun String.parseRow2() = this.split(" ").let {
    val opponentEntry = it.first().toEntry()
    opponentEntry to it.last().calculateFigure(opponentEntry)
}

fun day0010() {
    val yourScore = "day0010.txt".pathTo().toFile().useLines {
        it.sumOf { row ->
            val (opponentEntry, yourEntry) = row.parseRow1()
            //println("$opponentEntry against $yourEntry")
            //println("You get ${yourEntry.score} + ${yourEntry.against(opponentEntry)}")
            yourEntry.score + yourEntry.against(opponentEntry)
        }
    }
    "Your score with assumed strategy in championship is $yourScore".cowsay("day 2")

    val yourScoreWithFixedStrategy = "day0010.txt".pathTo().toFile().useLines {
        it.sumOf { row ->
            val (opponentEntry, yourEntry) = row.parseRow2()
            //println("$opponentEntry against $yourEntry")
            //println("You get ${yourEntry.score} + ${yourEntry.against(opponentEntry)}")
            yourEntry.score + yourEntry.against(opponentEntry)
        }
    }
    "Your score with proper strategy in championship is $yourScoreWithFixedStrategy".cowsay("day 2")
}