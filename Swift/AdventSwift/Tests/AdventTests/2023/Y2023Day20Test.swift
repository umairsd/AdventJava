// Created on 1/02/2024.

import XCTest
@testable import Advent

final class Y2023Day20Test: XCTestCase {
  private typealias DayType = Y2023Day20

  func testPart1_Example() throws {
    let day = DayType(dataFileNumber: 1)
    let lines = day.readData(from: day.dataFilename())
    XCTAssertEqual(day.part1(lines), "32000000")
  }

  func testPart1_Example2() throws {
    let day = DayType(dataFileNumber: 3)
    let lines = day.readData(from: day.dataFilename())
    XCTAssertEqual(day.part1(lines), "11687500")
  }

  func testPart1_Full() throws {
    let day = DayType(dataFileNumber: 2)
    let lines = day.readData(from: day.dataFilename())
    XCTAssertEqual(day.part1(lines), "788848550")
  }

  
  func testPart2_Full() throws {
    let day = DayType(dataFileNumber: 2)
    let lines = day.readData(from: day.dataFilename())
    XCTAssertEqual(day.part2(lines), "228300182686739")
  }
}
