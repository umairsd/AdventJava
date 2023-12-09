// Created on 12/09/2023.

import XCTest

final class Y2023Day08Test: XCTestCase {
  private typealias DayType = Y2023Day08

  func testPart1_Example() throws {
    let day = DayType(dataFileNumber: 1)
    let lines = TestUtils().loadTestData(from: day.dataFilename())
    XCTAssertEqual(day.part1(lines), "2")
  }

  func testPart1_Example2() throws {
    let day = DayType(dataFileNumber: 3)
    let lines = TestUtils().loadTestData(from: day.dataFilename())
    XCTAssertEqual(day.part1(lines), "6")
  }

  func testPart1_Full() throws {
    let day = DayType(dataFileNumber: 2)
    let lines = TestUtils().loadTestData(from: day.dataFilename())
    XCTAssertEqual(day.part1(lines), "20659")
  }

  func testPart2_Example() throws {
    let day = DayType(dataFileNumber: 4)
    let lines = TestUtils().loadTestData(from: day.dataFilename())
    XCTAssertEqual(day.part2(lines), "6")
  }

  func DISABLEDtestPart2_Full() throws {
    let day = DayType(dataFileNumber: 2)
    let lines = TestUtils().loadTestData(from: day.dataFilename())
    XCTAssertEqual(day.part2(lines), "")
  }
}
