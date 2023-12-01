// Created on 12/1/23.

import Foundation

class Y2023Day01: Day {
  var dayNumber: Int = 1
  var year: Int = 2023
  var dataFileNumber: Int

  required init(dataFileNumber: Int) {
    self.dataFileNumber = dataFileNumber
  }

  func part1(_ lines: [String]) -> String {
    let result = lines.reduce(0) { partialResult, s in
      partialResult + parseCalibrationValue(s)
    }
    return "\(result)"
  }

  func part2(_ lines: [String]) -> String {
    ""
  }
}


// MARK: - Parsing

extension Y2023Day01 {

  /// Takes a line, drops all the non-numeric values, and creates a number from
  /// the first and the last digits.
  private func parseCalibrationValue(_ line: String) -> Int {
    let numbersOnly = line.drop { !$0.isNumber }.compactMap { Int(String($0)) }
    let number = (numbersOnly.first ?? 0) * 10 + (numbersOnly.last ?? 0)
    return number
  }

  private func parseCalibrationValue2(_ line: String) -> Int {
    let numbersOnly = line.drop { !$0.isNumber }.compactMap { Int(String($0)) }
    let number = (numbersOnly.first ?? 0) * 10 + (numbersOnly.last ?? 0)
    return number
  }
}
