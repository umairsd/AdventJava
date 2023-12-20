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
    ""
  }


  func part2(_ lines: [String]) -> String {
    ""
  }
}


// MARK: - Helper Types

fileprivate class SpringRow {
  var springs: [SpringState]
  var counts: [Int]

  init(springs: [SpringState] = [], counts: [Int] = []) {
    self.springs = springs
    self.counts = counts
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
