// Created on 12/31/23.

import Foundation

struct Position: Hashable {
  let row: Int
  let column: Int

  /// Manhattan distance.
  func manhattanDistance(to: Position) -> Int {
    return abs(row - to.row) + abs(column - to.column)
  }

  /// All neighboring positions, including diagonals.
  func allNeighbors() -> [Position] {
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

  /// Neighboring positions above, below, left and right.
  func neighbors() -> [Position] {
    let neighbors = [
      Position(row: row - 1, column: column),
      Position(row: row + 1, column: column),
      Position(row: row, column: column - 1),
      Position(row: row, column: column + 1),
    ]
    return neighbors
  }
}
