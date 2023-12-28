// Created on 11/23/22.

import Foundation

protocol Day: AnyObject {

  var dayNumber: Int { get }
  var year: Int { get }
  var dataFileNumber: Int { get }

  init(dataFileNumber: Int)

  func solvePart1() -> String

  func solvePart2() -> String

  func part1(_ lines: [String]) -> String

  func part2(_ lines: [String]) -> String

  func dataFilename() -> String

  func readData(from filename: String) -> [String]

}

extension Day {

  init(dataFileNumber: Int = 2) {
    self.init(dataFileNumber: 2)
  }

  func dataFilename() -> String {
    return String(format: "y%04d-day%02d-data%d", year, dayNumber, dataFileNumber)
  }

  func solvePart1() -> String {
    let lines = readData(from: dataFilename())
    let result = part1(lines)
    return result
  }

  func solvePart2() -> String {
    let lines = readData(from: dataFilename())
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
