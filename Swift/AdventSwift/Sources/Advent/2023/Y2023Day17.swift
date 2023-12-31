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
    let result = dijkstraLowestHeatLoss(
      in: grid, startingAt: grid.topLeft, endingAt: grid.bottomRight, minSteps: 0, maxSteps: 3)
    return "\(result)"
  }


  func part2(_ lines: [String]) -> String {
    let grid = parseGrid(lines)
    let result = dijkstraLowestHeatLoss(
      in: grid, startingAt: grid.topLeft, endingAt: grid.bottomRight, minSteps: 4, maxSteps: 10)
    return "\(result)"
  }


  private func dijkstraLowestHeatLoss(
    in grid: Grid<Int>,
    startingAt start: Position,
    endingAt destination: Position,
    minSteps: Int,
    maxSteps: Int
  ) -> Int {
    assert(grid.containsPosition(start))
    assert(grid.containsPosition(destination))

    var queue = Heap<QueueNode> { qn1, qn2 in
      qn1.heatLoss < qn2.heatLoss
    }
    var seen = Set<CrucibleState>()

    queue.insert(QueueNode(
      state: CrucibleState(position: start, direction: .east, stepCount: 0),
      heatLoss: 0))

    queue.insert(QueueNode(
      state: CrucibleState(position: start, direction: .south, stepCount: 0),
      heatLoss: 0))

    while !queue.isEmpty {
      let qNode = queue.remove()!

      if qNode.state.position == destination && qNode.state.stepCount >= minSteps {
        return qNode.heatLoss
      }

      guard !seen.contains(qNode.state) else {
        continue
      }
      seen.insert(qNode.state)

      // Turning left. Need to go a minimum of `minSteps` before turning.
      if qNode.state.stepCount >= minSteps {
        let left = qNode.state.rotateLeftAndStep()
        let leftP = left.position
        if grid.containsPosition(leftP) && !seen.contains(left) {
          // cost is the sum of "heat loss so far" & the heat loss for the `leftP`.
          let cost = qNode.heatLoss + grid.getValueAt(leftP)
          let node = QueueNode(state: left, heatLoss: cost)
          queue.insert(node)
        }
      }

      // Turning right. Need to go a minimum of `minSteps` before turning.
      if qNode.state.stepCount >= minSteps {
        let right = qNode.state.rotateRightAndStep()
        let rightP = right.position
        if grid.containsPosition(rightP) && !seen.contains(right) {
          let cost = qNode.heatLoss + grid.getValueAt(rightP)
          let node = QueueNode(state: right, heatLoss: cost)
          queue.insert(node)
        }
      }

      let forward = qNode.state.step()
      let forwardP = forward.position
      if qNode.state.stepCount < maxSteps &&
          grid.containsPosition(forwardP) &&
          !seen.contains(forward)
      {
        let cost = qNode.heatLoss + grid.getValueAt(forwardP)
        let node = QueueNode(state: forward, heatLoss: cost)
        queue.insert(node)
      }
    }

    return -1
  }


  // MARK: - Private

  private func parseGrid(_ lines: [String]) -> Grid<Int> {
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

fileprivate struct QueueNode: Comparable {
  /// Current position.
  let state: CrucibleState
  /// The heat loss to get to the current `state` (position, direction).
  let heatLoss: Int

  static func < (lhs: QueueNode, rhs: QueueNode) -> Bool {
    lhs.heatLoss < rhs.heatLoss
  }
}


fileprivate struct CrucibleState: Hashable {
  let position: Position
  let direction: Direction
  /// The number of steps taken so far.
  let stepCount: Int

  func step() -> CrucibleState {
    return step(in: direction)
  }

  /// Steps in the given direction, and returns a new `Position`.
  func step(in dir: Direction) -> CrucibleState {
    let p = self.position.nextPosition(in: dir)
    return CrucibleState(position: p, direction: dir, stepCount: self.stepCount + 1)
  }

  func rotateLeftAndStep() -> CrucibleState {
    let newDirection = direction.turnLeft()
    let p =  self.position.nextPosition(in: newDirection)
    let s = CrucibleState(position: p, direction: newDirection, stepCount: 1)
    return s
  }

  func rotateRightAndStep() -> CrucibleState {
    let newDirection = direction.turnRight()
    let p =  self.position.nextPosition(in: newDirection)
    let s = CrucibleState(position: p, direction: newDirection, stepCount: 1)
    return s
  }
}


fileprivate extension Position {

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
