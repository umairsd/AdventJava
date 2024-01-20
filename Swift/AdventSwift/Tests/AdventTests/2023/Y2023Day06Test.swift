// Created on 12/06/2023.

import XCTest
@testable import Advent

final class Y2023Day06Test: XCTestCase {
  private typealias DayType = Y2023Day06

  func testPart1_Example() throws {
    let day = DayType()
    let lines = day.readData(from: day.exampleFile())
    XCTAssertEqual(day.part1(lines), "288")
  }

  func testPart1_Full() throws {
    let day = DayType()
    let lines = day.readData(from: day.fullDataFilename())
    XCTAssertEqual(day.part1(lines), "1159152")
  }

  func testPart2_Example() throws {
    let day = DayType()
    let lines = day.readData(from: day.exampleFile())
    XCTAssertEqual(day.part2(lines), "71503")
  }

  func testPart2_Full() throws {
    let day = DayType()
    let lines = day.readData(from: day.fullDataFilename())
    XCTAssertEqual(day.part2(lines), "41513103")
  }
}
