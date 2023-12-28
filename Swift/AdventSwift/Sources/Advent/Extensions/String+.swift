// Created on 12/3/23.

import Foundation


extension String {

  /// Loop through `string` to find the first index of the `match`, if any.
  func startIndex(of match: String) -> Int? {
    for (index, c) in self.enumerated() {
      guard c == match.first, (index + match.count) <= self.count else {
        continue
      }
      let start = self.index(self.startIndex, offsetBy: index)
      let end = self.index(self.startIndex, offsetBy: match.count + index)

      if self[start..<end] == match {
        return index
      }
    }
    return nil
  }


  /// Loop through `string` to find the last index of the `match`, if any.
  func lastIndexOf(_ match: String) -> Int? {
    let reversed = String(self.reversed())
    let reversedMatch = String(match.reversed())
    guard let index = reversed.startIndex(of: reversedMatch) else {
      return nil
    }
    return self.count - (index + match.count)
  }
}
