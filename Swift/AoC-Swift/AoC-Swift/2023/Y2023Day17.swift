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
    let result = dijkstra(grid)
    return ""
  }


  func part2(_ lines: [String]) -> String {
    ""
  }


  private func dijkstra(_ grid: Grid) -> Int {
    var queue: [QueueState] = []
    var seen = Set<Position>()

    let l1 = grid.start.nextLocation(in: .east)
    let p1 = Position(location: l1, direction: .east, stepCount: 1)
    queue.append(QueueState(position: p1, heatLoss: grid.getValueAt(l1)))

    let l2 = grid.start.nextLocation(in: .south)
    let p2 = Position(location: l2, direction: .south, stepCount: 1)
    queue.append(QueueState(position: p2, heatLoss: grid.getValueAt(l2)))

    while !queue.isEmpty {
      let state = removeCheapest(&queue)

      if state.position.location == grid.destination {
        return state.heatLoss
      }

      if seen.contains(state.position) {
        continue
      }
      seen.insert(state.position)

      let leftP = state.position.rotateAndStep(.counterClockwise)
      if grid.containsLocation(leftP.location) {
        let cost = state.heatLoss + grid.getValueAt(leftP.location)
        let s = QueueState(position: leftP, heatLoss: cost)
        queue.append(s)
      }

      let rightP = state.position.rotateAndStep(.clockwise)
      if grid.containsLocation(rightP.location) {
        let cost = state.heatLoss + grid.getValueAt(rightP.location)
        let s = QueueState(position: rightP, heatLoss: cost)
        queue.append(s)
      }

      let forwardP = state.position.step()
      if state.position.stepCount <= 3 && grid.containsLocation(forwardP.location) {
        let cost = state.heatLoss + grid.getValueAt(forwardP.location)
        let s = QueueState(position: forwardP, heatLoss: cost)
        queue.append(s)
      }
    }
    fatalError("Couldn't reach destination.")
  }


  private func removeCheapest(_ queue: inout [QueueState]) -> QueueState {
    // Go through the queue, and remove the state with the lowest cost.
    var lowestIndex = 0
    var lowestCost = Int.max

    for (i, s) in queue.enumerated() {
      if s.heatLoss < lowestCost {
        lowestIndex = i
        lowestCost = s.heatLoss
      }
    }

    let stateToRemove = queue.remove(at: lowestIndex)
    return stateToRemove
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
  let start: Location
  let destination: Location

  init(data: [[Int]]) {
    self.data = data
    self.rowCount = data.count
    self.columnCount = rowCount == 0 ? 0 : data[0].count
    start = Location(row: 0, column: 0)
    destination = Location(row: rowCount - 1, column: columnCount - 1)
  }


  func getValueAt(_ location: Location) -> Int {
    data[location.row][location.column]
  }


  func containsLocation(_ location: Location) -> Bool {
    (location.row >= 0 && location.row < rowCount)
    && (location.column >= 0 && location.column < columnCount)
  }
}


fileprivate struct Point {
  let row: Int
  let column: Int
}


fileprivate struct QueueState {
  /// Current position.
  let position: Position
  /// The heat loss to get to the current position.
  let heatLoss: Int
}


fileprivate struct Position: Hashable {
  let location: Location
  let direction: Direction
  /// The number of steps needed to get to the current position.
  let stepCount: Int

  /// Steps in the current direction, and returns a new `Position`.
  func step() -> Position {
    Position(
      location: self.location.nextLocation(in: direction),
      direction: direction,
      stepCount: self.stepCount + 1
    )
  }


  func rotateAndStep(_ rotation: Rotation) -> Position {
    let newDirection = direction.rotate(by: rotation)
    let location = self.location.nextLocation(in: newDirection)
    // The stepCount resets, as we are rotating, and moving.
    let p = Position(location: location, direction: newDirection, stepCount: 1)
    return p
  }
}


fileprivate struct Location: Hashable {
  let row: Int
  let column: Int

  func nextLocation(in direction: Direction) -> Location {
    switch direction {
    case .north:
      Location(row: row - 1, column: column)
    case .south:
      Location(row: row + 1, column: column)
    case .east:
      Location(row: row, column: column + 1)
    case .west:
      Location(row: row, column: column - 1)
    }
  }
}


fileprivate enum Direction {
  case north, south, east, west

  func rotate(by rotation: Rotation) -> Direction {
    let newDirection: Direction = switch (self, rotation) {
    case (.north, .clockwise):
        .east
    case (.north, .counterClockwise):
        .west
    case (.south, .clockwise):
        .west
    case (.south, .counterClockwise):
        .east
    case (.east, .clockwise):
        .south
    case (.east, .counterClockwise):
        .north
    case (.west, .clockwise):
        .north
    case (.west, .counterClockwise):
        .south
    }
    return newDirection
  }
}


fileprivate enum Rotation {
  case clockwise // 90 degrees.
  case counterClockwise // -90 degrees.
}
