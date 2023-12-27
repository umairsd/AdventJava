// Created on 12/27/23.

import Foundation

/// --- Day 16: The Floor Will Be Lava ---
/// https://adventofcode.com/2023/day/16
/// 
class Y2023Day16: Day {
  var dayNumber: Int = 16
  var year: Int = 2023
  var dataFileNumber: Int

  required init(dataFileNumber: Int) {
    self.dataFileNumber = dataFileNumber
  }


  func part1(_ lines: [String]) -> String {
    let grid = parseGrid(lines)
    let startingBeam = BeamState(position: Position(row: 0, column: 0), direction: .east)
    let result = energizedTileCount(grid, startingAt: startingBeam)
    return "\(result)"
  }

  
  func part2(_ lines: [String]) -> String {
    let grid = parseGrid(lines)
    let rowCount = grid.count
    let columnCount = rowCount == 0 ? 0 : grid[0].count

    var startingBeams: [BeamState] = []
    for c in 0..<grid[0].count {
      startingBeams.append(BeamState(position: Position(row: 0, column: c), direction: .south))
      startingBeams.append(
        BeamState(position: Position(row: rowCount - 1, column: c), direction: .north))
    }
    for r in 0..<grid.count {
      startingBeams.append(BeamState(position: Position(row: r, column: 0), direction: .east))
      startingBeams.append(
        BeamState(position: Position(row: r, column: columnCount - 1), direction: .west))
    }

    var maxEnergizedCount = Int.min
    for beam in startingBeams {
      let energizedCount = energizedTileCount(grid, startingAt: beam)
      maxEnergizedCount = max(maxEnergizedCount, energizedCount)
    }

    return "\(maxEnergizedCount)"
  }


  private func energizedTileCount(
    _ grid: [[Tile]],
    startingAt start: BeamState
  ) -> Int {

    var energizedPositions = Set<Position>()
    // The beams that are in progress through the grid.
    var queue: [BeamState] = []
    // The set of beams that have already been handled.
    var seenBeams = Set<BeamState>()
    queue.append(start)
    seenBeams.insert(start)

    while !queue.isEmpty {
      let beam = queue.removeFirst()

      // Energize the position occupied by the beam.
      energizedPositions.insert(beam.position)

      let nextBeams = beam.nextStates(in: grid)
      for nextBeam in nextBeams {
        if seenBeams.contains(nextBeam) {
          continue
        }
        seenBeams.insert(nextBeam)
        queue.append(nextBeam)
      }
    }

    let result = energizedPositions.count
    return result
  }
}


// MARK: - Parsing

extension Y2023Day16 {

  fileprivate func parseGrid(_ lines: [String]) -> [[Tile]] {
    let grid = lines
      .filter { !$0.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty }
      .map {
        Array($0).compactMap {
          Tile.init(rawValue: $0)
        }
      }
    return grid
  }
}

// MARK: - Types

fileprivate struct Position: Hashable {
  let row: Int
  let column: Int

  func nextPosition(in grid: [[Tile]], direction: Direction) -> Position? {
    let rowCount = grid.count
    let columnCount = rowCount == 0 ? 0 : grid[0].count

    switch direction {
    case .north:
      guard row > 0 else { return nil }
      return Position(row: row - 1, column: column)

    case .south:
      guard row < rowCount - 1 else { return nil }
      return Position(row: row + 1, column: column)

    case .east:
      guard column < columnCount - 1 else { return nil }
      return Position(row: row, column: column + 1)

    case .west:
      guard column > 0 else { return nil }
      return Position(row: row, column: column - 1)
    }
  }
}


fileprivate struct BeamState: Hashable {
  let position: Position
  let direction: Direction

  func nextStates(in grid: [[Tile]]) -> [BeamState] {
    let travelDirection = direction
    let currentTile = grid[position.row][position.column]

    let nextDirections: [Direction] = switch currentTile {
    case .empty:
      [travelDirection]

    case .horizontalSplitter:
      switch travelDirection {
      case .north, .south:
        [.east, .west]
      default:
        [travelDirection]
      }

    case .verticalSplitter:
      switch travelDirection {
      case .east, .west:
        [.north, .south]
      default:
        [travelDirection]
      }

    case .forwardMirror:
      switch travelDirection {
      case .north:
        [.east]
      case .east:
        [.north]

      case .south:
        [.west]
      case .west:
        [.south]
      }

    case .backwardMirror:
      switch travelDirection {
      case .north:
        [.west]
      case .west:
        [.north]

      case .south:
        [.east]
      case .east:
        [.south]
      }
    }

    var nextStates: [BeamState] = []
    for nextDir in nextDirections {
      guard let nextPosition = position.nextPosition(in: grid, direction: nextDir) else {
        continue
      }
      nextStates.append(BeamState(position: nextPosition, direction: nextDir))
    }
    return nextStates
  }
}


fileprivate enum Tile: Character {
  case empty = "."
  case horizontalSplitter = "-"
  case verticalSplitter = "|"
  case forwardMirror = "/"
  case backwardMirror = "\\"
}


fileprivate enum Direction {
  case north
  case south
  case east
  case west
}
