// Created on 1/11/23.

import Foundation

class Y2022Day03: Day {
  var dayNumber: Int = 3
  var year: Int = 2022
  var dataFileNumber: Int

  required init(dataFileNumber: Int) {
    self.dataFileNumber = dataFileNumber
  }

  func part1(_ lines: [String]) -> String {
    let rucksacks = lines
      .filter { !$0.isEmpty }
      .map { Rucksack(contents: Array($0)) }

    let totalPriority: Int = rucksacks
      .compactMap { $0.findCommon() }
      .map { priority($0) }
      .reduce(0, +)


    return "\(totalPriority)"
  }

  func part2(_ lines: [String]) -> String {
    let elfGroups = ElfGroup.parseElfGroups(from: lines)
    let totalPriority = elfGroups
      .compactMap { $0.findCommon() }
      .map { priority($0) }
      .reduce(0, +)

    return "\(totalPriority)"
  }
}

extension Y2022Day03 {

  fileprivate func priority(_ c: Character) -> Int {
    guard c.isASCII, let asciiValue = c.asciiValue else {
      return 0
    }

    if c.isUppercase {
      let v: UInt8 = asciiValue - Character("A").asciiValue! + 27
      return Int(v)
    } else {
      let v: UInt8 = asciiValue - Character("a").asciiValue! + 1
      return Int(v)
    }
  }
}

// MARK: - Helper Types

fileprivate struct Rucksack {
  let contents: [Character]

  func firstHalf() -> ArraySlice<Character> {
    assert(contents.count % 2 == 0)
    let endingIdx = contents.count / 2
    return contents[0...endingIdx]
  }

  func secondHalf() -> ArraySlice<Character> {
    assert(contents.count % 2 == 0)
    let startingIdx = contents.count / 2
    return contents[startingIdx...]
  }

  func findCommon() -> Character? {
    let matched = firstHalf().filter { secondHalf().contains($0) }
    return matched.first
  }
}

fileprivate struct ElfGroup {
  let rucksacks: [Rucksack]

  func findCommon() -> Character? {
    assert(rucksacks.count == 3)

    let matched = rucksacks[0].contents
      .filter { rucksacks[1].contents.contains($0) }
      .filter { rucksacks[2].contents.contains($0) }

    return matched.first
  }

  static func parseElfGroups(from lines: [String]) -> [ElfGroup] {
    var elfGroups: [ElfGroup] = []
    var rucksacks: [Rucksack] = []

    let filteredLines = lines.filter({ !$0.isEmpty })

    for (i, line) in filteredLines.enumerated() {
      if i % 3 == 0 && !rucksacks.isEmpty {
        elfGroups.append(ElfGroup(rucksacks: rucksacks))
        rucksacks.removeAll()
      }

      rucksacks.append(Rucksack(contents: Array(line)))
    }

    elfGroups.append(ElfGroup(rucksacks: rucksacks))
    return elfGroups
  }
}
