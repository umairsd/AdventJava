// Created on 12/21/23.

import Foundation


/// --- Day 13: Point of Incidence ---
/// https://adventofcode.com/2023/day/13
///
class Y2023Day13: Day {
  var dayNumber: Int = 13
  var year: Int = 2023

  required init() {}


  func part1(_ lines: [String]) -> String {
    let patterns = parsePatterns(lines)
    let reflectedRows = patterns
      .compactMap { $0.findReflectionRow() }
      .map { 100 * $0 }

    let refelectedColumns = patterns
      .map { Pattern.transpose($0) }
      .compactMap { $0.findReflectionRow() }

    let result = reflectedRows.reduce(0, +) + refelectedColumns.reduce(0, +)
    return "\(result)"
  }


  func part2(_ lines: [String]) -> String {
    let patterns = parsePatterns(lines)
    let reflectedRows = patterns
      .compactMap { $0.findReflectionRow(1) }
      .map { 100 * $0 }

    let refelectedColumns = patterns
      .map { Pattern.transpose($0) }
      .compactMap { $0.findReflectionRow(1) }

    let result = reflectedRows.reduce(0, +) + refelectedColumns.reduce(0, +)
    return "\(result)"
  }
}

// MARK: - Parsing

fileprivate extension Y2023Day13 {

  func parsePatterns(_ lines: [String]) -> [Pattern] {
    let splits: [[String]] = lines
      .map { $0.trimmingCharacters(in: .whitespaces) }
      .split(separator: "")
      .map { Array($0) }

    let patterns = splits.map { lines in
      let p = lines.map { Array($0) }
      return Pattern(data: p)
    }
    return patterns
  }
}

// MARK: - Helper Types

fileprivate struct Pattern {
  let data: [[Character]]
  let rowMasks: [UInt]

  init(data: [[Character]]) {
    self.data = data
    self.rowMasks = data.map { Self.buildMask($0) }
  }


  func findReflectionRow() -> Int? {
    for i in 1..<rowMasks.count {
      var p = i - 1
      var n = i
      while p >= 0 && n < rowMasks.count && rowMasks[p] == rowMasks[n] {
        p -= 1
        n += 1
      }

      if p < 0 || n >= rowMasks.count {
        return i
      }
    }
    return nil
  }


  func findReflectionRow(_ rowsWithAllowedSmudges: Int) -> Int? {
    for r in 1..<data.count {
      let rowsAbove = data[0..<r].reversed()
      let rowsBelow = data[r...]

      var equalCount = 0
      var kindOfEqualCount = 0
      var totalCount = 0
      for rowPair in zip(rowsAbove, rowsBelow) {
        if rowPair.0 == rowPair.1 {
          equalCount += 1
        } else if areRowsKindOfEqual(rowPair.0, rowPair.1) {
          kindOfEqualCount += 1
        }
        totalCount += 1
      }

      if kindOfEqualCount == rowsWithAllowedSmudges &&
          (kindOfEqualCount + equalCount) == totalCount {
        return r
      }
    }

    return nil
  }


  func areRowsKindOfEqual(_ row1: [Character], _ row2: [Character]) -> Bool {
    guard row1.count == row2.count else {
      return false
    }

    var diffCount = 0
    zip(row1, row2).forEach { diffCount += $0.0 == $0.1 ? 0 : 1 }
    return diffCount <= 1
  }


  static func transpose(_ pattern: Pattern) -> Pattern {
    let transposedColumnCount = pattern.data.count
    let transposedRowCount = transposedColumnCount == 0 ? 0 : pattern.data[0].count
    var transposedData: [[Character]] = Array(
      repeating: Array(repeating: ".", count: transposedColumnCount),
      count: transposedRowCount)

    for (r, row) in pattern.data.enumerated() {
      for (c, value) in row.enumerated() {
        transposedData[c][r] = value
      }
    }

    return Pattern(data: transposedData)
  }


  private static func buildMask(_ array: [Character]) -> UInt {
    assert(array.count < 64)

    var mask: UInt = 0
    for c in array {
      mask = (mask << 1) | (c == "#" ? 1 : 0)
    }
    return mask
  }


}
