// Created on 12/09/2023.

import XCTest
@testable import Advent

final class Y2023Day08Test: XCTestCase {
  private typealias DayType = Y2023Day08

  func testPart1_Example() throws {
    let day = DayType()
    let lines = day.readData(from: day.exampleFilename())
    XCTAssertEqual(day.part1(lines), "2")
  }

  func testPart1_Example2() throws {
    let day = DayType()
    let lines = day.readData(from: day.exampleFilename(3))
    XCTAssertEqual(day.part1(lines), "6")
  }

  func testPart1_Full() throws {
    let day = DayType()
    let lines = day.readData(from: day.fullDataFilename())
    XCTAssertEqual(day.part1(lines), "20659")
  }

  func testPart2_Example() throws {
    let day = DayType()
    let lines = day.readData(from: day.exampleFilename(4))
    XCTAssertEqual(day.part2(lines), "6")
  }

  func testPart2_Full() throws {
    let day = DayType()
    let lines = day.readData(from: day.fullDataFilename())
    XCTAssertEqual(day.part2(lines), "15690466351717")
  }
}
