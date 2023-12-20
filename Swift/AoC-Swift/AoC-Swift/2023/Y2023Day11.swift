// Created on 12/19/23.

import Foundation

/// --- Day 11: Cosmic Expansion ---
/// https://adventofcode.com/2023/day/11
///
class Y2023Day11: Day {
  var dayNumber: Int = 11
  var year: Int = 2023
  var dataFileNumber: Int

  required init(dataFileNumber: Int) {
    self.dataFileNumber = dataFileNumber
  }

  func part1(_ lines: [String]) -> String {
    let image = parseAndExpandImage(lines)
    let galaxyPositions = galaxyPositions(in: image)

    var distances: [Int] = []
    for i in 0..<galaxyPositions.count {
      let p1 = galaxyPositions[i]
      for j in (i + 1)..<galaxyPositions.count {
        distances.append(p1.distance(to: galaxyPositions[j]))
      }
    }

    let result = distances.reduce(0, +)
    return "\(result)"
  }

  
  func part2(_ lines: [String]) -> String {
    ""
  }


  // MARK: Private

  private func galaxyPositions(in image: [[Pixel]]) -> [Position] {
    var positions: [Position] = []
    for (r, row) in image.enumerated() {
      for (c, pixel) in row.enumerated() {
        if pixel == .galaxy {
          positions.append(Position(row: r, column: c))
        }
      }
    }
    return positions
  }
}


// MARK: - Parsing

extension Y2023Day11 {

  fileprivate func parseAndExpandImage(_ lines: [String]) -> [[Pixel]] {
    let image = parseImage(lines)
    return expandImage(image)
  }

  fileprivate func parseImage(_ lines: [String]) -> [[Pixel]] {
    let image: [[Pixel]] = lines
      .filter { !$0.isEmpty } 
      .map { l in
        Array(l).compactMap { Pixel.init(rawValue: $0) }
      }
    return image
  }


  fileprivate func expandImage(_ image: [[Pixel]]) -> [[Pixel]] {
    var rowExpandedImage: [[Pixel]] = []
    for (r, row) in image.enumerated() {
      rowExpandedImage.append(row)
      if isRowEmpty(r, in: image) {
        rowExpandedImage.append(row)
      }
    }

    var columnExpandedImage: [[Pixel]] = Array(repeating: [], count: rowExpandedImage.count)
    for c in 0..<rowExpandedImage[0].count {
      let emptyColumn = isColumnEmpty(c, in: rowExpandedImage)

      for r in 0..<rowExpandedImage.count {
        let value = rowExpandedImage[r][c]
        columnExpandedImage[r].append(value)
        if emptyColumn {
          columnExpandedImage[r].append(value)
        }
      }
    }

    return columnExpandedImage
  }


  private func isRowEmpty(_ r: Int, in image: [[Pixel]]) -> Bool {
    guard r >= 0 && r < image.count else {
      return false
    }
    return image[r].allSatisfy { $0 == .empty }
  }


  private func isColumnEmpty(_ c: Int, in image: [[Pixel]]) -> Bool {
    guard c >= 0 && c < image[0].count else {
      return false
    }
    return image.map { $0[c] }.allSatisfy { $0 == .empty }
  }
}

// MARK: - Helper Types


fileprivate struct Position {
  let row: Int
  let column: Int

  func distance(to: Position) -> Int {
    return abs(row - to.row) + abs(column - to.column)
  }
}


fileprivate enum Pixel: Character {
  case empty = "."
  case galaxy = "#"
}
