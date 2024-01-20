// Created on 3/27/23.

import Foundation

class Y2022Day08: Day {
  var dayNumber: Int = 8
  var year: Int = 2022

  required init() {}

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
    let trees = parseTrees(lines)
    var maxScenicScore = Int.min

    for row in 0..<trees.count {
      for column in 0..<trees[row].count {
        let score = scenicScore(trees, row, column)
        maxScenicScore = max(maxScenicScore, score)
      }
    }

    return "\(maxScenicScore)"
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


  private func scenicScore(_ trees: [[Int]], _ row: Int, _ column: Int) -> Int {
    let rowCount = trees.count
    let columnCount = trees[0].count
    let treeHeight = trees[row][column]

    // Look towards the left.
    var seenOnLeft = 0
    var c = column - 1
    while c >= 0 {
      seenOnLeft += 1
      if trees[row][c] >= treeHeight {
        // A tree as tall as `treeHeight` blocks our view, we cannot look any further.
        break
      }
      c -= 1
    }

    // Look towards the right.
    var seenOnRight = 0
    c = column + 1
    while c < columnCount {
      seenOnRight += 1
      if trees[row][c] >= treeHeight {
        break
      }
      c += 1
    }

    // Look above.
    var seenAbove = 0
    var r = row - 1
    while r >= 0 {
      seenAbove += 1
      if trees[r][column] >= treeHeight {
        break
      }
      r -= 1
    }

    // Look below.
    var seenBelow = 0
    r = row + 1
    while r < rowCount {
      seenBelow += 1
      if trees[r][column] >= treeHeight {
        break
      }
      r += 1
    }

    let score = seenOnLeft * seenOnRight * seenAbove * seenBelow
    return score
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
