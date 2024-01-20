// Created on 2/2/23.

import Foundation
import RegexBuilder

class Y2022Day05: Day {
  var dayNumber: Int = 5
  var year: Int = 2022

  required init() {}

  func part1(_ lines: [String]) -> String {
    let splits = lines.split(separator: "")
    assert(splits.count >= 2)
    let stacks = parseStacks(from: Array(splits[0]))
    let moves = splits[1].compactMap(parseMove(from:))

    for move in moves {
      // Crates are moved one at a time.
      for _ in 0..<move.quantity {
        guard let removedCrate = stacks.crates[move.srcId]?.removeLast() else { continue }
        stacks.crates[move.destinationId]?.append(removedCrate)
      }
    }

    let result: String = stacks.cratesOnTop().joined()
    return result
  }

  func part2(_ lines: [String]) -> String {
    let splits = lines.split(separator: "")
    assert(splits.count >= 2)
    let stacks = parseStacks(from: Array(splits[0]))
    let moves = splits[1].compactMap(parseMove(from:))

    for move in moves {
      // Crates are moved all at once.
      guard let srcStack = stacks.crates[move.srcId] else {
        continue
      }
      let itemsToMove = srcStack.suffix(move.quantity)
      stacks.crates[move.destinationId]?.append(contentsOf: itemsToMove)
      stacks.crates[move.srcId]?.removeLast(move.quantity)
    }

    let result: String = stacks.cratesOnTop().joined()
    return result
  }
}

// MARK: - Parse input.

extension Y2022Day05 {

  private static let moveRegex = Regex {
    "move "
    Capture {
      OneOrMore(.digit)
    }
    " from "
    Capture {
      OneOrMore(.digit)
    }
    " to "
    Capture {
      OneOrMore(.digit)
    }
  }

  private func parseMove(from line: String) -> Move? {
    guard let result = line.firstMatch(of: Self.moveRegex) else {
      return nil
    }
    guard let q = Int(result.1) else {
      return nil
    }
    return Move(quantity: q, srcId: String(result.2), destinationId: String(result.3))
  }

  private func parseStacks(from stackLines: [String]) -> Stacks {
    let maxLineWidth = stackLines.map { $0.count }.max() ?? 0
    let paddedLines = stackLines.map {
      $0.padding(toLength: maxLineWidth, withPad: " ", startingAt: 0)
    }

    let chunks: [[String]] = paddedLines.dropLast(1).map { $0.chunks(withMaxLength: 4) }
    let stackIds: [String] = (paddedLines.last?.split(separator: " ") ?? []).map {
      $0.trimmingCharacters(in: .whitespaces)
    }

    let stacks = Stacks(with: stackIds)

    assert(chunks[0].count == stackIds.count)

    for row in (0..<chunks.count).reversed() {
      for column in 0..<chunks[row].count {
        let crate = chunks[row][column]
          .trimmingCharacters(in: .whitespaces)
          .trimmingCharacters(in: CharacterSet(charactersIn: "[]"))

        guard !crate.isEmpty else { continue }

        let stackId = stackIds[column]
        stacks.crates[stackId]?.append(String(crate))
      }
    }

    return stacks
  }
}

// MARK: - Helpers.

/// A type that represents the stack of crates.
/// - note: This custom type would be unnecessary if Swift had a sorted map/dictionary.
///   Specifically, this type helps with maintaining the order of the stack ids, as my
///   implementation does not make any assumptions about the order of the stacks.
private class Stacks {
  // Map of stackId to the crates in that stack.
  var crates: [String : [String]] = [:]
  // Ordered list of stack Ids.
  let orderedStackIds: [String]

  init(with stackIds: [String]) {
    self.orderedStackIds = stackIds
    for id in orderedStackIds {
      crates[id] = []
    }
  }

  func cratesOnTop() -> [String] {
    var top: [String] = []
    for stackId in orderedStackIds {
      guard let crate = crates[stackId]?.last else { continue }
      top.append(crate)
    }
    return top
  }
}

private struct Move {
  let quantity: Int
  let srcId: String
  let destinationId: String
}


extension String {

  func chunks(withMaxLength length: Int) -> [String] {
    return stride(from: 0, to: self.count, by: length).map {
      let startIdx = self.index(self.startIndex, offsetBy: $0)
      let endIdx = self.index(startIdx, offsetBy: length, limitedBy: self.endIndex) ?? self.endIndex
      return String(self[startIdx..<endIdx])
    }
  }
}
