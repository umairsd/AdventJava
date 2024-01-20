// Created on 12/1/23.

import XCTest
@testable import Advent

final class Y2023Day01Test: XCTestCase {
  private typealias DayType = Y2023Day01

  func testPart1_Example() throws {
    let day = DayType()
    let lines = day.readData(from: day.exampleFile())
    XCTAssertEqual(day.part1(lines), "142")
  }

  func testPart1_Full() throws {
    let day = DayType()
    let lines = day.readData(from: day.fullDataFilename())
    XCTAssertEqual(day.part1(lines), "54390")
  }

  func testPart2_Example() throws {
    let day = DayType()
    let lines = day.readData(from: day.exampleFile(number: 3))
    XCTAssertEqual(day.part2(lines), "281")
  }

  func testPart2_Full() throws {
    let day = DayType()
    let lines = day.readData(from: day.fullDataFilename())
    XCTAssertEqual(day.part2(lines), "54277")
  }
}
