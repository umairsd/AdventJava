// Created on 2/16/23.

import XCTest

final class Y2022Day07Test: XCTestCase {

  func testPart1_Example() throws {
    let day = Y2022Day07(dataFileNumber: 1)
    let lines = TestUtils().loadTestData(from: day.dataFilename())
    XCTAssertEqual(day.part1(lines), "95437")
  }

  func testPart1_Full() throws {
    let day = Y2022Day07(dataFileNumber: 2)
    let lines = TestUtils().loadTestData(from: day.dataFilename())
    XCTAssertEqual(day.part1(lines), "1886043")
  }

  func DISABLED_testPart2_Example() throws {
    let day = Y2022Day07(dataFileNumber: 1)
    let lines = TestUtils().loadTestData(from: day.dataFilename())
    XCTAssertEqual(day.part2(lines), "24933642")
  }

  func DISABLED_testPart2_Full() throws {
    let day = Y2022Day07(dataFileNumber: 2)
    let lines = TestUtils().loadTestData(from: day.dataFilename())
    XCTAssertEqual(day.part2(lines), "3842121")
  }
}

