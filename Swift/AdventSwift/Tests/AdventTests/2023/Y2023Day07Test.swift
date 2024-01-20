// Created on 12/07/2023.

import XCTest
@testable import Advent

final class Y2023Day07Test: XCTestCase {
  private typealias DayType = Y2023Day07

  func testPart1_Example() throws {
    let day = DayType()
    let lines = day.readData(from: day.exampleFile())
    XCTAssertEqual(day.part1(lines), "6440")
  }

  func testPart1_Full() throws {
    let day = DayType()
    let lines = day.readData(from: day.fullDataFilename())
    XCTAssertEqual(day.part1(lines), "246424613")
  }

  func testPart2_Example() throws {
    let day = DayType()
    let lines = day.readData(from: day.exampleFile())
    XCTAssertEqual(day.part2(lines), "5905")
  }

  func testPart2_Full() throws {
    let day = DayType()
    let lines = day.readData(from: day.fullDataFilename())
    XCTAssertEqual(day.part2(lines), "248256639")
  }
}
