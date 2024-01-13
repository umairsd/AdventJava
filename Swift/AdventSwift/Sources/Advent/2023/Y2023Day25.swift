// Created on 1/12/24.

import Foundation
import RegexBuilder

/// --- Day 25: Snowverload ---
/// https://adventofcode.com/2023/day/25
/// 
class Y2023Day25: Day {
  var dayNumber: Int = 25
  var year: Int = 2023
  var dataFileNumber: Int

  required init(dataFileNumber: Int) {
    self.dataFileNumber = dataFileNumber
  }


  func part1(_ lines: [String]) -> String {
    let graph = parseGraph(lines)
    return ""
  }


  func part2(_ lines: [String]) -> String {
    ""
  }
}


// MARK: - Parsing

fileprivate extension Y2023Day25 {
  private static let nodeRef = Reference(String.self)
  private static let remainingRef = Reference(String.self)

  private static let lineRegex = Regex {
    TryCapture(as: nodeRef) {
      OneOrMore(.any)
    } transform: { w in
      String(w)
    }
    ": "
    TryCapture(as: remainingRef) {
      OneOrMore(.any)
    } transform: { w in
      String(w)
    }
  }

  func parseChildren(_ line: String) -> [String] {
    let line = line.trimmingCharacters(in: .whitespacesAndNewlines)
    guard !line.isEmpty else {
      return []
    }

    let tokens = line
      .split(separator: " ")
      .map { String($0) }
    return tokens
  }

  func parseLine(_ line: String) -> [String] {
    guard let match = line.firstMatch(of: Self.lineRegex) else {
      return []
    }
    return [match[Self.nodeRef]] + parseChildren(match[Self.remainingRef])
  }


  func parseGraph(_ lines: [String]) -> [String: [String]] {
    var graph: [String: [String]] = [:]
    for line in lines {
      guard !line.isEmpty else {
        continue
      }
      let nodes = parseLine(line)
      // First node is the src.
      assert(nodes.count > 0)
      nodes.forEach { graph[$0] = graph[$0, default: []] }

      let src = nodes[0]
      graph[src] = Array(nodes.suffix(from: 1))
    }

    return graph
  }
}
