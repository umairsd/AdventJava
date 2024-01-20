// Created on 12/31/23.

import Foundation

struct Grid<Element: Equatable> {
  let data: [[Element]]
  let rowCount: Int
  let columnCount: Int
  var topLeft: Position {
    Position(row: 0, column: 0)
  }
  var bottomRight: Position {
    Position(row: rowCount - 1, column: columnCount - 1)
  }

  init(data: [[Element]]) {
    self.data = data
    self.rowCount = data.count
    self.columnCount = rowCount == 0 ? 0 : data[0].count
  }

  func getValueAt(_ p: Position) -> Element {
    data[p.row][p.column]
  }

  func containsPosition(_ p: Position) -> Bool {
    (p.row >= 0 && p.row < rowCount) && (p.column >= 0 && p.column < columnCount)
  }

  func firstPosition(of element: Element) -> Position? {
    for (r, row) in data.enumerated() {
      for (c, value) in row.enumerated() {
        if value == element {
          return Position(row: r, column: c)
        }
      }
    }
    return nil
  }

  /// All direct neighbors (up, down, left, right) that are in bounds.
  func neighbors(of position: Position) -> [Position] {
    let neighbors = position.neighbors().filter { containsPosition($0) }
    return neighbors
  }


  /// All neighbors that are in bounds.
  func allNeighbors(of position: Position) -> [Position] {
    let neighbors = position.allNeighbors().filter { containsPosition($0) }
    return neighbors
  }
}
