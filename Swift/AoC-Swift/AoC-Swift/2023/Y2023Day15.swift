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
    guard lines.count > 0 else {
      fatalError()
    }
    let tokens = lines[0]
      .split(separator: ",")
      .map { String($0.trimmingCharacters(in: .whitespaces)).customHash() }
    let result = tokens.map { $0 }.reduce(0, +)
    return "\(result)"
  }

  
  func part2(_ lines: [String]) -> String {
    ""
  }
}


extension String {

  fileprivate func customHash() -> Int {
    var result = 0
    for c in self {
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
