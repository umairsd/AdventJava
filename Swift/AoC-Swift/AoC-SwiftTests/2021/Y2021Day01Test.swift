// Created on 1/4/23.

import XCTest

final class Y2021Day01Test: XCTestCase {

  func testPart1_Example() throws {
    let lines = TestUtils().loadTestData(from: "y2021-day01-data1.txt")
    let day = Y2021Day01()
    XCTAssertEqual(day.part1(lines), "7")
  }

  func testPart1_Full() throws {
    let lines = TestUtils().loadTestData(from: "y2021-day01-data2.txt")
    let day = Y2021Day01()
    XCTAssertEqual(day.part1(lines), "1709")
  }

  func testPart2_Example() throws {
    let lines = TestUtils().loadTestData(from: "y2021-day01-data1.txt")
    let day = Y2021Day01()
    XCTAssertEqual(day.part2(lines), "5")
  }

  func testPart2_Full() throws {
    let lines = TestUtils().loadTestData(from: "y2021-day01-data2.txt")
    let day = Y2021Day01()
    XCTAssertEqual(day.part2(lines), "1761")
  }

}
