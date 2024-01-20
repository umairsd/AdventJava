// Created on 11/23/22.

import Foundation

protocol Day: AnyObject {

  var dayNumber: Int { get }
  var year: Int { get }

  init()

  func solvePart1Example() -> String

  func solvePart2Example() -> String

  func solvePart1() -> String

  func solvePart2() -> String

  func part1(_ lines: [String]) -> String

  func part2(_ lines: [String]) -> String

  func fullDataFilename() -> String

  func exampleFilename(_ fileNumber: Int) -> String

  func readData(from filename: String) -> [String]

}

extension Day {

  func fullDataFilename() -> String {
    return String(format: "y%04d-day%02d-fulldata", year, dayNumber)
  }

  func exampleFilename(_ fileNumber: Int = 1) -> String {
    return String(format: "y%04d-day%02d-example%d", year, dayNumber, fileNumber)
  }

  func solvePart1Example() -> String {
    let lines = readData(from: exampleFilename(1))
    let result = part1(lines)
    return result
  }

  func solvePart2Example() -> String {
    let lines = readData(from: exampleFilename(1))
    let result = part1(lines)
    return result
  }

  func solvePart1() -> String {
    let lines = readData(from: exampleFilename(2))
    let result = part1(lines)
    return result
  }

  func solvePart2() -> String {
    let lines = readData(from: exampleFilename(2))
    let result = part2(lines)
    return result
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
