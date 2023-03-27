// Created on 3/27/23.

import Foundation

class Y2022Day08: Day {
  var dayNumber: Int = 8
  var year: Int = 2022
  var dataFileNumber: Int = 2

  required init(dataFileNumber: Int) {
    self.dataFileNumber = dataFileNumber
  }

  func part1(_ lines: [String]) -> String {
    let trees = parseTrees(lines)
    let tallestOnRight = tallestOnRight(trees)
    let tallestOnLeft = tallestOnLeft(trees)
    let tallestAbove = tallestAbove(trees)
    let tallestBelow = tallestBelow(trees)

    let edgeCount = trees.count * 2 + (trees[0].count - 2) * 2
    var visibleCount = edgeCount

    for row in 1..<(trees.count - 1) {
      for column in 1..<(trees[row].count - 1) {
        let height = trees[row][column]

        let isVisibleFromLeft = height > tallestOnLeft[row][column]
        let isVisibleFromRight = height > tallestOnRight[row][column]
        let isVisibleFromAbove = height > tallestAbove[row][column]
        let isVisibleFromBelow = height > tallestBelow[row][column]

        if isVisibleFromLeft || isVisibleFromRight || isVisibleFromAbove || isVisibleFromBelow {
          visibleCount += 1
        }
      }
    }

    return "\(visibleCount)"
  }

  func part2(_ lines: [String]) -> String {
    ""
  }
}


// MARK: - Helpers

extension Y2022Day08 {

  private func parseTrees(_ lines: [String]) -> [[Int]] {
    let trees: [[Int]] = lines
      .filter( { !$0.isEmpty })
      .map( { $0.split(separator: "") })
      .map( { row in
        let integerRow = row.compactMap( { Int($0) })
        return integerRow
      })

    return trees
  }


  private func tallestOnLeft(_ trees: [[Int]]) -> [[Int]] {
    var tallest = emptyArrayOfSameSize(trees)

    for row in 0..<trees.count {
      for column in 1..<trees[row].count {
        let maxHeightSeenSoFar = max(trees[row][column - 1], tallest[row][column - 1])
        tallest[row][column] = maxHeightSeenSoFar
      }
    }
    return tallest
  }


  private func tallestOnRight(_ trees: [[Int]]) -> [[Int]] {
    var tallest = emptyArrayOfSameSize(trees)

    for row in 0..<trees.count {
      for column in (0..<(trees[row].count - 1)).reversed() {
        let maxHeightSeenSoFar = max(trees[row][column + 1], tallest[row][column + 1])
        tallest[row][column] = maxHeightSeenSoFar
      }
    }
    return tallest
  }


  private func tallestAbove(_ trees: [[Int]]) -> [[Int]] {
    var tallest = emptyArrayOfSameSize(trees)

    for column in 0..<trees[0].count {
      for row in 1..<trees.count {
        let maxHeightSeenSoFar = max(tallest[row - 1][column], trees[row - 1][column])
        tallest[row][column] = maxHeightSeenSoFar
      }
    }
    return tallest
  }


  private func tallestBelow(_ trees: [[Int]]) -> [[Int]] {
    var tallest = emptyArrayOfSameSize(trees)
    let lastRowIndex = trees.count - 1

    for column in 0..<trees[0].count {
      for row in (0..<lastRowIndex).reversed() {
        let maxHeightSeenSoFar = max(tallest[row + 1][column], trees[row + 1][column])
        tallest[row][column] = maxHeightSeenSoFar
      }
    }
    return tallest
  }


  private func emptyArrayOfSameSize(_ trees: [[Int]]) -> [[Int]] {
    let duplicate: [[Int]] = Array(
      repeating: Array(repeating: 0, count: trees[0].count),
      count: trees.count)
    return duplicate
  }
}
