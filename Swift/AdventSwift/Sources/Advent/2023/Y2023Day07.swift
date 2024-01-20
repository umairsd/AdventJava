// Created on 12/7/23.

import Foundation
import RegexBuilder


/// --- Day 7: Camel Cards ---
/// https://adventofcode.com/2023/day/7
class Y2023Day07: Day {
  var dayNumber: Int = 07
  var year: Int = 2023

  required init() {}

  func part1(_ lines: [String]) -> String {
    let cardSortOrder: [Card: Int] = [
      Card(name: "A"): 0,
      Card(name: "K"): 1,
      Card(name: "Q"): 2,
      Card(name: "J"): 3,
      Card(name: "T"): 4,
      Card(name: "9"): 5,
      Card(name: "8"): 6,
      Card(name: "7"): 7,
      Card(name: "6"): 8,
      Card(name: "5"): 9,
      Card(name: "4"): 10,
      Card(name: "3"): 11,
      Card(name: "2"): 12
    ]

    let deals = lines.compactMap { parseHands($0) }

    let sortedDeals = deals.sorted { lhs, rhs in
      if lhs.hand.handType().sortOrder() < rhs.hand.handType().sortOrder() {
        return true
      } else if lhs.hand.handType().sortOrder() > rhs.hand.handType().sortOrder() {
        return false
      } else {
        // Equal!
        for (cardL, cardR) in zip(lhs.hand.cards, rhs.hand.cards) {
          if cardL == cardR {
            continue
          }
          return cardSortOrder[cardL]! < cardSortOrder[cardR]!
        }
      }
      return true
    }.reversed()

    var totalWinnings = 0
    for (rank, handInfo) in sortedDeals.enumerated() {
      totalWinnings += (rank + 1) * handInfo.bid
    }
    return "\(totalWinnings)"
  }


  func part2(_ lines: [String]) -> String {
    let cardSortOrder: [Card: Int] = [
      Card(name: "A"): 0,
      Card(name: "K"): 1,
      Card(name: "Q"): 2,
      Card(name: "T"): 4,
      Card(name: "9"): 5,
      Card(name: "8"): 6,
      Card(name: "7"): 7,
      Card(name: "6"): 8,
      Card(name: "5"): 9,
      Card(name: "4"): 10,
      Card(name: "3"): 11,
      Card(name: "2"): 12,
      Card(name: "J"): 13,
    ]

    let deals = lines.compactMap { parseHands($0) }

    let sortedDeals = deals.sorted { lhs, rhs in
      let leftSortValue = lhs.hand.handType(isJoking: true).sortOrder()
      let rightSortValue = rhs.hand.handType(isJoking: true).sortOrder()

      if leftSortValue < rightSortValue {
        return true
      } else if leftSortValue > rightSortValue {
        return false
      } else {
        // Equal!
        for (cardL, cardR) in zip(lhs.hand.cards, rhs.hand.cards) {
          if cardL == cardR {
            continue
          }
          return cardSortOrder[cardL]! < cardSortOrder[cardR]!
        }
      }
      return true
    }.reversed()

    var totalWinnings = 0
    for (rank, handInfo) in sortedDeals.enumerated() {
      totalWinnings += (rank + 1) * handInfo.bid
    }
    return "\(totalWinnings)"
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
      return charArray.map { String($0) }.map { Card(name: $0) }
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


fileprivate struct Card: Hashable {
  let name: String

  func isJoker() -> Bool {
    return name == "J"
  }
}


fileprivate struct Hand {
  let cards: [Card]

  init(cards: [Card]) {
    self.cards = cards
  }

  func handType(isJoking: Bool = false) -> HandType {
    if isJoking {
      return jokerHandType()
    } else {
      return normalHandType()
    }
  }

  // MARK: Private

  private func normalHandType() -> HandType {
    let countsMap = Self.countsCards(self.cards)
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


  private func jokerHandType() -> HandType {
    var nonJokerCards = cards
    nonJokerCards.removeAll { $0.isJoker() }
    let jokerCount = cards.count - nonJokerCards.count

    let nonJokerCounts = Self.countsCards(nonJokerCards).values.sorted()

    switch jokerCount {
    case 0:
      return normalHandType()
    case 1:
      if nonJokerCounts.contains([4]) {
        return .fiveOfAKind
      } else if nonJokerCounts.contains([1,3]) {
        return .fourOfAKind
      } else if nonJokerCounts.contains([2,2]) {
        return .fullHouse
      } else if nonJokerCounts.contains([1,1,2]) {
        return .threeOfAKind
      } else if nonJokerCounts.contains([1,1,1,1]) {
        return .onePair
      } else {
        fatalError()
      }

    case 2:
      if nonJokerCounts.contains([3]) {
        return .fiveOfAKind
      } else if nonJokerCounts.contains([1,2]) {
        return .fourOfAKind
      } else if nonJokerCounts.contains([1,1,1]) {
        return .threeOfAKind
      } else {
        fatalError()
      }

    case 3:
      if nonJokerCounts.contains([2]) {
        return .fiveOfAKind
      } else {
        return .fourOfAKind
      }

    case 4, 5:
      return .fiveOfAKind

    default:
      fatalError()
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
