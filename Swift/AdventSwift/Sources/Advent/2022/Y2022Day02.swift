// Created on 1/10/23.

import Foundation
import RegexBuilder

class Y2022Day02: Day {
  var dayNumber: Int = 2
  var year: Int = 2022

  required init() {}

  func part1(_ lines: [String]) -> String {
    let gameRounds = lines.compactMap(parseSpecificMoveStrategy(from:))
    let score = gameRounds
      .map { $0.scoreForHuman() }
      .reduce(0, +)
    return "\(score)"
  }

  func part2(_ lines: [String]) -> String {
    let gameRounds = lines.compactMap(parseDesiredOutcomeStrategy(from:))
    let score = gameRounds
      .map { $0.score() }
      .reduce(0, +)
    return "\(score)"
  }
}

// MARK: - Parse

extension Y2022Day02 {
  private static let searchRegex = Regex {
    Capture {
      One(.word)
    }
    " "
    Capture {
      One(.word)
    }
  }

  fileprivate func parseSpecificMoveStrategy(from line: String) -> SpecificMoveStrategy? {
    guard let result = line.firstMatch(of: Self.searchRegex) else {
      return nil
    }
    guard let elfMove = GameMove(result.1), let humanMove = GameMove(result.2) else {
      return nil
    }
    return SpecificMoveStrategy(elfMove: elfMove, humanMove: humanMove)
  }

  fileprivate func parseDesiredOutcomeStrategy(from line: String) -> DesiredOutcomeStrategy? {
    guard let result = line.firstMatch(of: Self.searchRegex) else {
      return nil
    }
    guard let elfMove = GameMove(result.1), let outcome = GameOutcome(result.2) else {
      return nil
    }
    return DesiredOutcomeStrategy(elfMove: elfMove, desiredOutcome: outcome)
  }
}

// MARK: - Specific Move Strategy (p1)

/// Represents the strategy of playing the exact move specified in the guide.
fileprivate struct SpecificMoveStrategy {
  let elfMove: GameMove
  let humanMove: GameMove

  func scoreForHuman() -> Int {
    let outcome: GameOutcome
    if humanMove == elfMove {
      outcome = .draw
    } else if elfMove.winningCountermove() == humanMove {
      outcome = .win
    } else {
      outcome = .lose
    }

    return outcome.score() + humanMove.score()
  }
}

// MARK: - Desired Outcome Strategy (p2)

/// Represents the strategy of getting to the desired outcome as specified in the guide.
fileprivate struct DesiredOutcomeStrategy {
  let elfMove: GameMove
  let desiredOutcome: GameOutcome

  func score() -> Int {
    switch desiredOutcome {
      case .win:
        return desiredOutcome.score() + elfMove.winningCountermove().score()
      case .draw:
        return desiredOutcome.score() + elfMove.score()
      case .lose:
        return desiredOutcome.score() + elfMove.losingCountermove().score()
    }
  }
}

// MARK: - Helper Types

fileprivate enum GameMove {
  case rock, paper, scissors

  init?(_ string: Substring) {
    switch string {
      case "A", "X": self = .rock
      case "B", "Y": self = .paper
      case "C", "Z": self = .scissors
      default: return nil
    }
  }

  /// The move that would win against the current move.
  func winningCountermove() -> GameMove {
    switch self {
      case .rock: return .paper
      case .paper: return .scissors
      case .scissors: return .rock
    }
  }

  /// The move that would lose against the current move.
  func losingCountermove() -> GameMove {
    switch self {
      case .rock: return .scissors
      case .paper: return .rock
      case .scissors: return .paper
    }
  }

  func score() -> Int {
    switch self {
      case .rock: return 1
      case .paper: return 2
      case .scissors: return 3
    }
  }
}

fileprivate enum GameOutcome {
  case win, draw, lose

  init?(_ string: Substring) {
    switch string {
      case "X": self = .lose
      case "Y": self = .draw
      case "Z": self = .win
      default: return nil
    }
  }

  func score() -> Int {
    switch self {
      case .win: return 6
      case .draw: return 3
      case .lose: return 0
    }
  }
}
