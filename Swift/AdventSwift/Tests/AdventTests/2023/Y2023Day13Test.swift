// Created on 12/21/2023.

import XCTest
@testable import Advent

final class Y2023Day13Test: XCTestCase {
  private typealias DayType = Y2023Day13

  func testPart1_Example() throws {
    let day = DayType()
    let lines = day.readData(from: day.exampleFile())
    XCTAssertEqual(day.part1(lines), "405")
  }

  func testPart1_Full() throws {
    let day = DayType()
    let lines = day.readData(from: day.fullDataFilename())
    XCTAssertEqual(day.part1(lines), "26957")
  }

  func testPart2_Example() throws {
    let day = DayType()
    let lines = day.readData(from: day.exampleFile())
    XCTAssertEqual(day.part2(lines), "400")
  }

  func testPart2_Full() throws {
    let day = DayType()
    let lines = day.readData(from: day.fullDataFilename())
    XCTAssertEqual(day.part2(lines), "42695")
  }
}
