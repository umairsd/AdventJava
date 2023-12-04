// Created on 12/4/23.

import Foundation
import RegexBuilder

/// --- Day 4: Scratchcards ---
/// https://adventofcode.com/2023/day/4
class Y2023Day04: Day {
  var dayNumber: Int = 04
  var year: Int = 2023
  var dataFileNumber: Int

  required init(dataFileNumber: Int) {
    self.dataFileNumber = dataFileNumber
  }

  func part1(_ lines: [String]) -> String {
    let cards = lines.compactMap { parseCard($0) }
    let totalScore = cards.map { $0.score() }.reduce(0, +)
    return "\(totalScore)"
  }


  func part2(_ lines: [String]) -> String {
    ""
  }
}

// MARK: - Parsing

extension Y2023Day04 {

  private static let cardIdRef = Reference(Int.self)
  private static let winningNumbersRef = Reference(String.self)
  private static let numbersRef = Reference(String.self)

  private static let lineRegex = Regex {
    "Card"
    OneOrMore(.whitespace)

    TryCapture(as: cardIdRef) {
      OneOrMore(.digit)
    } transform: { match in
      Int(match)
    }
    ": "
    TryCapture(as: winningNumbersRef) {
      OneOrMore(.any)
    } transform: { match in
      String(match)
    }
    " | "
    TryCapture(as: numbersRef) {
      OneOrMore(.any)
    } transform: { match in
      String(match)
    }
  }


  private func parseCard(_ line: String) -> Card? {
    guard let match = line.firstMatch(of: Self.lineRegex) else {
      return nil
    }

    let cardId = match[Self.cardIdRef]
    let winningNumbers = match[Self.winningNumbersRef]
      .trimmingCharacters(in: .whitespaces)
      .split(separator: " ")
      .compactMap { Int($0) }
    let numbers = match[Self.numbersRef]
      .trimmingCharacters(in: .whitespaces)
      .split(separator: " ")
      .compactMap { Int($0) }

    let card = Card(cardId: cardId, winningNumbers: Set(winningNumbers), numbers: numbers)
    return card
  }
}


// MARK: - Helpful Types

fileprivate struct Card {
  let cardId: Int
  let winningNumbers: Set<Int>
  let numbers: [Int]

  func score() -> Int {
    let count = numbers.filter { winningNumbers.contains($0) }.count
    let s = (count > 0) ? Int(pow(Double(2), Double(count - 1))) : 0
    return s
  }
}
