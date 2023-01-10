// Created on 1/10/23.

import Foundation

class Y2022Day02: Day {
  var dayNumber: Int = 2
  var year: Int = 2022
  var dataFileNumber: Int

  required init(dataFileNumber: Int) {
    self.dataFileNumber = dataFileNumber
  }

  func part1(_ lines: [String]) -> String {
    ""
  }

  func part2(_ lines: [String]) -> String {
    ""
  }
}


fileprivate struct SpecificMoveStrategy {
  let elfMove: GameMove
  let humanMove: GameMove
}

fileprivate enum GameMove {
  case rock, paper, scissors

  init?(_ string: String) {
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

  func score() -> Int {
    switch self {
    case .win: return 6
    case .draw: return 3
    case .lose: return 0
    }
  }
}
