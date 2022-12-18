package com.github.ghmulti

import java.nio.file.Path

private fun template(text: String) =
    """
     ${border(text, "_")}
    < $text >
     ${border(text, "-")}
            \   ^__^
             \  (oo)\_______
                (__)\       )\/\
                    ||----w |
                    ||     ||
    """.trimIndent()

private fun border(text: String, chr: String) = chr.repeat(text.length + 2)

fun String.cowsay(prefix: String? = null) = println(template((prefix?.let { "${it}: " } ?: "") + this))

fun main() {
    day00000()
    day00001()
    day00010()
    day00011()
    day00100()
    day00101()
    day00110()
    day00111()
    day01000()
    day01001()
    day01010()
    day01011()
    day01100()
    day01101()
    day01110()
    day01111()
    day10001()
}

fun String.pathTo(): Path = Path.of("src", "main", "resources", this)