// Created on 11/23/22.

import Foundation

protocol Day: AnyObject {

  var dayNumber: Int { get }
  var year: Int { get }

  init()

  func solvePart1()

  func solvePart2()

  func part1(_ lines: [String]) -> String

  func part2(_ lines: [String]) -> String

  func part1Filename() -> String

  func part2Filename() -> String

  func readData(from filename: String) -> [String]

}

extension Day {

  func solvePart1() {
    let filename = part1Filename()
    let lines = readData(from: filename)
    let result = part1(lines)
    print("Part 1: \(result)")
  }

  func solvePart2() {
    let lines = readData(from: part2Filename())
    let result = part2(lines)
    print("Part 2: \(result)")
  }

  func readData(from filename: String) -> [String] {
    do {
      let content = try FileUtils.readLines(fromFileNamed: filename)
      return content
    } catch {
      return []
    }
  }
}
