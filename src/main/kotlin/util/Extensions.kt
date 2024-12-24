package util

fun <T : Comparable<T>> MutableList<T>.insertSorted(element: T) {
    val index = this.binarySearch(element)
    if (index < 0) {
        // `binarySearch` returns the negative insertion point - 1 if the element is not found
        this.add(-index - 1, element)
    } else {
        // If the element is found (duplicates allowed), you can decide to insert or not
        this.add(index, element)
    }
}