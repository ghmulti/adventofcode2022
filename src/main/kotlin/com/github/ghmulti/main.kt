package com.github.ghmulti

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
    day01()
}
