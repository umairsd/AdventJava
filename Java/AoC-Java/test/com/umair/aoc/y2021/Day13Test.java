package com.umair.aoc.y2021;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day13Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runPartForDayWithFileNumber(1, new Day13(), 1, "17");
  }

  @Test
  void testPart1_Full() {
    runPartForDayWithFileNumber(1, new Day13(), 2, "942");
  }

  @Test
  void testPart2_Example() {
    String expected =
        "\n" +
        "#####\n" +
        "#...#\n" +
        "#...#\n" +
        "#...#\n" +
        "#####\n" +
        ".....\n" +
        ".....\n";
    runPartForDayWithFileNumber(2, new Day13(), 1, expected);
  }

  @Test
  void testPart2_Full() {
    String expected =
        "\n" +
        "..##.####..##..#..#..##..###..###..###..\n" +
        "...#....#.#..#.#..#.#..#.#..#.#..#.#..#.\n" +
        "...#...#..#....#..#.#..#.#..#.#..#.###..\n" +
        "...#..#...#.##.#..#.####.###..###..#..#.\n" +
        "#..#.#....#..#.#..#.#..#.#....#.#..#..#.\n" +
        ".##..####..###..##..#..#.#....#..#.###..\n";
    runPartForDayWithFileNumber(2, new Day13(), 2, expected);
  }
}
