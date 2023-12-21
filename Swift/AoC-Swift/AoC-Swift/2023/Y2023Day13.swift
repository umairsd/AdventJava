// Created on 12/21/23.

import Foundation


/// --- Day 13: Point of Incidence ---
/// https://adventofcode.com/2023/day/13
///
class Y2023Day13: Day {
  var dayNumber: Int = 13
  var year: Int = 2023
  var dataFileNumber: Int

  required init(dataFileNumber: Int) {
    self.dataFileNumber = dataFileNumber
  }


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
    return ""
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


fileprivate struct Pattern {
  let data: [[Character]]
  let rowMasks: [UInt]

  init(data: [[Character]]) {
    self.data = data
    self.rowMasks = data.map { Self.buildMask($0) }
  }


  func findReflectionRow() -> Int? {
    for i in 1..<rowMasks.count {
      // Check if this is perfect reflection.
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
