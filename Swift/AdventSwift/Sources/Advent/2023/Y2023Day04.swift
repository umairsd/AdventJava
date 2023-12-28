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
    let totalScore = cards.map { $0.score }.reduce(0, +)
    return "\(totalScore)"
  }


  func part2(_ lines: [String]) -> String {
    let cards = lines.compactMap { parseCard($0) }

    // Map of cardId to the corresponding count.
    var cardCounts: [Int: Int] = [:]
    cards.forEach { cardCounts[$0.cardId] = 1 }

    for i in 0..<cards.count {
      let currentCard = cards[i]
      let countOfCurrentCard = cardCounts[currentCard.cardId]!
      let score = currentCard.matchingCount
      (0..<score).forEach {
        let cardToAdd = cards[i + 1 + $0]
        cardCounts[cardToAdd.cardId] = cardCounts[cardToAdd.cardId]! + countOfCurrentCard
      }
    }

    let totalCards = cardCounts.values.reduce(0, +)
    return "\(totalCards)"
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
  let matchingCount: Int
  let score: Int

  init(cardId: Int, winningNumbers: Set<Int>, numbers: [Int]) {
    self.cardId = cardId
    self.winningNumbers = winningNumbers
    self.numbers = numbers
    matchingCount = numbers.filter { winningNumbers.contains($0) }.count
    score = (matchingCount > 0) ? Int(pow(Double(2), Double(matchingCount - 1))) : 0
  }
}
