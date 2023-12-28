// Created on 2/16/23.

import XCTest
@testable import Advent

final class Y2022Day07Test: XCTestCase {

  func testPart1_Example() throws {
    let day = Y2022Day07(dataFileNumber: 1)
    let lines = day.readData(from: day.dataFilename())
    XCTAssertEqual(day.part1(lines), "95437")
  }

  func testPart1_Full() throws {
    let day = Y2022Day07(dataFileNumber: 2)
    let lines = day.readData(from: day.dataFilename())
    XCTAssertEqual(day.part1(lines), "1886043")
  }

  func testPart2_Example() throws {
    let day = Y2022Day07(dataFileNumber: 1)
    let lines = day.readData(from: day.dataFilename())
    XCTAssertEqual(day.part2(lines), "24933642")
  }

  func testPart2_Full() throws {
    let day = Y2022Day07(dataFileNumber: 2)
    let lines = day.readData(from: day.dataFilename())
    XCTAssertEqual(day.part2(lines), "3842121")
  }
}

