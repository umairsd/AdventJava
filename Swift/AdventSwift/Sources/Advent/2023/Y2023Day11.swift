// Created on 12/19/23.

import Foundation

/// --- Day 11: Cosmic Expansion ---
/// https://adventofcode.com/2023/day/11
///
class Y2023Day11: Day {
  var dayNumber: Int = 11
  var year: Int = 2023

  required init() {}

  func part1(_ lines: [String]) -> String {
    let image = parseImage(lines)
    let expandedImage = expandImage(image, multiplier: 2)
    let galaxyPositions = galaxyPositions(in: expandedImage)

    var distances: [Int] = []
    for i in 0..<galaxyPositions.count {
      let p1 = galaxyPositions[i]
      for j in (i + 1)..<galaxyPositions.count {
        distances.append(p1.manhattanDistance(to: galaxyPositions[j]))
      }
    }

    let result = distances.reduce(0, +)
    return "\(result)"
  }

  
  func part2(_ lines: [String]) -> String {
    let image = parseImage(lines)
    let expandedImage = expandImage(image, multiplier: 1000_000)
    let galaxyPositions = galaxyPositions(in: expandedImage)

    var distances: [Int] = []
    for i in 0..<galaxyPositions.count {
      let p1 = galaxyPositions[i]
      for j in (i + 1)..<galaxyPositions.count {
        distances.append(p1.manhattanDistance(to: galaxyPositions[j]))
      }
    }

    let result = distances.reduce(0, +)
    return "\(result)"
  }


  // MARK: Private

  private func galaxyPositions(in image: [[Pixel]]) -> [Position] {
    var positions: [Position] = []
    for row in image {
      for pixel in row {
        if case .galaxy(let maybePosition) = pixel, let position = maybePosition {
          positions.append(position)
        }
      }
    }
    return positions
  }
}


// MARK: - Parsing

extension Y2023Day11 {

  fileprivate func parseImage(_ lines: [String]) -> [[Pixel]] {
    let image: [[Pixel]] = lines
      .filter { !$0.isEmpty } 
      .map { l in
        Array(l).compactMap { Pixel.pixelFrom($0) }
      }
    return image
  }


  fileprivate func expandImage(_ img: [[Pixel]], multiplier: Int) -> [[Pixel]] {
    var image = img
    var verticalCount = 0
    for (r, row) in image.enumerated() {
      for (c, pixel) in row.enumerated() {
        if case .galaxy(_) = pixel  {
          image[r][c] = .galaxy(Position(row: verticalCount, column: c))
        }
      }
      let delta = isRowEmpty(r, in: image) ? multiplier : 1
      verticalCount += delta
    }

    let columnCount = image[0].count
    var horizontalCount = 0
    for c in 0..<columnCount {
      for (r, row) in image.enumerated() {
        if case .galaxy(let maybePosition) = row[c], let oldP = maybePosition {
          let newPosition = Position(row: oldP.row, column: horizontalCount)
          image[r][c] = .galaxy(newPosition)
        }
      }

      let delta = isColumnEmpty(c, in: image) ? multiplier : 1
      horizontalCount += delta
    }

    return image
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

fileprivate enum Pixel: Equatable {
  case empty
  case galaxy(Position?)

  static func pixelFrom(_ character: Character) -> Pixel? {
    if character == "." {
      return .empty
    } else if character == "#" {
      return .galaxy(nil)
    }
    return nil
  }
}
