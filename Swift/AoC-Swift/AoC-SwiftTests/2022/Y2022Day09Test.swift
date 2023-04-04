// Created on 4/3/23.

import XCTest

final class Y2022Day09Test: XCTestCase {

  func testPart1_Example() throws {
    let day = Y2022Day09(dataFileNumber: 1)
    let lines = TestUtils().loadTestData(from: day.dataFilename())
    XCTAssertEqual(day.part1(lines), "13")
  }

  func testPart1_Full() throws {
    let day = Y2022Day09(dataFileNumber: 2)
    let lines = TestUtils().loadTestData(from: day.dataFilename())
    XCTAssertEqual(day.part1(lines), "6087")
  }

  func testPart2_Example() throws {
    let day = Y2022Day09(dataFileNumber: 1)
    let lines = TestUtils().loadTestData(from: day.dataFilename())
    XCTAssertEqual(day.part2(lines), "1")
  }

  func testPart2_AnotherExample() throws {
    let day = Y2022Day09(dataFileNumber: 3)
    let lines = TestUtils().loadTestData(from: day.dataFilename())
    XCTAssertEqual(day.part2(lines), "36")
  }

  func testPart2_Full() throws {
    let day = Y2022Day09(dataFileNumber: 2)
    let lines = TestUtils().loadTestData(from: day.dataFilename())
    XCTAssertEqual(day.part2(lines), "2493")
  }
}
