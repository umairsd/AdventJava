package com.umair.aoc.y2022;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day10Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runPartForDayWithFileNumber(1, new Day10(), 2, "13140");
  }

  @Test
  void testPart1_Full() {
    runPartForDayWithFileNumber(1, new Day10(), 3, "16480");
  }

  @Test
  void testPart2_Example() {
    String expected =
        "\n" +
        "##..##..##..##..##..##..##..##..##..##..\n" +
        "###...###...###...###...###...###...###.\n" +
        "####....####....####....####....####....\n" +
        "#####.....#####.....#####.....#####.....\n" +
        "######......######......######......####\n" +
        "#######.......#######.......#######.... \n";
    runPartForDayWithFileNumber(2, new Day10(), 2, expected);
  }

  @Test
  void testPart2_Full() {
    String expected =
        "\n" +
        "###..#....####.####.#..#.#....###..###..\n" +
        "#..#.#....#....#....#..#.#....#..#.#..#.\n" +
        "#..#.#....###..###..#..#.#....#..#.###..\n" +
        "###..#....#....#....#..#.#....###..#..#.\n" +
        "#....#....#....#....#..#.#....#....#..#.\n" +
        "#....####.####.#.....##..####.#....###. \n";
    runPartForDayWithFileNumber(2, new Day10(), 3, expected);
  }
}
