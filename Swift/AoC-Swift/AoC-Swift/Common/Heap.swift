// Created on 12/28/23.

import Foundation


struct Heap<Element> {

  private var elements: [Element] = []
  /// A predicate that returns true if its first argument should be ordered before its
  /// second argument; otherwise, false.
  private let areInIncreasingOrder: (Element, Element) -> Bool

  init(sortedBy areInIncreasingOrder: @escaping (Element, Element) -> Bool) {
    self.areInIncreasingOrder = areInIncreasingOrder
  }

  /// Whether the heap is empty.
  var isEmpty: Bool {
    elements.isEmpty
  }

  /// Return the root of the heap, without removing it.
  func peek() -> Element? {
    elements.first
  }

  /// Index of the left child of the current node.
  /// - note: left child of a node at `i` is at: `2i + 1`.
  func leftChildIndex(ofNodeAt index: Int) -> Int {
    (2 * index) + 1
  }

  /// Index of the right child of the current node.
  /// - note: right child of a node at `i` is at: `2i + 2`.
  func rightChildIndex(ofNodeAt index: Int) -> Int {
    (2 * index) + 2
  }

  /// Index of the parent of the current node.
  func parentIndex(ofNodeAt index: Int) -> Int {
    (index - 1) / 2
  }

  // MARK: - Modifications

  /// Remove the root of the heap, ensuring that the heap property holds after removal.
  /// - Returns: The root of the heap, if it exists.
  mutating func remove() -> Element? {
    // If the heap is empty, nothing to return.
    guard !elements.isEmpty else {
      return nil
    }
    // Swap the root (the element to be returned) with the last element in the heap
    elements.swapAt(0, elements.count - 1)
    // Remove the element to be returned, which will be at the last position now.
    let result = elements.removeLast()
    // Heapify, to maintain the heap property.
    siftDown(from: 0)
    // Finally, return the element just removed.
    return result
  }


  /// Inserts the given element into the heap, ensuring that the heap-property holds after
  /// insertion.
  mutating func insert(_ element: Element) {
    elements.append(element)
    siftUp(from: elements.count - 1)
  }


  /// Remove an element at the given index, ensuring the heap property holds after removal.
  /// - Parameter index: Index of the element to be removed
  /// - Returns: The element at the given index if it exists.
  mutating func remove(at index: Int) -> Element? {
    guard index < elements.count else {
      return nil
    }

    if index == elements.count - 1 {
      return elements.removeLast()
    } else {
      elements.swapAt(index, elements.count - 1)
      let result = elements.removeLast()

      siftDown(from: index)
      siftUp(from: index)

      return result
    }
  }


  /// Searches for the index of the given element, if it exists.
  /// - Parameters:
  ///   - element: The element to search for.
  ///   - i: The starting index
  /// - Returns: The index of the element.
  func index(of element: Element, startingAt i: Int) -> Int? {
    if i >= elements.count {
      return nil
    }
    if areInIncreasingOrder(element, elements[i]) {
      return nil
    }
    if let j = index(of: element, startingAt: leftChildIndex(ofNodeAt: i)) {
      return j
    }
    if let j = index(of: element, startingAt: rightChildIndex(ofNodeAt: i)) {
      return j
    }
    return nil
  }


  // MARK: - Private

  private mutating func siftUp(from index: Int) {
    // Algorithm:
    // Swap the current node with its parent, as long as the current node has a higher priority
    // than its parent. Repeat until done.
    var currentIdx = index
    var parentIdx = parentIndex(ofNodeAt: currentIdx)

    while currentIdx > 0 && areInIncreasingOrder(elements[currentIdx], elements[parentIdx]) {
      elements.swapAt(currentIdx, parentIdx)

      currentIdx = parentIdx
      parentIdx = parentIndex(ofNodeAt: currentIdx)
    }
  }

  private mutating func siftDown(from index: Int) {
    // Algorithm:
    // To perform a heapify operation, check the current value and its left and right children.
    // If any of the children has a value greater than current value, swap the current value with
    // the child having the larger value.
    var currentIdx = index

    while true {
      let leftIdx = leftChildIndex(ofNodeAt: currentIdx)
      let rightIdx = rightChildIndex(ofNodeAt: currentIdx)

      var candidateIdx = currentIdx

      if leftIdx < elements.count && areInIncreasingOrder(elements[leftIdx], elements[candidateIdx]) {
        candidateIdx = leftIdx
      }
      if rightIdx < elements.count && areInIncreasingOrder(elements[rightIdx], elements[candidateIdx]) {
        candidateIdx = rightIdx
      }

      if candidateIdx == currentIdx {
        return
      }

      elements.swapAt(currentIdx, candidateIdx)
      currentIdx = candidateIdx
    }
  }
}

