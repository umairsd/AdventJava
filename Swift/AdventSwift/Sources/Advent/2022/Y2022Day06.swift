// Created on 2/16/23.

import Foundation

class Y2022Day06: Day {

  private static let startOfPacketMarkerWidth = 4
  private static let startOfMessageMarkerWidth = 14
  
  var dayNumber: Int = 6
  var year: Int = 2022

  required init() {}

  func part1(_ lines: [String]) -> String {
    guard let i = endIndexOfUniqueBlock(in: lines[0], width: Self.startOfPacketMarkerWidth) else {
      fatalError("Unable to find the start of packet marker.")
    }
    // The problem expects 1-based indexing.
    return "\(i + 1)"
  }

  func part2(_ lines: [String]) -> String {
    guard let i = endIndexOfUniqueBlock(in: lines[0], width: Self.startOfMessageMarkerWidth) else {
      fatalError("Unable to find the start of message marker.")
    }
    // The problem expects 1-based indexing.
    return "\(i + 1)"

  }

  /// Finds the ending index of the first block of size `width` where all characters within
  /// the block are unique.
  private func endIndexOfUniqueBlock(in line: String, width: Int) -> Int? {
    guard width > 0 else {
      return nil
    }

    let lineArray = Array(line)
    // A map of characters to their counts. Represents a sliding window.
    var charCountMap = lineArray
      .prefix(width)
      .reduce(into: [:]) { countsMap, c in
        countsMap[c, default: 0] += 1
      }

    for i in width..<lineArray.count {
      let previous = lineArray[i - width]
      let current = lineArray[i]

      // Remove the previous character from the 'sliding window'
      charCountMap[previous, default: 0] -= 1
      if let v = charCountMap[previous], v == 0 {
        charCountMap.removeValue(forKey: previous)
      }

      // Add the current character.
      charCountMap[current, default: 0] += 1

      if charCountMap.count == width {
        return i
      }
    }

    return nil
  }
}
