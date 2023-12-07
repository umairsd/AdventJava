// Created on 12/06/2023.

import XCTest

final class Y2023Day06Test: XCTestCase {
  private typealias DayType = Y2023Day06

  func testPart1_Example() throws {
    let day = DayType(dataFileNumber: 1)
    let lines = TestUtils().loadTestData(from: day.dataFilename())
    XCTAssertEqual(day.part1(lines), "288")
  }

  func testPart1_Full() throws {
    let day = DayType(dataFileNumber: 2)
    let lines = TestUtils().loadTestData(from: day.dataFilename())
    XCTAssertEqual(day.part1(lines), "1159152")
  }

  func testPart2_Example() throws {
    let day = DayType(dataFileNumber: 1)
    let lines = TestUtils().loadTestData(from: day.dataFilename())
    XCTAssertEqual(day.part2(lines), "71503")
  }

  func testPart2_Full() throws {
    let day = DayType(dataFileNumber: 2)
    let lines = TestUtils().loadTestData(from: day.dataFilename())
    XCTAssertEqual(day.part2(lines), "41513103")
  }
}
