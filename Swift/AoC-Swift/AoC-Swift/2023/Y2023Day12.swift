// Created on 12/20/23.

import Foundation

/// --- Day 12: Hot Springs ---
/// https://adventofcode.com/2023/day/12
///
class Y2023Day12: Day {
  var dayNumber: Int = 12
  var year: Int = 2023
  var dataFileNumber: Int

  required init(dataFileNumber: Int) {
    self.dataFileNumber = dataFileNumber
  }

  
  func part1(_ lines: [String]) -> String {
    let rows = lines.compactMap { parseSpringRow($0) }
    let arrangementCounts = rows.map { $0.possibleArrangements() }
    let result = arrangementCounts.reduce(0, +)
    return "\(result)"
  }


  func part2(_ lines: [String]) -> String {
    let rows = lines.compactMap { parseSpringRow($0) }
    rows.forEach { row in
      row.springs = SpringRow.expandSprings(row.springs)
      row.damagedBlockSizes = SpringRow.expandBlocks(row.damagedBlockSizes)
    }

    let arrangementCounts = rows.map { $0.possibleArrangements() }
    let result = arrangementCounts.reduce(0, +)
    return "\(result)"
  }

  // MARK: - Private

}


// MARK: - Helper Types

fileprivate struct MemoKey: Hashable {
  let springIndex: Int
  let blockIndex: Int
}

fileprivate class SpringRow {
  var springs: [SpringState]
  var damagedBlockSizes: [Int]

  init(springs: [SpringState] = [], counts: [Int] = []) {
    self.springs = springs
    self.damagedBlockSizes = counts
  }


  func possibleArrangements() -> Int {
    var memoDict: [MemoKey: Int] = [:]
    let count = Self.arrangementCount(
      springStates: springs,
      springIndex: 0,
      damagedBlockSizes: damagedBlockSizes, 
      blockIndex: 0,
      using: &memoDict
    )
    return count
  }

  private static func arrangementCount(
    springStates: [SpringState],
    springIndex: Int,
    damagedBlockSizes: [Int],
    blockIndex: Int,
    using memoDict: inout [MemoKey: Int]
  ) -> Int {

    // If we've run out of blocks, check the remaining springs.
    if blockIndex >= damagedBlockSizes.count {
      // Make sure there are no more damaged springs. If so, this is a
      // valid arrangement.
      if !springStates[springIndex...].contains(.damaged) {
        return 1
      } else {
        return 0
      }
    }

    // There are more blocks of damaged springs. However, there are
    // no more springs remaining.
    if springIndex >= springStates.count {
      return 0
    }

    let memoKey = MemoKey(springIndex: springIndex, blockIndex: blockIndex)
    if let v = memoDict[memoKey] {
      return v
    }

    let currentSpring = springStates[springIndex]
    let currentBlockSize = damagedBlockSizes[blockIndex]

    // Inner function to run when the first spring is operational.
    func dot() -> Int {
      arrangementCount(
        springStates: springStates,
        springIndex: springIndex + 1,
        damagedBlockSizes: damagedBlockSizes,
        blockIndex: blockIndex,
        using: &memoDict)
    }

    // Inner function to run when the first spring is damaged.
    func pound() -> Int {
      // If the first spring is damaged, then the entirety of the the current block
      // of size `currentBlockSize` must fit.
      var endIndex = springIndex
      while endIndex < min(springIndex + currentBlockSize, springStates.count) {
        if springStates[endIndex] == .operational {
          break
        }
        endIndex += 1
      }

      let canFitBlock = endIndex == (springIndex + currentBlockSize)
      guard canFitBlock else {
        // Can't fit the current block. This isn't a valid arrangement.
        return 0
      }

      let remainingSprings = springStates.count - springIndex
      // The count of the remaining springs is exactly equal to the current block size,
      if remainingSprings == currentBlockSize {
        // ...and this is the last block, this is a valid arrangement.
        if blockIndex == damagedBlockSizes.count - 1 {
          return 1
        } else {
          return 0
        }
      } else {
        // After we fit the current block, the next spring must be an .operational spring.
        // If so, proceed to the next step.
        if springStates[endIndex] == .unknown || springStates[endIndex] == .operational {
          return arrangementCount(
            springStates: springStates,
            springIndex: endIndex + 1,
            damagedBlockSizes: damagedBlockSizes,
            blockIndex: blockIndex + 1,
            using: &memoDict)
        }
      }

      return 0
    }

    let count = switch currentSpring {
    case .damaged:
      pound()
    case .operational:
      dot()
    case .unknown:
      dot() + pound()
    }

    memoDict[memoKey] = count
    return count
  }


  static func expandSprings(_ springs: [SpringState]) -> [SpringState] {
    let duplicatedSprings = Array(repeating: springs, count: 5)
    var expandedSprings: [SpringState] = []
    expandedSprings.append(contentsOf: duplicatedSprings.first!)

    duplicatedSprings[1...].forEach { ss in
      expandedSprings.append(.unknown)
      expandedSprings.append(contentsOf: ss)
    }
    return expandedSprings
  }

  static func expandBlocks(_ blocks: [Int]) -> [Int] {
    let duplicatedBlocks: [[Int]] = Array(repeating: blocks, count: 5)
    return Array(duplicatedBlocks.joined())
  }
}


fileprivate enum SpringState: Character {
  case unknown = "?"
  case damaged = "#"
  case operational = "."
}


// MARK: - Parsing

extension Y2023Day12 {

  fileprivate func parseSpringRow(_ line: String) -> SpringRow? {
    let line = line.trimmingCharacters(in: .whitespacesAndNewlines)
    guard !line.isEmpty else {
      return nil
    }

    let segments = line.split(separator: " ")
    guard segments.count == 2 else {
      return nil
    }
    let springStates = parseSpringStates(String(segments[0]))
    let counts = parseCounts(String(segments[1]))

    let row = SpringRow(springs: springStates, counts: counts)
    return row
  }


  fileprivate func parseSpringStates(_ line: String) -> [SpringState] {
    let tokens = Array(line)
    return tokens.compactMap { SpringState(rawValue: $0) }
  }

  fileprivate func parseCounts(_ line: String) -> [Int] {
    let tokens = line.split(separator: ",")
    return tokens.compactMap { Int($0.trimmingCharacters(in: .whitespaces)) }
  }

}
