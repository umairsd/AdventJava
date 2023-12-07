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
      let count = waysToCoverDistance_Brute(maxDistance, within: time)
      waysToWin.append(count)
    }

    let result = waysToWin.reduce(1, *)
    return "\(result)"
  }


  func part2(_ lines: [String]) -> String {
    assert(lines.count >= 2)
    let times = parseTimes(lines[0])
    let distances = parseDistances(lines[1])

    let totalTime = Int(times.map { "\($0)" }.joined()) ?? 0
    let totalDistance = Int(distances.map { "\($0)" }.joined()) ?? 0

    let waysToWin = waysToCoverDistance(totalDistance, within: totalTime)
    return "\(waysToWin)"
  }


  // MARK: - Private


  private func waysToCoverDistance_Brute(_ maxDistance: Int, within time: Int) -> Int {
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


  /// We only need the index of the first first win, and the index of the last win.
  private func waysToCoverDistance(_ maxDistance: Int, within time: Int) -> Int {
    var begin = 0
    for timeToHold in 0...time {
      let timeRemaining = time - timeToHold
      if timeToHold * timeRemaining > maxDistance {
        begin = timeToHold
        break
      }
    }

    var end = 0
    for timeToHold in (begin...time).reversed() {
      let timeRemaining = time - timeToHold
      if timeToHold * timeRemaining > maxDistance {
        end = timeToHold
        break
      }
    }

    return end - begin + 1
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


//  private func parseBigInteger(_ line: String) -> Int {
}
