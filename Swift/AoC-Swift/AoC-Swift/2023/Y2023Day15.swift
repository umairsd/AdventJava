// Created on 12/26/23.

import Foundation

/// --- Day 15: Lens Library ---
/// https://adventofcode.com/2023/day/15
///
class Y2023Day15: Day {
  var dayNumber: Int = 15
  var year: Int = 2023
  var dataFileNumber: Int

  required init(dataFileNumber: Int) {
    self.dataFileNumber = dataFileNumber
  }


  func part1(_ lines: [String]) -> String {
    let tokens = parseTokens(lines[0])
    let result = tokens.map { $0.hashValue() }.reduce(0, +)
    return "\(result)"
  }

  
  func part2(_ lines: [String]) -> String {
    ""
  }


  private func parseTokens(_ line: String) -> [Token] {
    let t = line.split(separator: ",")
      .map { Token(data: String($0.trimmingCharacters(in: .whitespaces))) }
    return t
  }
}


fileprivate struct Token {
  let data: String

  func hashValue() -> Int {
    var result = 0
    for c in data {
      guard let ascii = c.asciiValue else {
        continue
      }
      result += Int(ascii)
      result *= 17
      result %= 256
    }
    return result
  }
}
