// Created on 2/16/23.

import XCTest

final class Y2022Day06Test: XCTestCase {

  func testPart1_Example() throws {
    let day = Y2022Day06(dataFileNumber: 1)
    let lines = TestUtils().loadTestData(from: day.dataFilename())
    XCTAssertEqual(day.part1(lines), "7")
  }

  func testPart1_Full() throws {
    let day = Y2022Day06(dataFileNumber: 2)
    let lines = TestUtils().loadTestData(from: day.dataFilename())
    XCTAssertEqual(day.part1(lines), "1855")
  }

  func testPart2_Example() throws {
    let day = Y2022Day06(dataFileNumber: 1)
    let lines = TestUtils().loadTestData(from: day.dataFilename())
    XCTAssertEqual(day.part2(lines), "19")
  }

  func testPart2_Full() throws {
    let day = Y2022Day06(dataFileNumber: 2)
    let lines = TestUtils().loadTestData(from: day.dataFilename())
    XCTAssertEqual(day.part2(lines), "3256")
  }
}
