// Created on 12/1/23.

import Foundation

/// --- Day 1: Trebuchet?! ---
/// https://adventofcode.com/2023/day/1
class Y2023Day01: Day {
  var dayNumber: Int = 1
  var year: Int = 2023
  var dataFileNumber: Int

  required init(dataFileNumber: Int) {
    self.dataFileNumber = dataFileNumber
  }

  func part1(_ lines: [String]) -> String {
    let result = lines.reduce(0) { partialResult, s in
      partialResult + parseIntoTwoDigitNumber_v1(s)
    }
    return "\(result)"
  }

  func part2(_ lines: [String]) -> String {
    let result = lines.reduce(0) { partialResult, s in
      partialResult + parseIntoTwoDigitNumber_v2(s)
    }
    return "\(result)"
  }
}


// MARK: - Parsing

extension Y2023Day01 {

  private static let numbersMap = [
    "1": 1,
    "one": 1,
    "2": 2,
    "two": 2,
    "3": 3,
    "three": 3,
    "4": 4,
    "four": 4,
    "5": 5,
    "five": 5,
    "6": 6,
    "six": 6,
    "7": 7,
    "seven": 7,
    "8": 8,
    "eight": 8,
    "9": 9,
    "nine": 9,
    "0": 0,
    "zero": 0
  ]


  /// Takes a line, drops all the non-numeric values, and creates a number from
  /// the first and the last digits.
  private func parseIntoTwoDigitNumber_v1(_ line: String) -> Int {
    let numbersOnly = line.drop { !$0.isNumber }.compactMap { Int(String($0)) }
    let number = (numbersOnly.first ?? 0) * 10 + (numbersOnly.last ?? 0)
    return number
  }


  /// First the first and the last digits in the line (of the form 1, 2, or "one, "two"), and
  /// generates a number from these digits.
  private func parseIntoTwoDigitNumber_v2(_ line: String) -> Int {
    let firstDigit = firstDigit(line, numbersMap: Self.numbersMap) ?? 0
    let lastDigit = lastDigit(line, numbersMap: Self.numbersMap) ?? 0
    return (firstDigit * 10 + lastDigit)
  }
  

  /// Finds the first digit in the given string.
  private func firstDigit(_ line: String, numbersMap: [String: Int]) -> Int? {
    var firstIndex = Int.max
    var firstDigit: Int?
    for num in numbersMap.keys {
      guard let i = line.startIndex(of: num) else {
        continue
      }

      if i < firstIndex {
        firstIndex = i
        firstDigit = numbersMap[num]
      }
    }
    return firstDigit
  }


  /// Finds the last digit in the given string.
  private func lastDigit(_ line: String, numbersMap: [String: Int]) -> Int? {
    var lastIndex = Int.min
    var lastDigit: Int?
    for num in numbersMap.keys {
      guard let i = line.lastIndexOf(num) else {
        continue
      }
      if i > lastIndex {
        lastIndex = i
        lastDigit = numbersMap[num]
      }
    }
    return lastDigit
  }
}
