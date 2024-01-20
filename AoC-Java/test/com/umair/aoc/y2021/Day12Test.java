package com.umair.aoc.y2021;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day12Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runPartForDayWithFileNumber(1, new Day12(), 1, "10");
    runPartForDayWithFileNumber(1, new Day12(), 2, "19");
    runPartForDayWithFileNumber(1, new Day12(), 3, "226");
  }

  @Test
  void testPart1_Full() {
    runPartForDayWithFileNumber(1, new Day12(), 4, "5228");
  }

  @Test
  void testPart2_Example() {
    runPartForDayWithFileNumber(2, new Day12(), 1, "36");
    runPartForDayWithFileNumber(2, new Day12(), 2, "103");
    runPartForDayWithFileNumber(2, new Day12(), 3, "3509");
  }

  @Test
  void testPart2_Full() {
    runPartForDayWithFileNumber(2, new Day12(), 4, "131228");
  }
}
