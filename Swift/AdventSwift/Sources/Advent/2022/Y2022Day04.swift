// Created on 2/1/23.

import Foundation
import RegexBuilder

class Y2022Day04: Day {
  var dayNumber: Int = 4
  var year: Int = 2022
  
  required init() {}


  func part1(_ lines: [String]) -> String {
    let ranges: [ClosedRangePair] = lines.compactMap(parseRangePair(from:))
    let fullyOverlapping = ranges.filter {
      $0.first.fullyContains($0.second) || $0.second.fullyContains($0.first)
    }
    return "\(fullyOverlapping.count)"
  }

  func part2(_ lines: [String]) -> String {
    let ranges: [ClosedRangePair] = lines.compactMap(parseRangePair(from:))
    let overlapping = ranges.filter { $0.first.overlaps($0.second) }
    return "\(overlapping.count)"
  }
}

extension ClosedRange<Int> {

  fileprivate func fullyContains(_ other: ClosedRange<Int>) -> Bool {
    guard self.overlaps(other) else {
      return false
    }
    let clampedRange = self.clamped(to: other)
    return clampedRange == other
  }
}


// MARK: - Parsing

extension Y2022Day04 {

  private static let searchRegex = Regex {
    Capture {
      OneOrMore(.digit)
    }
    "-"
    Capture {
      OneOrMore(.digit)
    }
    ","
    Capture {
      OneOrMore(.digit)
    }
    "-"
    Capture {
      OneOrMore(.digit)
    }
  }

  fileprivate func parseRangePair(from line: String) -> ClosedRangePair? {
    guard let result = line.firstMatch(of: Self.searchRegex) else {
      return nil
    }

    guard let s1 = Int(result.1),
          let e1 = Int(result.2),
          let s2 = Int(result.3),
          let e2 = Int(result.4)
    else {
      return nil
    }
    return ClosedRangePair(first: s1...e1, second: s2...e2)
  }
}

private struct ClosedRangePair {
  let first: ClosedRange<Int>
  let second: ClosedRange<Int>
}
