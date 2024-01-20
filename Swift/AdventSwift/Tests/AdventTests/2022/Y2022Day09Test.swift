// Created on 4/3/23.

import XCTest
@testable import Advent

final class Y2022Day09Test: XCTestCase {

  func testPart1_Example() throws {
    let day = Y2022Day09()
    let lines = day.readData(from: day.exampleFilename())
    XCTAssertEqual(day.part1(lines), "13")
  }

  func testPart1_Full() throws {
    let day = Y2022Day09()
    let lines = day.readData(from: day.fullDataFilename())
    XCTAssertEqual(day.part1(lines), "6087")
  }

  func testPart2_Example() throws {
    let day = Y2022Day09()
    let lines = day.readData(from: day.exampleFilename())
    XCTAssertEqual(day.part2(lines), "1")
  }

  func testPart2_AnotherExample() throws {
    let day = Y2022Day09()
    let lines = day.readData(from: day.exampleFilename(3))
    XCTAssertEqual(day.part2(lines), "36")
  }

  func testPart2_Full() throws {
    let day = Y2022Day09()
    let lines = day.readData(from: day.fullDataFilename())
    XCTAssertEqual(day.part2(lines), "2493")
  }
}
