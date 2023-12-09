// Created on 12/7/23.

import Foundation
import RegexBuilder


/// --- Day 7: Camel Cards ---
/// https://adventofcode.com/2023/day/7
class Y2023Day07: Day {
  var dayNumber: Int = 07
  var year: Int = 2023
  var dataFileNumber: Int

  required init(dataFileNumber: Int) {
    self.dataFileNumber = dataFileNumber
  }

  
  func part1(_ lines: [String]) -> String {
    let deals = lines.compactMap { parseHands($0) }
    let sortedDeals = deals.sorted { h1, h2 in
      // Note: reverse order.
      h1.hand > h2.hand
    }

    var totalWinnings = 0
    for (rank, handInfo) in sortedDeals.enumerated() {
      totalWinnings += (rank + 1) * handInfo.bid
    }
    return "\(totalWinnings)"
  }


  func part2(_ lines: [String]) -> String {
    ""
  }
}


// MARK: - Parsing

extension Y2023Day07 {


  private static let cardsListRef = Reference([Card].self)
  private static let bidRef = Reference(Int.self)

  private static let lineRegex = Regex {
    TryCapture(as: cardsListRef) {
      OneOrMore(.any)
    } transform: { match in
      let charArray: [Character] = Array(String(match))
      return charArray.map { String($0) }.map { Card(value: $0) }
    }
    " "
    TryCapture(as: bidRef) {
      OneOrMore(.digit)
    } transform: { match in
      Int(match)
    }
  }


  fileprivate func parseHands(_ line: String) -> HandInfo? {
    guard let match = line.firstMatch(of: Self.lineRegex) else {
      return nil
    }
    let cardsList = match[Self.cardsListRef]
    let hand = Hand(cards: cardsList)
    let bid = match[Self.bidRef]
    return HandInfo(hand: hand, bid: bid)
  }

}


// MARK: - Helpful Types

fileprivate struct HandInfo {
  let hand: Hand
  let bid: Int
}


fileprivate struct Card: Comparable, Hashable {
  private static let sortOrder: [String: Int] = [
    "A": 0,
    "K": 1,
    "Q": 2,
    "J": 3,
    "T": 4,
    "9": 5,
    "8": 6,
    "7": 7,
    "6": 8,
    "5": 9,
    "4": 10,
    "3": 11,
    "2": 12
  ]

  let value: String

  static func < (lhs: Card, rhs: Card) -> Bool {
    return lhs.sortOrder() < rhs.sortOrder()
  }

  func sortOrder() -> Int {
    return Self.sortOrder[self.value, default: 99]
  }
}


fileprivate struct Hand: Comparable {
  let cards: [Card]
  let handType: HandType

  init(cards: [Card]) {
    self.cards = cards
    self.handType = Self.getHandType(of: cards)
  }

  // MARK: Comparable

  static func < (lhs: Hand, rhs: Hand) -> Bool {
    if lhs.handType.sortOrder() < rhs.handType.sortOrder() {
      return true
    } else if lhs.handType.sortOrder() > rhs.handType.sortOrder() {
      return false
    } else {
      // Equal!
      for (l, r) in zip(lhs.cards, rhs.cards) {
        if l == r {
          continue
        }
        return l < r
      }
    }
    return true
  }

  // MARK: Private

  private static func getHandType(of cards: [Card]) -> HandType {
    let countsMap = countsCards(cards)
    let cardCounts = countsMap.values.sorted()
    if cardCounts.count == 1 {
      return .fiveOfAKind
    } else if cardCounts.contains([1,4]) {
      return .fourOfAKind
    } else if cardCounts.contains([2,3]) {
      return .fullHouse
    } else if cardCounts.contains([1,1,3]) {
      return .threeOfAKind
    } else if cardCounts.contains([1,2,2]) {
      return .twoPair
    } else if cardCounts.contains([1,1,1,2]) {
      return .onePair
    } else {
      return .highCard
    }
  }

  private static func countsCards(_ cards: [Card]) -> [Card: Int] {
    var cardsCount: [Card: Int] = [:]
    cards.forEach { cardsCount[$0] = cardsCount[$0, default: 0] + 1 }
    return cardsCount
  }
}



fileprivate enum HandType: CaseIterable {
  case fiveOfAKind
  case fourOfAKind
  case fullHouse
  case threeOfAKind
  case twoPair
  case onePair
  case highCard


  private static let internalSortOrder: [HandType: Int] = {
    var sortOrder: [HandType: Int] = [:]
    for (i, type) in HandType.allCases.enumerated() {
      sortOrder[type] = i
    }
    return sortOrder
  }()

  func sortOrder() -> Int {
    Self.internalSortOrder[self] ?? 99
  }
}
