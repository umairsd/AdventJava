// Created on 12/05/2023.

import XCTest
@testable import Advent

final class Y2023Day05Test: XCTestCase {
  private typealias DayType = Y2023Day05

  func testPart1_Example() throws {
    let day = DayType()
    let lines = day.readData(from: day.exampleFilename())
    XCTAssertEqual(day.part1(lines), "35")
  }

  func testPart1_Full() throws {
    let day = DayType()
    let lines = day.readData(from: day.fullDataFilename())
    XCTAssertEqual(day.part1(lines), "650599855")
  }

  func testPart2_Example() throws {
    let day = DayType()
    let lines = day.readData(from: day.exampleFilename())
    XCTAssertEqual(day.part2(lines), "46")
  }

  func testPart2_Full() throws {
    let day = DayType()
    let lines = day.readData(from: day.fullDataFilename())
    XCTAssertEqual(day.part2(lines), "1240035")
  }
}
