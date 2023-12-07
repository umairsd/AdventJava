// Created on 12/6/23.

import Foundation
import RegexBuilder

/// --- Day 6: Wait For It ---
/// https://adventofcode.com/2023/day/6
class Y2023Day06: Day {
  var dayNumber: Int = 06
  var year: Int = 2023
  var dataFileNumber: Int

  required init(dataFileNumber: Int) {
    self.dataFileNumber = dataFileNumber
  }

  func part1(_ lines: [String]) -> String {
    assert(lines.count >= 2)
    let times = parseTimes(lines[0])
    let distances = parseDistances(lines[1])

    var waysToWin: [Int] = []
    for (time, maxDistance) in zip(times, distances) {
      let count = waysToCoverDistance(maxDistance, within: time)
      waysToWin.append(count)
    }

    let result = waysToWin.reduce(1, *)
    return "\(result)"
  }


  func part2(_ lines: [String]) -> String {
    ""
  }


  // MARK: - Private


  private func waysToCoverDistance(_ maxDistance: Int, within time: Int) -> Int {
    var distance = 0
    var winCount = 0
    for timeToHold in 0...time {
      let timeRemaining = time - timeToHold
      distance = timeToHold * timeRemaining
      if distance > maxDistance {
        winCount += 1
      }
    }

    return winCount
  }

}


// MARK: - Parsing

extension Y2023Day06 {

  func parseTimes(_ line: String) -> [Int] {
    assert(line.starts(with: "Time: "))
    return parseIntegers(line)
  }

  func parseDistances(_ line: String) -> [Int] {
    assert(line.starts(with: "Distance:"))
    return parseIntegers(line)
  }


  private func parseIntegers(_ line: String) -> [Int] {
    let timeValues = line.trimmingCharacters(in: .whitespaces)
      .split(separator: " ")
      .dropFirst()
      .map { $0.trimmingCharacters(in: .whitespaces) }
      .compactMap { Int($0) }
    return timeValues
  }
}
