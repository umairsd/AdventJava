// Created on 12/05/2023.

import XCTest

final class Y2023Day05Test: XCTestCase {
  private typealias DayType = Y2023Day05

  func testPart1_Example() throws {
    let day = DayType(dataFileNumber: 1)
    let lines = TestUtils().loadTestData(from: day.dataFilename())
    XCTAssertEqual(day.part1(lines), "35")
  }

  func testPart1_Full() throws {
    let day = DayType(dataFileNumber: 2)
    let lines = TestUtils().loadTestData(from: day.dataFilename())
    XCTAssertEqual(day.part1(lines), "650599855")
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
