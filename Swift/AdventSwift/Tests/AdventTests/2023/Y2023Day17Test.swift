// Created on 12/27/2023.

import XCTest
@testable import Advent

final class Y2023Day17Test: XCTestCase {
  private typealias DayType = Y2023Day17

  func testPart1_Example() throws {
    let day = DayType()
    let lines = day.readData(from: day.exampleFile())
    XCTAssertEqual(day.part1(lines), "102")
  }

  func testPart1_Full() throws {
    let day = DayType()
    let lines = day.readData(from: day.fullDataFilename())
    XCTAssertEqual(day.part1(lines), "665")
  }

  func testPart2_Example() throws {
    let day = DayType()
    let lines = day.readData(from: day.exampleFile())
    XCTAssertEqual(day.part2(lines), "94")
  }

  func testPart2_Example2() throws {
    let day = DayType()
    let lines = day.readData(from: day.exampleFile(number: 3))
    XCTAssertEqual(day.part2(lines), "71")
  }

  func testPart2_Example3() throws {
    let day = DayType()
    let lines = day.readData(from: day.exampleFile(number: 4))
    XCTAssertEqual(day.part2(lines), "8")
  }

  func testPart2_Full() throws {
    let day = DayType()
    let lines = day.readData(from: day.fullDataFilename())
    XCTAssertEqual(day.part2(lines), "809")
  }
}
