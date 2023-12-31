// Created on 12/31/23.

import Foundation

extension Array {

  /// Returns elements of this array as a list of pairs of adjacent elements.
  /// The last element is paired with the first one.
  func pairwise() -> [(Element, Element)] {
    guard !isEmpty else {
      return []
    }

    var result: [(Element, Element)] = []
    for r in 1..<count {
      let e1 = self[r - 1]
      let e2 = self[r]
      result.append((e1, e2))
    }
    result.append((last!, first!))
    return result
  }
}



extension Array where Element: Hashable {

  /// Returns a new array by removing duplicates from the current array.
  func removingDuplicates() -> [Element] {
    var seen = Set<Element>()

    return filter {
      if seen.contains($0) {
        return false
      }
      seen.insert($0)
      return true
    }
  }


  /// Removes duplicates from the array.
  mutating func removeDuplicates() {
    self = self.removingDuplicates()
  }
}
