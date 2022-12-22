package com.github.ghmulti

private val monkeyTest = """
root: pppw + sjmn
dbpl: 5
cczh: sllz + lgvd
zczc: 2
ptdq: humn - dvpt
dvpt: 3
lfqf: 4
humn: 5
ljgn: 2
sjmn: drzm * dbpl
sllz: 4
pppw: cczh / lfqf
lgvd: ljgn * ptdq
drzm: hmdt - zczc
hmdt: 32
""".trimIndent()

private data class Yelling(var value: String)

private fun List<String>.parseMonkeys(): MutableMap<String, Yelling> = map { line ->
    val key = line.substringBefore(":")
    val value = line.substringAfter(": ").trim()
    key to Yelling(value)
}.associate { it }.toMutableMap()

private fun Map<String, Yelling>.rootResolved(): Boolean = getValue("root").value.toLongOrNull() != null

private data class Expression(val one: String, val two: String, val operation: (one: Long, two: Long) -> Long)

private fun String.parseExpression(): Expression {
    return Expression(
        one = split(" ").first().trim(),
        two = split(" ").last().trim(),
        operation = { a, b ->
            when (split(" ")[1].trim()) {
                "+" -> a + b
                "-" -> a - b
                "*" -> a * b
                "/" -> a / b
                "==" -> if (a == b) 1 else 0
                else -> error("not expected")
            }
        }
    )
}

fun day10100() {
    val monkeys = "day10100.txt".pathTo().toFile().readLines().parseMonkeys()
//    val monkeys = monkeyTest.lines().parseMonkeys()
//    println(monkeys)

    while (!monkeys.rootResolved()) {
        val iterator = monkeys.iterator()
        while (iterator.hasNext()) {
            val mnk = iterator.next()
            if (mnk.value.value.toLongOrNull() != null) {
                continue
            }
            val (one, two, exp) = mnk.value.value.parseExpression()
            val oneParsed = monkeys.getValue(one).value.toLongOrNull()
            val twoParsed = monkeys.getValue(two).value.toLongOrNull()
            if (oneParsed == null || twoParsed == null) {
                continue
            }
            val newValue = exp(oneParsed, twoParsed)
//            println("Replacing ${mnk.value.value} to $newValue [one=$one=$oneParsed, two=$two=$twoParsed]")
            mnk.value.value = newValue.toString()
        }
    }

    "root monkey yells ${monkeys.getValue("root").value}".cowsay("day 21")

    val monkeys2 = "day10100.txt".pathTo().toFile().readLines().parseMonkeys()
//    val monkeys2 = monkeyTest.lines().parseMonkeys()
    monkeys2["humn"] = Yelling("???")
    val exp = monkeys2.getValue("root").value.replace("+", "==")
    val simplified = simplify(exp, monkeys2)
//    println(simplified.replace("???", "x").replace("==", "="))
    val result = calculate(simplified)
//    println(result.replace("???", "x").replace("==", "="))
    val result2 = calculate2(result)
//    println(result2.replace("???", "x").replace("==", "="))
    val result3 = calculate3(result2)
    "What number do you yell to pass root's equality test? $result3".cowsay("day 21")
    // 3560324848168
}

private data class ExpressionState(
    val expressions: MutableList<String>,
    val partialExpression: MutableList<Char>,
    var nestedCounter: Int,
)

private fun String.simplifyByParentheses(): List<String> = toList().fold(ExpressionState(mutableListOf(), mutableListOf(), 0)) { exp, ch ->
    if (exp.partialExpression.isNotEmpty() && exp.nestedCounter == 0) {
        exp.expressions.add(exp.partialExpression.joinToString(""))
        exp.partialExpression.clear()
    } else {
        if (ch == '(' && exp.partialExpression.isNotEmpty()) {
            exp.partialExpression.clear()
            exp.nestedCounter = 0
        }
        if (ch == '(') {
            exp.nestedCounter += 1
        }
        if (ch == ')') {
            exp.nestedCounter -= 1
        }
        if (exp.nestedCounter > 0 || ch == ')' || ch == '(') {
            exp.partialExpression.add(ch)
        }
    }
    exp
}.apply {
    expressions.add(partialExpression.joinToString(""))
}.expressions.toList()

private fun calculate2(exp: String): String {
    var source = exp
    var target = ""
    while (source != target) {
        val leftExp = source.split("==").first().trim()
        val groups = leftExp.simplifyByParentheses()
        val replaced = groups.fold(source) { acc, e ->
            val calc = e.evaluate()
            calc?.let { c -> acc.replace(e, c) } ?: acc
        }
        target = source
        source = replaced
    }
    return target
}

private fun calculate3(exp: String): String {
    var source = exp
    while (source.toLongOrNull() == null) {
        val p2 = source.lastIndexOf(")")
        val p3 = source.substring(1, p2).reversed().dropWhile { it != ')' }.reversed().trim()
        val replaced = if (p3.startsWith("(")) {
            val p4 = source.substring(1, p2).reversed().takeWhile { it != ')' }.reversed().trim()
            val op = p4.split(" ").first()
            val p5 = p4.split(" ").last().toLongOrNull()
            val rightSide = source.split("==").last().trim().toLong()
            val temp = calculate3(op = op, leftSide = p5!!, rightSide = rightSide)
            if (p3 == "(???)") temp.toString() else "$p3 == $temp"
        } else {
            val pp = p3.dropWhile { it != '(' }.substringBefore("==")
            val p4 = p3.substringBefore("(").split(" ")
            val op = p4[1]
            val p5 = p4[0].trim().toLong()
            val rightSide = source.split("==").last().trim().toLong()
            val temp = calculate4(op = op, leftSide = p5, rightSide = rightSide)
            if (p3 == "(???)") temp.toString() else "$pp == $temp"
        }
        source = replaced
//        println(replaced.replace("==", "=").replace("???", "x"))
    }
    return source
}

private fun String.evaluate(): String? {
    if (this.contains("???")){
        return this
    }
    val calculateExp = replace(")", "").replace("(", "").calculateExp()
    return calculateExp?.toString()
}

private fun calculate3(op: String, leftSide: Long, rightSide: Long): Long {
    return when(op) {
        "+" -> rightSide - leftSide
        "-" -> rightSide + leftSide
        "/" -> rightSide * leftSide
        "*" -> rightSide / leftSide
        else -> error("not expected")
    }
}
private fun calculate4(op: String, leftSide: Long, rightSide: Long): Long {
    return when(op) {
        "+" -> rightSide - leftSide
        "-" -> leftSide - rightSide
        "/" -> leftSide / leftSide
        "*" -> rightSide / leftSide
        else -> error("not expected")
    }
}

private fun calculate(exp: String): String {
    var source = exp
    var target = ""
    while (source != target) {
        val p1 = source.lastIndexOf("(") + 1
        val p2 = source.drop(p1).indexOf(")")
        val targetExp = source.substring(p1, p1+p2)
        val v = targetExp.calculateExp()
        val replaced = if (v == null) source else source.replace("($targetExp)", v.toString())
        target = source
        source = replaced
    }
    return target
}

private fun String.calculateExp(): Long? {
    val first = this.split(" ").first().toLongOrNull() ?: return null
    val second = this.split(" ").last().toLongOrNull() ?: return null
    return when (this.split(" ")[1].trim()) {
        "+" -> first + second
        "-" -> first - second
        "*" -> first * second
        "/" -> first / second
        else -> error("unexpected")
    }
}

private tailrec fun simplify(exp: String, monkeys: Map<String, Yelling>): String {
    val result = monkeys.entries.fold(exp) { acc, el ->
        if (acc.contains(el.key)) {
            val replacedWith = if (el.value.value.toLongOrNull() != null) el.value.value else "(${el.value.value})"
            return@fold acc.replace(el.key, replacedWith)
        }
        acc
    }
    return if (exp == result) {
        result
    } else {
        simplify(result, monkeys)
    }
}