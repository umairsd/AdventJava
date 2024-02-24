package com.umair.aoc.y2022;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day10Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runExampleForDay(new Day10(), 1, 3, "13140");
  }

  @Test
  void testPart1_Full() {
    runForDay(new Day10(), 1, "16480");
  }

  @Test
  void testPart2_Example() {
    String expected =
        """

            ##..##..##..##..##..##..##..##..##..##..
            ###...###...###...###...###...###...###.
            ####....####....####....####....####....
            #####.....#####.....#####.....#####.....
            ######......######......######......####
            #######.......#######.......#######....\s
            """;
    runExampleForDay(new Day10(), 2, 3, expected);
  }

  @Test
  void testPart2_Full() {
    String expected =
        """

            ###..#....####.####.#..#.#....###..###..
            #..#.#....#....#....#..#.#....#..#.#..#.
            #..#.#....###..###..#..#.#....#..#.###..
            ###..#....#....#....#..#.#....###..#..#.
            #....#....#....#....#..#.#....#....#..#.
            #....####.####.#.....##..####.#....###.\s
            """;
    runForDay(new Day10(), 2, expected);
  }
}
