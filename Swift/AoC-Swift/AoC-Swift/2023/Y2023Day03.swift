// Created on 12/3/23.

import Foundation

/// --- Day 3: Gear Ratios ---
/// https://adventofcode.com/2023/day/3
class Y2023Day03: Day {
  var dayNumber: Int = 3
  var year: Int = 2023
  var dataFileNumber: Int

  required init(dataFileNumber: Int) {
    self.dataFileNumber = dataFileNumber
  }

  func part1(_ lines: [String]) -> String {
    let grid = lines
      .filter { !$0.isEmpty}
      .map { Array($0) }

    let partNumbers = getValidPartNumbers(grid)
    let sum = partNumbers.reduce(0, +)
    return "\(sum)"
  }

  func part2(_ lines: [String]) -> String {
    ""
  }


  /// Traverses the grid, and when it encounters a numeric cell, builds a number. For each such
  /// number, it keeps a record of its position, and then checks the neighbors of all these
  /// positions to see if they are a symbol. If so, adds this number to the list of valid
  /// part numbers.
  private func getValidPartNumbers(_ grid: [[Character]]) -> [Int] {
    var partNumbers: [Int] = []

    var r = 0
    while r < grid.count {
      var c = 0
      let currentRow = grid[r]
      while c < currentRow.count {
        if grid[r][c].isNumber {
          // Start from this column index, and keep going as long as the cells are numeric.
          var number = 0
          var positionSet = Set<Position>()
          while c < currentRow.count && grid[r][c].isNumber {
            number *= 10
            number += grid[r][c].wholeNumberValue ?? 0
            positionSet.insert(Position(row: r, column: c))
            c += 1
          }

          let neighbors = neighborsOfPositionSet(positionSet, in: grid)
          for neighbor in neighbors {
            let v = grid[neighbor.row][neighbor.column]
            if v != "." && !v.isNumber {
              // This number is a part number!
              partNumbers.append(number)
            }
          }
        } else {
          c += 1
        }
      }

      r += 1
    }

    return partNumbers
  }


  private func neighborsOfPositionSet(_ positions: Set<Position>, in grid: [[Character]]) -> [Position] {
    var neighbors = Set<Position>()
    for p in positions {
      // Valid neighbor that's not one of the positions in the positions set.
      let validNeighbors = p.neighbors().filter { isValid($0, in: grid) && !positions.contains($0) }
      validNeighbors.forEach { neighbors.insert($0) }
    }
    return Array(neighbors)
  }


  /// Whether the given position is valid within the current grid.
  private func isValid(_ position: Position, in grid: [[Character]]) -> Bool {
    let rowCount = grid.count
    let columnCount = rowCount == 0 ? 0 : grid[0].count
    guard position.row >= 0 && position.row < rowCount else { return false }
    guard position.column >= 0 && position.column < columnCount else { return false }

    return true
  }
}


fileprivate struct Position: Hashable {
  let row: Int
  let column: Int

  fileprivate func neighbors() -> [Position] {
    let neighbors = [
      Position(row: row - 1, column: column - 1),
      Position(row: row - 1, column: column),
      Position(row: row - 1, column: column + 1),
      Position(row: row, column: column - 1),
      Position(row: row, column: column + 1),
      Position(row: row + 1, column: column - 1),
      Position(row: row + 1, column: column),
      Position(row: row + 1, column: column + 1),
    ]
    return neighbors
  }
}
