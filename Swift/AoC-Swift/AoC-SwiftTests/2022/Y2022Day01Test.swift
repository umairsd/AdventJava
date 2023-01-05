// Created on 1/5/23.

import XCTest

final class Y2022Day01Test: XCTestCase {

  func testPart1_Example() throws {
    let day = Y2022Day01(dataFileNumber: 1)
    let lines = TestUtils().loadTestData(from: day.dataFilename())
    XCTAssertEqual(day.part1(lines), "24000")
  }

  func testPart1_Full() throws {
    let day = Y2022Day01(dataFileNumber: 2)
    let lines = TestUtils().loadTestData(from: day.dataFilename())
    XCTAssertEqual(day.part1(lines), "71780")
  }

  func testPart2_Example() throws {
    let day = Y2022Day01(dataFileNumber: 1)
    let lines = TestUtils().loadTestData(from: day.dataFilename())
    XCTAssertEqual(day.part2(lines), "45000")
  }

  func testPart2_Full() throws {
    let day = Y2022Day01(dataFileNumber: 2)
    let lines = TestUtils().loadTestData(from: day.dataFilename())
    XCTAssertEqual(day.part2(lines), "212489")
  }
}
