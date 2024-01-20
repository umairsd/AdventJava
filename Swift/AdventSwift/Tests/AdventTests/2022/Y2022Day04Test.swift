// Created on 2/1/23.

import XCTest
@testable import Advent

final class Y2022Day04Test: XCTestCase {

  func testPart1_Example() throws {
    let day = Y2022Day04()
    let lines = day.readData(from: day.exampleFile())
    XCTAssertEqual(day.part1(lines), "2")
  }

  func testPart1_Full() throws {
    let day = Y2022Day04()
    let lines = day.readData(from: day.fullDataFilename())
    XCTAssertEqual(day.part1(lines), "532")
  }

  func testPart2_Example() throws {
    let day = Y2022Day04()
    let lines = day.readData(from: day.exampleFile())
    XCTAssertEqual(day.part2(lines), "4")
  }

  func testPart2_Full() throws {
    let day = Y2022Day04()
    let lines = day.readData(from: day.fullDataFilename())
    XCTAssertEqual(day.part2(lines), "854")
  }

}
