// Created on 12/3/23.

import Foundation

/// --- Day 3: Gear Ratios ---
/// https://adventofcode.com/2023/day/3
class Y2023Day03: Day {
  var dayNumber: Int = 3
  var year: Int = 2023
  
  required init() {}

  func part1(_ lines: [String]) -> String {
    let grid = lines
      .filter { !$0.isEmpty}
      .map { Array($0) }

    let partNumbers = getValidPartNumbers(grid)
    let sum = partNumbers.map { $0.number }.reduce(0, +)
    return "\(sum)"
  }

  
  func part2(_ lines: [String]) -> String {
    let grid = lines
      .filter { !$0.isEmpty}
      .map { Array($0) }

    let partNumbers = getValidPartNumbers(grid)
    var positionToPartNumbers: [Position: PartNumber] = [:]
    for pn in partNumbers {
      pn.positions.forEach { positionToPartNumbers[$0] = pn }
    }
    let gearRatios = getGearRatios(in: grid, using: positionToPartNumbers)
    let sum = gearRatios.reduce(0, +)
    return "\(sum)"
  }

  // MARK: - Private


  /// Traverses the grid, and when it encounters a numeric cell, builds a number. For each such
  /// number, it keeps a record of its position, and then checks the neighbors of all these
  /// positions to see if they are a symbol. If so, adds this number to the list of valid
  /// part numbers.
  private func getValidPartNumbers(_ grid: [[Character]]) -> [PartNumber] {
    var partNumbers: [PartNumber] = []

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

          let neighbors = neighborsOfAllPositions(positionSet, in: grid)
          // Go through the neighbors of these positions, and if any of these is a symbol, it means
          // the current number is a valid part number.
          for neighbor in neighbors {
            let v = grid[neighbor.row][neighbor.column]
            if v != "." && !v.isNumber {
              partNumbers.append(PartNumber(number: number, positions: positionSet))
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


  /// Traverses the grid, and for each cell that's a gear i.e. `*`, get the neighboring part
  /// numbers (use a set for uniqueness). If there are exactly two part numbers, get their
  /// product (gear ratio) and add it to the result.
  private func getGearRatios(
    in grid: [[Character]],
    using positionToPartsMap: [Position: PartNumber]
  ) -> [Int] {

    var gearRatios: [Int] = []
    for (r, row) in grid.enumerated() {
      for (c, value) in row.enumerated() {
        if value != "*" { continue }

        let p = Position(row: r, column: c)
        // Get the neighbors that contains part numbers.
        let neighboringPositions = p.allNeighbors().filter {
          isValid($0, in: grid) && grid[$0.row][$0.column].isNumber
        }

        var gears = Set<PartNumber>()
        // Filter the neighboring positions to the ones that have a corresponding part number,
        // and add them to the gears set.
        neighboringPositions
          .filter { positionToPartsMap[$0] != nil }
          .forEach { gears.insert(positionToPartsMap[$0]!) }

        if gears.count == 2 {
          let ratio = gears.map { $0.number }.reduce(1, *)
          gearRatios.append(ratio)
        }
      }
    }

    return gearRatios
  }


  private func neighborsOfAllPositions(
    _ positions: Set<Position>,
    in grid: [[Character]]
  ) -> [Position] {

    var neighbors = Set<Position>()
    for p in positions {
      // Valid neighbor that's not one of the positions in the positions set.
      let validNeighbors = p.allNeighbors().filter { isValid($0, in: grid) && !positions.contains($0) }
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


fileprivate struct PartNumber: Hashable {
  let number: Int
  let positions: Set<Position>
}
