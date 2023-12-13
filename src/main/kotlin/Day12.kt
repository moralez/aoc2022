import java.util.*

fun main(args: Array<String>) {
    println("Hello World!")

    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString()}")

    val day = Day12()
    day.processInput()
}
class Day12: Day(fileName = "D:\\Projects\\aoc2022\\src\\main\\resources\\day12") {

    var lineLength = 0
    override fun processInput(lines: Int?) {
        log("Day 12...")

        val peaks: MutableList<MutableList<Node>> = mutableListOf()
        val nodes: MutableSet<Node> = mutableSetOf()

        var currId = 0
        for (i in input.indices) {
            val line = input[i]
            lineLength = line.length
            val currPeakRow: MutableList<Node> = mutableListOf()
            peaks.add(currPeakRow)
            for (j in line.indices) {
                val currTree = when (val char = line[j]) {
                    'S' -> Node(id = currId, height = 'a'.code, start = true, distance = 0)
                    'E' -> Node(id = currId, height = 'z'.code, end = true)
                    in 'a'..'z' -> Node(id = currId, height = char.code)
                    else -> null
                }

                currTree?.let {
                    val north: Node? = if (i-1 >= 0) peaks[i-1][j] else null
                    val east: Node? = null
                    val west: Node? = if (j-1 >= 0) peaks[i][j-1] else null
                    val south: Node? = null
                    currTree.setNeighbors(north, south, east, west)
                    currPeakRow.add(it)
                    nodes.add(it)
                    currId++
                }
            }
        }

        val graph2 = buildGraph(nodes)
        val graph2Start = graph2.keys.first { it.start }
        val graph2End = graph2.keys.first { it.end }
//        val list = dijkstra2(graph2, graph2Start, graph2End)
//        log("List: ${list.size}\n${list.map { it.height.toChar() }.joinToString(" -> ")}")

        jmokstra(graph2, graph2Start, graph2End)
//        printDistanceGraph(graph2)

        log("Distance ? = ${graph2End.distance}")

        val starts = nodes.filter { it.height == 'a'.code }
        val ends = mutableListOf<Int>()
        for (start in starts) {
            val graph = buildGraph(nodes)
            val graphEnd = graph2.keys.first { it.end }
            jmokstra(graph, start, graphEnd)
//            log("Distance[${start.id}] = ${graphEnd.distance}")
            ends.add(graphEnd.distance)
        }

        log("Minimum: ${ends.min()}")
    }

    fun buildGraph(nodes: MutableSet<Node>): MutableMap<Node, MutableMap<Node, Int>> {
        val graph = mutableMapOf<Node, MutableMap<Node, Int>>()
        for (node in nodes) {
            graph[node] = mutableMapOf<Node, Int>().apply {
                node.north?.let {
                    if (node.height+1 >= it.height) {
                        put(it, 1)
                    }
                }
                node.south?.let {
                    if (node.height+1 >= it.height) {
                        put(it, 1)
                    }
                }
                node.east?.let {
                    if (node.height+1 >= it.height) {
                        put(it, 1)
                    }
                }
                node.west?.let {
                    if (node.height+1 >= it.height) {
                        put(it, 1)
                    }
                }
            }
        }
        return graph
    }

    fun jmokstra(graph: Map<Node, Map<Node, Int>>, source: Node, target: Node) {
        val nodes = PriorityQueue<Node> { a, b -> a.distance - b.distance }

        val previous = mutableMapOf<Node, Node>()

        // Initialize distances
        for (node in graph.keys) {
            node.distance = if (node == source) 0 else Int.MAX_VALUE
            node.visited = false
        }

        nodes.add(source)

        while (nodes.isNotEmpty()) {
            val current = nodes.poll()
            current.visited = true

            if (current == target) {
//                log("Found the target: current size of previous: ${previous.size}")
                val path = mutableListOf<Node>()
                var node = target
                while (node != source) {
                    path.add(0, node)
                    node = previous[node]!!
                }
                path.add(0, source)
//                log("Final Path:")
//                log("\t${path.map { it.id }.joinToString(", ")}")

//                printPretty(previous, graph, path)
                return
            }

            val neighbors = current.reachableNeighbors
            for (neighbor in neighbors) {
                if (neighbor.visited) continue

                val possibleNewNeighborDistance = current.distance + 1
                if (possibleNewNeighborDistance < neighbor.distance) {
                    neighbor.distance = possibleNewNeighborDistance
                    nodes.add(neighbor)
                    previous[neighbor] = current
//                    log("Added Neighbor -> ${neighbor.id} ${neighbor.distance}")
//                    log("\tPriority Queue: ${nodes.map { "${it.id} ${it.distance}" }.joinToString(", ")}")
                }
            }
        }
    }

    fun dijkstra2(graph: Map<Node, Map<Node, Int>>, source: Node, target: Node): List<Node> {
        log("beginning")
        val distances = mutableMapOf<Node, Int>()
        val visited = mutableSetOf<Node>()
        val previous = mutableMapOf<Node, Node>()
        val nodes = PriorityQueue<Node> { a, b -> distances[a]!! - distances[b]!! }

        // Initialize distances
        for (node in graph.keys) {
            distances[node] = if (node == source) {
                log("zero distance set for node $node / ${node.id}")
                0
            } else Int.MAX_VALUE
        }
        log("distances initialized")

        nodes.add(source)

        while (nodes.isNotEmpty()) {
            log("Not empty")
            val current = nodes.poll()
            log("Checking Node: ${current.id}")
            visited.add(current)
            current.visited = true

            if (current == target) {
                log("Found the target")
                // We have reached the target node, so we can construct the shortest path by
                // following the previous node of each node in the path
                val path = mutableListOf<Node>()
                var node = target
                while (node != source) {
                    path.add(0, node)
                    node = previous[node]!!
                }
                path.add(0, source)
                return path
            }

            log("Checking Node $current #${current.id} neighbors...")
            for (neighbor in graph[current]!!.keys) {
                log("\tNeighbor Node #${neighbor.id} has distance ${graph[current]?.get(neighbor)}")
                if (visited.contains(neighbor)) {
                    log("\tAlready visited this guy")
                    continue
                }

                val currentNodesDistanceFromSource = distances[current]!!
                val neighborsDistanceFromCurrent = graph[current]!![neighbor]!!
                val currentDistanceForNeighborFromSource = distances[neighbor]!!
                val distance = currentNodesDistanceFromSource + neighborsDistanceFromCurrent
                log("\tChecking $distance v $currentDistanceForNeighborFromSource")
                if (distance < currentDistanceForNeighborFromSource) {
                    log("Shorter Route found from Node #${current.id} to #${neighbor.id} [$currentDistanceForNeighborFromSource -> $distance]")
                    distances[neighbor] = distance
                    previous[neighbor] = current
                    nodes.add(neighbor)
                }
            }
        }

        // If we reach here, it means that we have not been able to find a path to the target node
        log("No path")
        return emptyList()
    }
    private fun printGraph(peaks: MutableMap<Node, MutableMap<Node, Int>>) {
        val sb = StringBuilder()
        for (node in peaks.keys) {
            sb.append("Node #${node.id}: ")
            sb.append("\t[")
            sb.append(node.graphNeighbors().keys.map { it.id }.joinToString(", "))
            sb.append("]")
            sb.append("\n")
        }
        log("Graph:")
        log(sb.toString())
    }

    private fun printDistanceGraph(peaks: MutableMap<Node, MutableMap<Node, Int>>) {
        log("****Distances: [${peaks.size}]")
        val sb = StringBuilder()
        var newLine = 0
        for (node in peaks.keys) {
            sb.append(node.distance)
            newLine++
            if (newLine == lineLength) {
                newLine = 0
                sb.append("\n")
            } else {
                sb.append("\t")
            }
        }
        log(sb.toString())
    }

    class Node(
        val id: Int,
        val height: Int,
        val start: Boolean = false,
        val end: Boolean = false,
        var north: Node? = null,
        var south: Node? = null,
        var east: Node? = null,
        var west: Node? = null,
        var distance: Int = Int.MAX_VALUE,
        var visited: Boolean = false,
        val previous: Node? = null
    ) {
        val neighbors: List<Node>
            get() {
                return mutableListOf<Node>().apply {
                    north?.let { add(it) }
                    south?.let { add(it) }
                    east?.let { add(it) }
                    west?.let { add(it) }
                }
            }

        val reachableNeighbors: List<Node>
            get() {
                return mutableListOf<Node>().apply {
                    north?.let { if (height+1 >= it.height) add(it) }
                    south?.let { if (height+1 >= it.height) add(it) }
                    east?.let { if (height+1 >= it.height) add(it) }
                    west?.let { if (height+1 >= it.height) add(it) }
                }
            }

        fun graphNeighbors(): MutableMap<Node, Int> {
            val map = mutableMapOf<Node, Int>()
            north?.let { map[it] = 1 }
            south?.let { map[it] = 1 }
            east?.let { map[it] = 1 }
            west?.let { map[it] = 1 }
            return map
        }

        fun distanceToNeighbor(neighbor: Node): Int {
            if (!neighbors.contains(neighbor)) return Int.MAX_VALUE

            return if (neighbor.height <= height+1) return 1 else Int.MAX_VALUE
        }

        fun setNeighbors(north: Node?, south: Node?, east: Node?, west: Node?) {
            this.north = north
            this.north?.let {
                it.south = this
            }

            this.south = south
            this.south?.let {
                it.north = this
            }

            this.east = east
            this.east?.let {
                it.west = this
            }

            this.west = west
            this.west?.let {
                it.east = this
            }
        }

        fun isNeighborTo(node: Node): Boolean {
            return node == north || node == south || node == east || node == west
        }
    }

    fun printPretty(previous: Map<Node, Node>, graph: Map<Node, Map<Node, Int>>, path: List<Node>) {
        val sb = StringBuilder()
        for (node in graph.keys) {
            if (node.start) {
                sb.append("S")
            } else if (node.end) {
                sb.append("E")
            } else if (path.contains(node)) {
                val parent = path[path.indexOf(node)-1]
                if (parent == node.north) {
                    sb.append("|") // down
                } else if (parent == node.south){
                    sb.append("|") // up
                } else if (parent == node.east){
                    sb.append("-") // left
                } else if (parent == node.west){
                    sb.append("-") // right
                }
            } else {
                sb.append(node.height.toChar())
            }
        }
        log(sb.toString().chunked(lineLength).joinToString("\n"))
    }
}