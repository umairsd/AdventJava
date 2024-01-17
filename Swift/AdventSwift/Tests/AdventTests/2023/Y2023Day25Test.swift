// Created on 1/12/2024.

import XCTest
@testable import Advent

final class Y2023Day25Test: XCTestCase {
  private typealias DayType = Y2023Day25

  func testPart1_ExampleA() throws {
    let day = DayType(dataFileNumber: 3)
    let lines = day.readData(from: day.dataFilename())
    XCTAssertEqual(day.part1(lines), "")
  }


  func testPart1_Example() throws {
    let day = DayType(dataFileNumber: 1)
    let lines = day.readData(from: day.dataFilename())
    XCTAssertEqual(day.part1(lines), "")
  }

  func testPart1_Full() throws {
    let day = DayType(dataFileNumber: 2)
    let lines = day.readData(from: day.dataFilename())
    XCTAssertEqual(day.part1(lines), "")
  }

  func testPart2_Example() throws {
    let day = DayType(dataFileNumber: 1)
    let lines = day.readData(from: day.dataFilename())
    XCTAssertEqual(day.part2(lines), "")
  }

  func testPart2_Full() throws {
    let day = DayType(dataFileNumber: 2)
    let lines = day.readData(from: day.dataFilename())
    XCTAssertEqual(day.part2(lines), "")
  }
}
