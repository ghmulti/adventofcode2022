package com.github.ghmulti

private fun String.isCommand() = startsWith("$")

private fun String.parseInfo(): Obj = when {
    contains("dir") -> Obj(isFolder = true, size = 0, name = split(" ").last())
    else -> {
        val args = split(" ")
        Obj(isFolder = false, size = args.first().toInt(), name = args.last())
    }
}

private fun String.parseCommand(): Cmd = when {
    contains("ls") -> Cmd(cmdType = CmdType.LS)
    contains("cd") -> Cmd(cmdType = CmdType.CD, arg = split(" ").last())
    else -> error("not supported")
}

private fun Context.processCommand(cmd: Cmd): Context = when(cmd.cmdType) {
    CmdType.LS -> this // do nothing
    CmdType.CD -> cmd.arg?.let { path -> moveTo(path) } ?: error("no path provided for cd")
}

private fun Context.enrichNode(obj: Obj): Context {
    if (currentNode.children.map { it.value }.contains(obj)) {
        // do nothing, already exist
    } else {
        currentNode.children.add(TreeNode(value = obj, parent = this@enrichNode.currentNode))
    }
    return this
}

fun day00110() {
    val root = TreeNode(Obj(name = "/", isFolder = true, size = 0))
    "day00110.txt".pathTo().toFile().useLines { lines ->
        lines.fold(Context(
            root = root,
            currentNode = root,
        )) { context, line ->
            //println("Processing line [$line], Children ${context.currentNode.children.map { it.value.name }}")
            when {
                line.isCommand() -> context.processCommand(line.parseCommand())
                else -> context.enrichNode(line.parseInfo())
            }
            //println("Line: ${index+1}, currentNodeInfo: ${context.currentNode.value.name}, number of elements=${context.currentNode.children.size}, parent=${context.currentNode.parent?.value?.name}")
            //println("Path to root ${combinePath("!", context.currentNode)}")
        }
    }

    val pathAcc = mutableMapOf<String, Int>()
    traverseFolders("", pathAcc,  node = root)
    val sumOfDirectories = pathAcc.values.filter { it <= 100000 }.sumOf { it }
    "Sum of directories <= 100000 is $sumOfDirectories".cowsay("day 7")

    val sortedPaths = pathAcc.map { it.key to it.value }.sortedBy { it.second }
    val rootSize = sortedPaths.last().second
    val unused = 70_000_000 - rootSize
    val pathToDrop = sortedPaths.find { unused + it.second >= 30_000_000 } ?: error("not found")
    "Directory that needs to be deleted to cleanup 30_000_000 is ${pathToDrop.first}, with size=${pathToDrop.second} (memory available after delete = ${unused + pathToDrop.second})".cowsay("day 7")
}

private fun traverseFolders(pathTo: String, acc: MutableMap<String, Int>, node: TreeNode<Obj>) {
    val fileSize = node.children.filter { !it.value.isFolder }.sumOf { it.value.size }
    val folders = node.children.filter { it.value.isFolder }
    folders.forEach { traverseFolders("$pathTo/${it.value.name}", acc, it) }
    acc[pathTo] = fileSize + folders.sumOf { acc["$pathTo/${it.value.name}"]!! }
}

@Suppress("unused")
private fun combinePath(acc: String, node: TreeNode<Obj>): String =
    "${node.value.name}->$acc".takeIf { node.parent == null } ?: combinePath("${node.value.name}->$acc", node.parent!!)

private data class Context(
    val currentNode: TreeNode<Obj>,
    val root: TreeNode<Obj>,
)

private fun Context.moveTo(path: String): Context = when (path) {
    ROOT_PATH -> copy(currentNode = root)
    PARENT_PATH -> currentNode.parent?.let { parent -> copy(currentNode = parent) } ?: error("no parent")
    else -> {
        val targetNode = currentNode.children.find { it.value.isFolder && it.value.name == path } ?: error("path $path was not found")
        copy(currentNode = targetNode)
    }
}

private class TreeNode<T>(val value: T, val parent: TreeNode<T>? = null) {
    val children: MutableList<TreeNode<T>> = mutableListOf()
}

private data class Obj(val name: String, val isFolder: Boolean, val size: Int)
private data class Cmd(val cmdType: CmdType, val arg: String? = null)
private enum class CmdType { LS, CD }
private const val ROOT_PATH = "/"
private const val PARENT_PATH = ".."