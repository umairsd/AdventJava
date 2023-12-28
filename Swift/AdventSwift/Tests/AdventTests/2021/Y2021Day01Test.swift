// Created on 1/4/23.

import XCTest
@testable import Advent

final class Y2021Day01Test: XCTestCase {

  func testPart1_Example() throws {
    let day = Y2021Day01(dataFileNumber: 1)
    let lines = day.readData(from: day.dataFilename())
    XCTAssertEqual(day.part1(lines), "7")
  }

  func testPart1_Full() throws {
    let day = Y2021Day01(dataFileNumber: 2)
    let lines = day.readData(from: day.dataFilename())
    XCTAssertEqual(day.part1(lines), "1709")
  }

  func testPart2_Example() throws {
    let day = Y2021Day01(dataFileNumber: 1)
    let lines = day.readData(from: day.dataFilename())
    XCTAssertEqual(day.part2(lines), "5")
  }

  func testPart2_Full() throws {
    let day = Y2021Day01(dataFileNumber: 2)
    let lines = day.readData(from: day.dataFilename())
    XCTAssertEqual(day.part2(lines), "1761")
  }

}
