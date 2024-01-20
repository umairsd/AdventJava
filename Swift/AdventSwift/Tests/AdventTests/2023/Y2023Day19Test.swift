// Created on 12/31/2023.

import XCTest
@testable import Advent

final class Y2023Day19Test: XCTestCase {
  private typealias DayType = Y2023Day19

  func testPart1_Example() throws {
    let day = DayType()
    let lines = day.readData(from: day.exampleFilename())
    XCTAssertEqual(day.part1(lines), "19114")
  }

  func testPart1_Full() throws {
    let day = DayType()
    let lines = day.readData(from: day.fullDataFilename())
    XCTAssertEqual(day.part1(lines), "397643")
  }

  func testPart2_Example() throws {
    let day = DayType()
    let lines = day.readData(from: day.exampleFilename())
    XCTAssertEqual(day.part2(lines), "167409079868000")
  }

  func testPart2_Full() throws {
    let day = DayType()
    let lines = day.readData(from: day.fullDataFilename())
    XCTAssertEqual(day.part2(lines), "132392981697081")
  }
}
