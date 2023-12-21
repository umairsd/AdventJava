// Created on 12/21/2023.

import XCTest

final class Y2023Day13Test: XCTestCase {
  private typealias DayType = Y2023Day13

  func testPart1_Example() throws {
    let day = DayType(dataFileNumber: 1)
    let lines = TestUtils().loadTestData(from: day.dataFilename())
    XCTAssertEqual(day.part1(lines), "405")
  }

  func testPart1_Full() throws {
    let day = DayType(dataFileNumber: 2)
    let lines = TestUtils().loadTestData(from: day.dataFilename())
    XCTAssertEqual(day.part1(lines), "26957")
  }

  func testPart2_Example() throws {
    let day = DayType(dataFileNumber: 1)
    let lines = TestUtils().loadTestData(from: day.dataFilename())
    XCTAssertEqual(day.part2(lines), "")
  }

  func testPart2_Full() throws {
    let day = DayType(dataFileNumber: 2)
    let lines = TestUtils().loadTestData(from: day.dataFilename())
    XCTAssertEqual(day.part2(lines), "")
  }
}
