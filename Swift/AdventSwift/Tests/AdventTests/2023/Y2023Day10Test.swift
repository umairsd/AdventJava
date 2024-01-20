// Created on 12/10/2023.

import XCTest
@testable import Advent

final class Y2023Day10Test: XCTestCase {
  private typealias DayType = Y2023Day10

  func testPart1_Example() throws {
    let day = DayType()
    let lines = day.readData(from: day.exampleFilename())
    XCTAssertEqual(day.part1(lines), "")
  }

  func testPart1_Full() throws {
    let day = DayType()
    let lines = day.readData(from: day.fullDataFilename())
    XCTAssertEqual(day.part1(lines), "")
  }

  func testPart2_Example() throws {
    let day = DayType()
    let lines = day.readData(from: day.exampleFilename())
    XCTAssertEqual(day.part2(lines), "")
  }

  func testPart2_Full() throws {
    let day = DayType()
    let lines = day.readData(from: day.fullDataFilename())
    XCTAssertEqual(day.part2(lines), "")
  }
}
