// Created on 12/27/23.

import Foundation
import SwiftAlgosDataStructures

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
    let result = dijkstra(grid)
    return "\(result)"
  }


  func part2(_ lines: [String]) -> String {
    ""
  }


  private func dijkstra(_ grid: Grid) -> Int {
    var queue = Heap<QueueNode> { qn1, qn2 in
      qn1.heatLoss < qn2.heatLoss
    }
    var seen = Set<State>()

    queue.insert(QueueNode(
      state: State(position: grid.start, direction: .east, stepCount: 1),
      heatLoss: 0))

    queue.insert(QueueNode(
      state: State(position: grid.start, direction: .south, stepCount: 1),
      heatLoss: 0))

    while !queue.isEmpty {
      let qNode = queue.remove()!

      if qNode.state.position == grid.destination {
        return qNode.heatLoss
      }

      guard !seen.contains(qNode.state) else {
        continue
      }
      seen.insert(qNode.state)

      // When turning left.
      let left = qNode.state.rotateLeftAndStep()
      let leftP = left.position
      if grid.containsPosition(leftP) && !seen.contains(left) {
        // cost is the sum of "heat loss so far" & the heat loss for the `leftP`.
        let cost = qNode.heatLoss + grid.getValueAt(leftP)
        let node = QueueNode(state: left, heatLoss: cost)
        queue.insert(node)
      }

      // When turning right
      let right = qNode.state.rotateRightAndStep()
      let rightP = right.position
      if grid.containsPosition(rightP) && !seen.contains(right) {
        let cost = qNode.heatLoss + grid.getValueAt(rightP)
        let node = QueueNode(state: right, heatLoss: cost)
        queue.insert(node)
      }

      let forward = qNode.state.step()
      let forwardP = forward.position
      if qNode.state.stepCount < 3 && grid.containsPosition(forwardP) && !seen.contains(forward) {
        let cost = qNode.heatLoss + grid.getValueAt(forwardP)
        let node = QueueNode(state: forward, heatLoss: cost)
        queue.insert(node)
      }
    }

    fatalError("Couldn't reach destination.")
  }


  // MARK: - Private

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

// MARK: - Helper Types

fileprivate struct Grid {
  let data: [[Int]]
  let rowCount: Int
  let columnCount: Int
  let start: Position
  let destination: Position

  init(data: [[Int]]) {
    self.data = data
    self.rowCount = data.count
    self.columnCount = rowCount == 0 ? 0 : data[0].count
    start = Position(row: 0, column: 0)
    destination = Position(row: rowCount - 1, column: columnCount - 1)
  }

  func getValueAt(_ p: Position) -> Int {
    data[p.row][p.column]
  }

  func containsPosition(_ p: Position) -> Bool {
    (p.row >= 0 && p.row < rowCount) && (p.column >= 0 && p.column < columnCount)
  }
}


fileprivate struct QueueNode: Comparable {
  /// Current position.
  let state: State
  /// The heat loss to get to the current `state` (position, direction).
  let heatLoss: Int

  static func < (lhs: QueueNode, rhs: QueueNode) -> Bool {
    lhs.heatLoss < rhs.heatLoss
  }
}


fileprivate struct State: Hashable {
  let position: Position
  let direction: Direction
  /// The number of steps needed to get to the current position.
  let stepCount: Int

  func step() -> State {
    return step(in: direction)
  }

  /// Steps in the given direction, and returns a new `Position`.
  func step(in dir: Direction) -> State {
    let p = self.position.nextPosition(in: dir)
    return State(position: p, direction: dir, stepCount: self.stepCount + 1)
  }

  func rotateLeftAndStep() -> State {
    let newDirection = direction.turnLeft()
    let p =  self.position.nextPosition(in: newDirection)
    let s = State(position: p, direction: newDirection, stepCount: 1)
    return s
  }

  func rotateRightAndStep() -> State {
    let newDirection = direction.turnRight()
    let p =  self.position.nextPosition(in: newDirection)
    let s = State(position: p, direction: newDirection, stepCount: 1)
    return s
  }
}


fileprivate struct Position: Hashable {
  let row: Int
  let column: Int

  func nextPosition(in direction: Direction) -> Position {
    switch direction {
    case .north:
      Position(row: row - 1, column: column)
    case .south:
      Position(row: row + 1, column: column)
    case .east:
      Position(row: row, column: column + 1)
    case .west:
      Position(row: row, column: column - 1)
    }
  }
}


fileprivate enum Direction {
  case north, south, east, west

  func turnLeft() -> Direction {
    return switch self {
      case .north:
        .west
      case .south:
        .east
      case .east:
        .north
      case .west:
        .south
    }
  }

  func turnRight() -> Direction {
    return switch self {
      case .north:
        .east
      case .south:
        .west
      case .east:
        .south
      case .west:
        .north
    }
  }
}
