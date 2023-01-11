// Created on 1/11/23.

import XCTest

final class Y2022Day03Test: XCTestCase {

  func testPart1_Example() throws {
    let day = Y2022Day03(dataFileNumber: 1)
    let lines = TestUtils().loadTestData(from: day.dataFilename())
    XCTAssertEqual(day.part1(lines), "157")
  }

  func testPart1_Full() throws {
    let day = Y2022Day03(dataFileNumber: 2)
    let lines = TestUtils().loadTestData(from: day.dataFilename())
    XCTAssertEqual(day.part1(lines), "8298")
  }

  func testPart2_Example() throws {
    let day = Y2022Day03(dataFileNumber: 1)
    let lines = TestUtils().loadTestData(from: day.dataFilename())
    XCTAssertEqual(day.part2(lines), "70")
  }

  func testPart2_Full() throws {
    let day = Y2022Day03(dataFileNumber: 2)
    let lines = TestUtils().loadTestData(from: day.dataFilename())
    XCTAssertEqual(day.part2(lines), "2708")
  }
  
}
