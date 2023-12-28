// Created on 12/1/23.

import XCTest
@testable import Advent

final class Y2023Day01Test: XCTestCase {

  func testPart1_Example() throws {
    let day = Y2023Day01(dataFileNumber: 1)
    let lines = day.readData(from: day.dataFilename())
    XCTAssertEqual(day.part1(lines), "142")
  }

  func testPart1_Full() throws {
    let day = Y2023Day01(dataFileNumber: 2)
    let lines = day.readData(from: day.dataFilename())
    XCTAssertEqual(day.part1(lines), "54390")
  }

  func testPart2_Example() throws {
    let day = Y2023Day01(dataFileNumber: 3)
    let lines = day.readData(from: day.dataFilename())
    XCTAssertEqual(day.part2(lines), "281")
  }

  func testPart2_Full() throws {
    let day = Y2023Day01(dataFileNumber: 2)
    let lines = day.readData(from: day.dataFilename())
    XCTAssertEqual(day.part2(lines), "54277")
  }
}
