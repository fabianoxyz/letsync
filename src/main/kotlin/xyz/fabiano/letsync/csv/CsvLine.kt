package xyz.fabiano.letsync.csv

class CsvLine(line: String, separator: Char) : Iterator<String> {
    private var currIndex = 0
    private val splitted = line.split(separator)

    operator fun get(index: Int): String {
        return splitted[index]
    }

    override fun hasNext(): Boolean {
        return currIndex < splitted.size
    }

    override fun next(): String {
        return splitted[currIndex++]
    }
}