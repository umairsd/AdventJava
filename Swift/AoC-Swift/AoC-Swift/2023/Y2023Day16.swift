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
    var energizedPositions = Set<Position>()
    
    // The beams that are in progress through the grid.
    var beams: [Beam] = []
    // The set of beams that have already been handled.
    var processedBeams = Set<Beam>()
    let start = Beam(position: Position(row: 0, column: 0), direction: .east)
    beams.append(start)
    processedBeams.insert(start)

    while !beams.isEmpty {
      let beam = beams.removeFirst()
      let currentPosition = beam.position
      let currentTile = grid[currentPosition.row][currentPosition.column]

      // Energize the position occupied by the beam.
      energizedPositions.insert(beam.position)

      let nextDirections = currentTile.nextDirectionsWhenTravelingIn(beam.direction)
      for nextDir in nextDirections {
        guard let p = currentPosition.nextPosition(in: nextDir, within: grid) else {
          continue
        }
        let b = Beam(position: p, direction: nextDir)
        if !processedBeams.contains(b) {
          processedBeams.insert(b)
          beams.append(b)
        }
      }
    }

    let result = energizedPositions.count
    return "\(result)"
  }

  
  func part2(_ lines: [String]) -> String {
    ""
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

  func nextPosition(in direction: Direction, within grid: [[Tile]]) -> Position? {
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


fileprivate struct Beam: Hashable {
  let position: Position
  let direction: Direction
}


fileprivate enum Tile: Character {
  case empty = "."
  case horizontalSplitter = "-"
  case verticalSplitter = "|"
  case forwardMirror = "/"
  case backwardMirror = "\\"

  func nextDirectionsWhenTravelingIn(_ travelDirection: Direction) -> [Direction] {
    let next: [Direction] = switch self {
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

    return next
  }
}

fileprivate enum Direction {
  case north
  case south
  case east
  case west
}
