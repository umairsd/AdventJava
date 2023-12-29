// Created on 12/27/2023.

import XCTest
@testable import Advent

final class Y2023Day17Test: XCTestCase {
  private typealias DayType = Y2023Day17

  func testPart1_Example() throws {
    let day = DayType(dataFileNumber: 1)
    let lines = day.readData(from: day.dataFilename())
    XCTAssertEqual(day.part1(lines), "102")
  }

  func testPart1_Full() throws {
    let day = DayType(dataFileNumber: 2)
    let lines = day.readData(from: day.dataFilename())
    XCTAssertEqual(day.part1(lines), "665")
  }

  func testPart2_Example() throws {
    let day = DayType(dataFileNumber: 1)
    let lines = day.readData(from: day.dataFilename())
    XCTAssertEqual(day.part2(lines), "94")
  }

  func testPart2_Example2() throws {
    let day = DayType(dataFileNumber: 3)
    let lines = day.readData(from: day.dataFilename())
    XCTAssertEqual(day.part2(lines), "71")
  }

  func testPart2_Example3() throws {
    let day = DayType(dataFileNumber: 4)
    let lines = day.readData(from: day.dataFilename())
    XCTAssertEqual(day.part2(lines), "8")
  }

  func testPart2_Full() throws {
    let day = DayType(dataFileNumber: 2)
    let lines = day.readData(from: day.dataFilename())
    XCTAssertEqual(day.part2(lines), "809")
  }
}
