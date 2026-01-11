package objects

class Node(
    val value: Any,
    var visited: Boolean = false,
) {
    private val parents: MutableList<Node> = mutableListOf()
    private val children: MutableList<Node> = mutableListOf()

    fun addParent(node: Node) {
        parents.add(node)
    }

    fun addChild(node: Node) {
        children.add(node)
    }

    fun removeParent(node: Node): Boolean {
        return parents.remove(node)
    }

    fun removeChildren(node: Node): Boolean {
        return children.remove(node)
    }

    fun isParentOf(node: Node): Boolean {
        return children.contains(node)
    }

    fun isChildOf(node: Node): Boolean {
        return parents.contains(node)
    }

    fun hasChildWithValue(value: Any): Boolean {
        return children.any { it.value == value }
    }

    fun findChild(predicate: (Node) -> Boolean): Node? {
        return children.firstOrNull(predicate)
    }

    fun numChildren(): Int {
        return children.size
    }

    override fun toString(): String {
        return "Node(value = $value)"
    }

    override fun equals(other: Any?): Boolean {
        return other is Node && other.value == value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}
