// Created on 2/16/23.

import Foundation

class Y2022Day06: Day {

  private static let startOfMessageWidth = 4
  
  var dayNumber: Int = 6
  var year: Int = 2022
  var dataFileNumber: Int = 2

  required init(dataFileNumber: Int) {
    self.dataFileNumber = dataFileNumber
  }

  func part1(_ lines: [String]) -> String {
    let packetWidth = Self.startOfMessageWidth

    let lineArray = Array(lines[0])
    var charCountMap = lineArray.prefix(packetWidth).reduce(into: [:]) { partialResult, c in
      partialResult[c, default: 0] += 1
    }

    var startOfPacketIdx: Int?

    for i in packetWidth..<lineArray.count {
      let previous = lineArray[i - packetWidth]
      let current = lineArray[i]

      // Remove from the previous character from the map
      charCountMap[previous, default: 0] -= 1
      if let v = charCountMap[previous], v == 0 {
        charCountMap.removeValue(forKey: previous)
      }

      // Add the current character.
      charCountMap[current, default: 0] += 1

      if charCountMap.count == packetWidth {
        startOfPacketIdx = i
        break
      }
    }

    guard let idx = startOfPacketIdx else {
      fatalError("Unable to find the start of packet marker.")
    }

    // The problem expects 1-based indexing.
    let startOfPacketMarker = idx + 1
    return "\(startOfPacketMarker)"
  }

  func part2(_ lines: [String]) -> String {
    ""
  }
}
