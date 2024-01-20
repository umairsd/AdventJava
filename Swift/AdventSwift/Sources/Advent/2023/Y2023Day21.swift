// Created on 1/20/24.

import Foundation

/// --- Day 21: Step Counter ---
/// https://adventofcode.com/2023/day/21
/// 
class Y2023Day21: Day {
  var dayNumber: Int = 21
  var year: Int = 2023
  
  required init() {}


  func part1Example(_ lines: [String]) -> String {
    let garden = parseGarden(lines)
    let result = solvePart1ByTakingSteps(6, in: garden)
    return "\(result)"
  }

  func part1(_ lines: [String]) -> String {
    let garden = parseGarden(lines)
    let result = solvePart1ByTakingSteps(64, in: garden)
    return "\(result)"
  }


  func part2(_ lines: [String]) -> String {
    return ""
  }


  // MARK: - Private


  private func solvePart1ByTakingSteps(_ steps: Int, in garden: Grid<GardenTile>) -> String {
    guard let start = garden.firstPosition(of: .start) else {
      fatalError("Parsing error. There's no starting position in the garden.")
    }
    let result = positionsVisitedFrom(start, in: garden, takingSteps: steps)
    return "\(result)"
  }


  private func positionsVisitedFrom(
    _ start: Position, 
    in garden: Grid<GardenTile>,
    takingSteps allowedSteps: Int
  ) -> Int {

    var queue: [(Int, Position)] = []
    queue.append((0, start))

    var seenPositions = Set<Position>()
    var visited: [(Int, Position)] = []

    while !queue.isEmpty {
      let (steps, tilePosition) = queue.removeFirst()

      if steps > allowedSteps || seenPositions.contains(tilePosition) {
        continue
      }

      visited.append((steps, tilePosition))
      seenPositions.insert(tilePosition)

      let neighboringPositions = garden.neighbors(of: tilePosition)
      let openPositions = neighboringPositions.filter { garden.getValueAt($0) == .open }
      for p in openPositions {
        guard !seenPositions.contains(p) else { continue }
        queue.append((steps + 1, p))
      }
    }

    // Note:
    // https://github.com/villuna/aoc23/wiki/A-Geometric-solution-to-advent-of-code-2023,-day-21
    //
    // Each tile (position) has a parity. A tile can either be reached in an even number of
    // steps or an odd number of steps. There are NO tiles that can be reached in an odd 
    // number of steps through one path, and an even number of steps through a different path.
    if allowedSteps % 2 == 0 {
      let tilesVisitedInEvenSteps = visited.filter { $0.0 % 2 == 0 }.count
      return tilesVisitedInEvenSteps
    } else {
      let tilesVisitedInOddSteps = visited.filter { $0.0 % 2 == 1 }.count
      return tilesVisitedInOddSteps
    }
  }
}


// MARK: - Parsing

fileprivate extension Y2023Day21 {

  func parseGarden(_ lines: [String]) -> Grid<GardenTile> {
    let data: [[GardenTile]] = lines
      .filter({ !$0.trimmingCharacters(in: .whitespaces).isEmpty })
      .map {
        Array($0).compactMap {
          GardenTile.init(rawValue: $0)
        }
      }
    return Grid(data: data)
  }
}


fileprivate enum GardenTile: Character {
  case open = "."
  case rock = "#"
  case start = "S"
}
