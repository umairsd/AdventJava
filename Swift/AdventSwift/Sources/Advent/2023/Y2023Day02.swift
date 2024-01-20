// Created on 12/3/23.

import Foundation
import RegexBuilder

/// --- Day 2: Cube Conundrum ---
/// https://adventofcode.com/2023/day/2
class Y2023Day02: Day {

  private static let redCubesInBag = 12
  private static let greenCubesInBag = 13
  private static let blueCubesInBag = 14


  var dayNumber: Int = 2
  var year: Int = 2023

  required init() {}

  func part1(_ lines: [String]) -> String {
    let games = lines.compactMap { parseGame($0) }
    // Include only games where all gameResults are valid.
    let possibleGames = games.filter { game in
      game.results.allSatisfy { gameResult in
        gameResult.redCount <= Self.redCubesInBag &&
        gameResult.greenCount <= Self.greenCubesInBag &&
        gameResult.blueCount <= Self.blueCubesInBag
      }
    }
    
    let result = possibleGames.map { $0.gameId }.reduce(0, +)
    return "\(result)"
  }

  func part2(_ lines: [String]) -> String {
    let games = lines.compactMap { parseGame($0) }
    let result = games.map { powerOf($0) }.reduce(0, +)
    return "\(result)"
  }


  /// Computes the power of each game.
  private func powerOf(_ game: Game) -> Int {
    var maxRed = 0
    var maxGreen = 0
    var maxBlue = 0
    // Get the fewest number of cubes of each color to make the game possible.
    // This is the maximum count of each color that we see within the game.
    for gr in game.results {
      maxRed = max(maxRed, gr.redCount)
      maxGreen = max(maxGreen, gr.greenCount)
      maxBlue = max(maxBlue, gr.blueCount)
    }

    // Compute power
    return maxRed * maxGreen * maxBlue
  }
}


// MARK: - Parsing

extension Y2023Day02 {

  private static let resultsRef = Reference(String.self)
  private static let gameIdRef = Reference(Int.self)

  private static let lineRegex = Regex {
    "Game "
    TryCapture(as: gameIdRef) {
      OneOrMore(.digit)
    } transform: { match in
      Int(match)
    }
    ": "
    TryCapture(as: resultsRef) {
      OneOrMore(.any)
    } transform: { match in
      String(match)
    }
  }


  /// Parses a line of the form:
  /// "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green".
  func parseGame(_ line: String) -> Game? {
    guard let match = line.firstMatch(of: Self.lineRegex) else {
      return nil
    }
    let gameId = match[Self.gameIdRef]
    let resultsData = match[Self.resultsRef]
    let results = parseGameResults(resultsData)
    let game = Game(gameId: gameId, results: results)
    return game
  }


  /// Parses a string of the form, "3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green".
  func parseGameResults(_ line: String) -> [GameResult] {
    let gameResultStrings = line.trimmingCharacters(in: .whitespaces).split(separator: ";")
    let gameResults = gameResultStrings.map { parseGameResult(String($0)) }
    return gameResults
  }


  /// Parses a string of the form "1 red, 2 green, 6 blue".
  func parseGameResult(_ string: String) -> GameResult {
    let cubeResults = string.trimmingCharacters(in: .whitespacesAndNewlines).split(separator: ",")
    var redCount = 0
    var greenCount = 0
    var blueCount = 0

    for cube in cubeResults {
      let tokens = cube.trimmingCharacters(in: .whitespaces).split(separator: " ")
      guard tokens.count == 2,
            let count = Int(tokens[0].trimmingCharacters(in: .whitespaces)),
            let cubeType = Cube(rawValue: tokens[1].trimmingCharacters(in: .whitespaces))
      else {
        continue
      }

      switch cubeType {
      case .red:
        redCount = count
      case .green:
        greenCount = count
      case .blue:
        blueCount = count
      }
    }

    let gameResult = GameResult(redCount: redCount, greenCount: greenCount, blueCount: blueCount)
    return gameResult
  }
}


struct Game {
  let gameId: Int
  let results: [GameResult]
}


struct GameResult {
  let redCount: Int
  let greenCount: Int
  let blueCount: Int
}


enum Cube: String {
  case red
  case blue
  case green
}
