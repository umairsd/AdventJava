// Created on 4/27/23.

import XCTest
@testable import Advent

final class Y2022Day10Test: XCTestCase {

  func testPart1_Example() throws {
    let day = Y2022Day10()
    let lines = day.readData(from: day.exampleFile())
    XCTAssertEqual(day.part1(lines), "13140")
  }

  func testPart1_Full() throws {
    let day = Y2022Day10()
    let lines = day.readData(from: day.fullDataFilename())
    XCTAssertEqual(day.part1(lines), "16480")
  }

  func testPart2_Example() throws {
    let day = Y2022Day10()
    let lines = day.readData(from: day.exampleFile())

    let expected = """
    ##..##..##..##..##..##..##..##..##..##..
    ###...###...###...###...###...###...###.
    ####....####....####....####....####....
    #####.....#####.....#####.....#####.....
    ######......######......######......####
    #######.......#######.......#######.....

    """

    XCTAssertEqual(day.part2(lines), expected)
  }

  func testPart2_Full() throws {
    let day = Y2022Day10()
    let lines = day.readData(from: day.fullDataFilename())
    let expected = """
    ###..#....####.####.#..#.#....###..###..
    #..#.#....#....#....#..#.#....#..#.#..#.
    #..#.#....###..###..#..#.#....#..#.###..
    ###..#....#....#....#..#.#....###..#..#.
    #....#....#....#....#..#.#....#....#..#.
    #....####.####.#.....##..####.#....###..

    """
    XCTAssertEqual(day.part2(lines), expected)
  }
}
