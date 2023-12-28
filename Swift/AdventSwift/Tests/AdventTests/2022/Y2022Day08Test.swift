// Created on 3/27/23.

import XCTest
@testable import Advent

final class Y2022Day08Test: XCTestCase {

  func testPart1_Example() throws {
    let day = Y2022Day08(dataFileNumber: 1)
    let lines = day.readData(from: day.dataFilename())
    XCTAssertEqual(day.part1(lines), "21")
  }

  func testPart1_Full() throws {
    let day = Y2022Day08(dataFileNumber: 2)
    let lines = day.readData(from: day.dataFilename())
    XCTAssertEqual(day.part1(lines), "1792")
  }

  func testPart2_Example() throws {
    let day = Y2022Day08(dataFileNumber: 1)
    let lines = day.readData(from: day.dataFilename())
    XCTAssertEqual(day.part2(lines), "8")
  }

  func testPart2_Full() throws {
    let day = Y2022Day08(dataFileNumber: 2)
    let lines = day.readData(from: day.dataFilename())
    XCTAssertEqual(day.part2(lines), "334880")
  }
}

