package com.github.ghmulti

private data class ExpState(
    val expressions: MutableList<String>,
    val partialExpression: MutableList<Char>,
    var nestedCounter: Int,
)

private enum class Res {
    RIGHT, CHECK, NOT_RIGHT,
}

private fun String.splitExp(): List<String> = toList().fold(ExpState(mutableListOf(), mutableListOf(), 0)) { exp, ch ->
    if (ch == ',' && exp.nestedCounter == 0) {
        exp.expressions.add(exp.partialExpression.joinToString(""))
        exp.partialExpression.clear()
    } else {
        if (ch == '[') {
            exp.nestedCounter += 1
        }
        if (ch == ']') {
            exp.nestedCounter -= 1
        }
        exp.partialExpression.add(ch)
    }
    exp
}.apply {
    expressions.add(partialExpression.joinToString(""))
}.expressions.toList()

private fun compare(left: String, right: String): Res {
    val elements1 = left.substringAfter("[").substringBeforeLast("]").splitExp()
    val elements2 = right.substringAfter("[").substringBeforeLast("]").splitExp()
    (1..(maxOf(elements2.size, elements1.size))).forEach { index ->
        val el1 = elements1.getOrNull(index-1) ?: return Res.RIGHT
        val el2 = elements2.getOrNull(index-1) ?: return Res.NOT_RIGHT

        if (el1.isEmpty() || el2.isEmpty()) {
            if (el1.isEmpty() && el2.isEmpty()) {
                return@forEach
            }
            return Res.RIGHT.takeIf { el1.isEmpty() } ?: Res.NOT_RIGHT
        }

        if (el1.toIntOrNull() != null && el2.toIntOrNull() != null) {
            if (el2.toInt() == el1.toInt()) {
                return@forEach
            }
            return Res.RIGHT.takeIf { el2.toInt() > el1.toInt() } ?: Res.NOT_RIGHT
        }

        if (el1.toIntOrNull() == null && el2.toIntOrNull() == null) {
            val tmp = compare(el1, el2)
            if (tmp == Res.CHECK) {
                return@forEach
            }
            return tmp
        }

        // type mismatch
        if (el1.contains("[") && !el2.contains("[")) {
            val tmp = compare(el1, "[$el2]")
            if (tmp == Res.CHECK) {
                return@forEach
            }
            return tmp
        }

        if (el2.contains("[") && !el1.contains("[")) {
            val tmp = compare("[$el1]", el2)
            if (tmp == Res.CHECK) {
                return@forEach
            }
            return tmp
        }
    }
    return Res.CHECK
}

fun day01100() {
    "day01100.txt".pathTo().toFile().useLines { lines ->
        val windowed = lines.windowed(size = 2, step = 3).toList()
        val packets = windowed.mapIndexed { index, pairs ->
            when (compare(pairs.first(), pairs.last())) {
                Res.RIGHT -> index + 1
                Res.NOT_RIGHT -> 0
                else -> error("Unknown for $pairs")
            }
        }
        "Sum of indices of right pairs: ${packets.sum()}".cowsay("day 13")

        val packetWrappers = (windowed.flatten() + listOf(DIV_1, DIV_2)).sortedWith { a, b -> when (compare(b, a)) {
            Res.RIGHT -> 1
            Res.NOT_RIGHT -> -1
            Res.CHECK -> 0
        }}
        val decoderKey = (packetWrappers.indexOf(DIV_1)+1) * (packetWrappers.indexOf(DIV_2)+1)
        "Decoder key: $decoderKey".cowsay("day 13")
    }
}

private const val DIV_1 = "[[2]]"
private const val DIV_2 = "[[6]]"
