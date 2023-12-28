// Created on 1/5/23.

import Foundation

class Y2022Day01: Day {
  var dayNumber: Int = 1
  var year: Int = 2022
  var dataFileNumber: Int

  required init(dataFileNumber: Int) {
    self.dataFileNumber = dataFileNumber
  }

  /** Find the elf carrying the most calories. */
  func part1(_ lines: [String]) -> String {
    var maxCalories = Int.min
    var currentTotal = 0

    for line in lines {
      if line.isEmpty {
        maxCalories = max(maxCalories, currentTotal)
        currentTotal = 0
        continue
      }
      let calories = Int(line) ?? 0
      currentTotal += calories
    }

    maxCalories = max(maxCalories, currentTotal)
    return "\(maxCalories)"
  }

  /** Find the top 3 elves carrying the most calories. */
  func part2(_ lines: [String]) -> String {
    let perElfCalories = parseToPerElfCalories(lines)
    let sortedCalories = perElfCalories.sorted { $0 > $1 }
    let top3 = sortedCalories[0...2].reduce(0, +)
    return String(top3)
  }

  private func parseToPerElfCalories(_ lines: [String]) -> [Int] {
    var perElfCalories: [Int] = []

    var currentTotal = 0
    for line in lines {
      if line.isEmpty {
        perElfCalories.append(currentTotal)
        currentTotal = 0
        continue
      }
      currentTotal += Int(line) ?? 0
    }

    if currentTotal != 0 {
      perElfCalories.append(currentTotal)
    }

    return perElfCalories
  }
}
