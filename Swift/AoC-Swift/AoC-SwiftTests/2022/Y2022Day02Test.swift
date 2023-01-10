// Created on 1/10/23.

import XCTest

final class Y2022Day02Test: XCTestCase {
  
  func testPart1_Example() throws {
    let day = Y2022Day02(dataFileNumber: 1)
    let lines = TestUtils().loadTestData(from: day.dataFilename())
    XCTAssertEqual(day.part1(lines), "15")
  }

  func testPart1_Full() throws {
    let day = Y2022Day02(dataFileNumber: 2)
    let lines = TestUtils().loadTestData(from: day.dataFilename())
    XCTAssertEqual(day.part1(lines), "12586")
  }

  func testPart2_Example() throws {
    let day = Y2022Day02(dataFileNumber: 1)
    let lines = TestUtils().loadTestData(from: day.dataFilename())
//    XCTAssertEqual(day.part2(lines), "")
  }

  func testPart2_Full() throws {
    let day = Y2022Day02(dataFileNumber: 2)
    let lines = TestUtils().loadTestData(from: day.dataFilename())
//    XCTAssertEqual(day.part2(lines), "")
  }
  
}
