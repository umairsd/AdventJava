// Created on 12/27/23.

import Foundation


/// --- Day 17: Clumsy Crucible ---
/// https://adventofcode.com/2023/day/17
///
class Y2023Day17: Day {
  var dayNumber: Int = 17
  var year: Int = 2023
  var dataFileNumber: Int

  required init(dataFileNumber: Int) {
    self.dataFileNumber = dataFileNumber
  }


  func part1(_ lines: [String]) -> String {
    let grid = parseGrid(lines)
    return ""
  }


  func part2(_ lines: [String]) -> String {
    ""
  }

  private func parseGrid(_ lines: [String]) -> Grid {
    let grid: [[Int]] = lines
      .filter { !$0.trimmingCharacters(in: .whitespaces).isEmpty }
      .map {
        Array($0).compactMap { Int(String($0))
      }
    }
    return Grid(data: grid)
  }
}


fileprivate struct Grid {
  let data: [[Int]]
  let rowCount: Int
  let columnCount: Int

  init(data: [[Int]]) {
    self.data = data
    self.rowCount = data.count
    self.columnCount = rowCount == 0 ? 0 : data[0].count
  }
}
