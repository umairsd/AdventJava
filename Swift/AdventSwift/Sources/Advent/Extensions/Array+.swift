// Created on 12/31/23.

import Foundation

extension Array where Element: Hashable {

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

  mutating func removeDuplicates() {
    self = self.removingDuplicates()
  }
}
