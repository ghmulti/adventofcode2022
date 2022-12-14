package com.github.ghmulti

private fun List<String>.toMatrix(): List<List<Int>> {
    return map { line ->
        line.chunked(1).map { it.toInt() }
    }
}

private fun Tree.isVisible(matrix: List<List<Int>>): Boolean {
    if (row == 0 || column == 0 || row == matrix.size-1 || column == matrix[0].size-1 ) {
        return true
    }
    val matrixRow = matrix.map { e -> e[column] }
    val matrixCol = matrix[row]
    return matrixCol.filterIndexed { ind, _ -> ind < column }.all { it < value }
            || matrixCol.filterIndexed { ind, _ -> ind > column }.all { it < value }
            || matrixRow.filterIndexed { ind, _ -> ind < row }.all { it < value }
            || matrixRow.filterIndexed { ind, _ -> ind > row }.all { it < value }
}

private fun List<Int>.score(value: Int): Int = indexOfFirst { it >= value }.takeIf { it != -1 }?.let { it + 1 } ?: size

private fun Tree.calculateScenticScore(matrix: List<List<Int>>): Int {
    val matrixRow = matrix.map { e -> e[column] }
    val matrixCol = matrix[row]
    val p1 = matrixCol.filterIndexed { ind, _ -> ind < column }.reversed().score(value)
    val p2 = matrixCol.filterIndexed { ind, _ -> ind > column }.score(value)
    val p3 = matrixRow.filterIndexed { ind, _ -> ind < row }.reversed().score(value)
    val p4 = matrixRow.filterIndexed { ind, _ -> ind > row }.score(value)
    return p1 * p2 * p3 * p4
}

fun day00111() {
    val matrix = "day00111.txt".pathTo().toFile().readLines().toMatrix()

    val entries = matrix.flatMapIndexed { row, rows ->
        rows.mapIndexedNotNull { column, value ->
            Tree(row, column, value).takeIf { it.isVisible(matrix) }
        }
    }
    "${entries.size} trees are visible on the grid".cowsay("day 8")

    val highestScore = matrix.flatMapIndexed { row, rows ->
        rows.mapIndexed { column, value ->
            Tree(row, column, value).calculateScenticScore(matrix)
        }
    }.maxOf { it }
    "Highest scentic score on the grid = $highestScore".cowsay("day 8")
}

private data class Tree(val row: Int, val column: Int, val value: Int)