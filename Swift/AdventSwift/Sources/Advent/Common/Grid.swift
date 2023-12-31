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
}
