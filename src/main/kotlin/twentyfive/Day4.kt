package twentyfive

import Day

class Day4: Day(fileName = "2025/day4", isResource = true) {

    companion object {
        const val PAPER = '@'
        const val EMPTY = '.'
    }

    override fun processInput(lines: Int?) {
        var grid: List<List<Char>> = input.map { it.toList() }
        var grid2: MutableList<MutableList<Char>> = mutableListOf()

        var accessiblePaper = 0
        var removed = 0
        var pass = 0
        do {
            accessiblePaper = 0
            grid.forEachIndexed { rowIndex, row ->
                val currRow = mutableListOf<Char>()
                grid2.add(currRow)
                row.forEachIndexed { colIndex, col ->
                    if (col == PAPER) {
                        val neighboringPaper = countSurroundingChars(grid, rowIndex, colIndex, PAPER)
                        if (neighboringPaper < 4) {
                            accessiblePaper++
                            removed++
                            currRow.add(EMPTY)
                        } else {
                            currRow.add(col)
                        }
                    } else {
                        currRow.add(col)
                    }
                }
            }

            println("Pass $pass removed $accessiblePaper")

            pass++

            grid = grid2
            grid2 = mutableListOf()
        } while (accessiblePaper > 0)

        println("removed $removed")
        grid2.map { it.joinToString("") }.forEach { println(it) }
    }

    private fun countSurroundingChars(
        grid: List<List<Char>>,
        x: Int,
        y: Int,
        target: Char
    ): Int {
        val neighbors = listOf(
            -1 to -1,   0 to -1,    1 to -1,    // Top-left, Top, Top-right
            -1 to 0,    1 to 0,                 // Left, Right
            -1 to 1,    0 to 1,     1 to 1      // Bottom-left, Bottom, Bottom-right
        )

        val numRows = grid.size
        val numCols = grid[0].size
        var count = 0

        for ((dr, dc) in neighbors) {
            val nr = x + dr
            val nc = y + dc

            if (nr in 0 until numRows && nc in 0 until numCols) {
                if (grid[nr][nc] == target) {
                    count++
                }
            }
        }
        return count
    }
}

fun main() {
    val startTime = System.nanoTime()

    Day4().processInput()

    val endTime = System.nanoTime()
    val duration = endTime - startTime

    println("Execution time: ${duration / 1_000_000} ms")
}