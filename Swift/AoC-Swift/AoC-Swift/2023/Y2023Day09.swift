// Created on 12/9/23.

import Foundation

/// --- Day 9: Mirage Maintenance ---
///
/// https://adventofcode.com/2023/day/9
class Y2023Day09: Day {
  var dayNumber: Int = 09
  var year: Int = 2023
  var dataFileNumber: Int

  required init(dataFileNumber: Int) {
    self.dataFileNumber = dataFileNumber
  }

  func part1(_ lines: [String]) -> String {
    let sequences = lines
      .filter { !$0.isEmpty }
      .compactMap { parseSequence($0) }

    let nextValues = sequences.map { nextValueForSequence($0) }
    let result = nextValues.reduce(0, +)
    return "\(result)"
  }


  func part2(_ lines: [String]) -> String {
    let sequences = lines
      .filter { !$0.isEmpty }
      .compactMap { parseSequence($0) }

    let previousValues = sequences.map { previousValueForSequence($0) }
    let result = previousValues.reduce(0, +)
    return "\(result)"
  }


  private func nextValueForSequence(_ sequence: [Int]) -> Int {
    guard sequence.count >= 2 else {
      return 0
    }

    let steps = processUntilAllZero(sequence)
    var nextValue = 0 // lastSequence is zero.
    for sequence in steps.reversed() {
      nextValue = nextValue + sequence.last!
    }
    return nextValue
  }


  private func previousValueForSequence(_ sequence: [Int]) -> Int {
    guard sequence.count >= 2 else {
      return 0
    }

    let steps = processUntilAllZero(sequence)
    var previousValue = 0 // lastSequence is zero.
    for sequence in steps.reversed() {
      previousValue = sequence.first! - previousValue
    }
    return previousValue
  }


  private func processUntilAllZero(_ sequence: [Int]) -> [[Int]] {
    guard sequence.count >= 2 else {
      return []
    }

    var steps: [[Int]] = []
    steps.append(sequence)
    var currentSequence = sequence
    while !currentSequence.allSatisfy({ $0 == 0 }) {
      currentSequence = diffSequence(currentSequence)
      steps.append(currentSequence)
    }
    return steps
  }



  /// A sequence containing the difference between each value.
  private func diffSequence(_ sequence: [Int]) -> [Int] {
    guard sequence.count >= 2 else {
      fatalError()
    }
    var diff: [Int] = []
    for i in 1..<sequence.count {
      diff.append(sequence[i] - sequence[i - 1])
    }
    return diff
  }
}

// MARK: - Parsing

extension Y2023Day09 {

  func parseSequence(_ line: String) -> [Int] {
    line.split(separator: " ").compactMap { Int($0.trimmingCharacters(in: .whitespaces)) }
  }
}
