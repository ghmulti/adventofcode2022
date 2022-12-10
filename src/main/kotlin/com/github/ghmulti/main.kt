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
    day0001()
    day0010()
    day0011()
    day0100()
    day0101()
    day0110()
    day0111()
    day1000()
    day1001()
    day1010()
}

fun String.pathTo(): Path = Path.of("src", "main", "resources", this)