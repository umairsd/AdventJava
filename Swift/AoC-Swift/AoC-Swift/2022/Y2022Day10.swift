// Created on 4/27/23.

import Foundation
import RegexBuilder

/// --- Day 10: Cathode-Ray Tube ---
class Y2022Day10: Day {
  var dayNumber: Int = 10
  var year: Int = 2022
  var dataFileNumber: Int = 2

  required init(dataFileNumber: Int) {
    self.dataFileNumber = dataFileNumber
  }

  func part1(_ lines: [String]) -> String {
    let instructions = parseInstructions(from: lines)
    return ""
  }

  func part2(_ lines: [String]) -> String {
    ""
  }
}


// MARK: - Helpful Types

private enum Instruction {
  case noop
  case addx(value: Int)
}


// MARK: - Parsing

extension Y2022Day10 {
  private static let instructionRef = Reference(Instruction.self)

  private static let noop = "noop"

  private static let noopPattern = Regex {
    TryCapture(as: instructionRef) {
      noop
    } transform: { match in
      .noop
    }
  }

  private static let addXPattern = Regex {
    "addx "
    TryCapture(as: instructionRef) {
      OneOrMore(.any)
    } transform: { match in
      Instruction.addx(value: Int(match)!)
    }
  }

  private func parseInstructions(from lines: [String]) -> [Instruction] {
    let instructions = lines.compactMap { parseInstruction($0) }
    return instructions
  }

  private func parseInstruction(_ line: String) -> Instruction? {
    if let m1 = line.firstMatch(of: Self.noopPattern) {
      return m1[Self.instructionRef]

    } else if let m2 = line.firstMatch(of: Self.addXPattern) {
      return m2[Self.instructionRef]
    }
    return nil
  }
}

