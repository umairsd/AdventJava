// Created on 2/2/23.

import XCTest

final class Y2022Day05Test: XCTestCase {

  func testPart1_Example() throws {
    let day = Y2022Day05(dataFileNumber: 1)
    let lines = TestUtils().loadTestData(from: day.dataFilename())
    XCTAssertEqual(day.part1(lines), "CMZ")
  }

  func testPart1_Full() throws {
    let day = Y2022Day05(dataFileNumber: 2)
    let lines = TestUtils().loadTestData(from: day.dataFilename())
    XCTAssertEqual(day.part1(lines), "QNNTGTPFN")
  }

  func testPart2_Example() throws {
    let day = Y2022Day05(dataFileNumber: 1)
    let lines = TestUtils().loadTestData(from: day.dataFilename())
    XCTAssertEqual(day.part2(lines), "MCD")
  }

  func testPart2_Full() throws {
    let day = Y2022Day05(dataFileNumber: 2)
    let lines = TestUtils().loadTestData(from: day.dataFilename())
    XCTAssertEqual(day.part2(lines), "GGNPJBTTR")
  }

}
