package com.umair.aoc.y2022;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day12Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runPartForDayWithFileNumber(1, new Day12(), 1, "31");
  }

  @Test
  void testPart1_Full() {
    runPartForDayWithFileNumber(1, new Day12(), 2, "472");
  }

  @Test
  void testPart2_Example() {
    runPartForDayWithFileNumber(2, new Day12(), 1, "29");
  }

  @Test
  void testPart2_Full() {
    runPartForDayWithFileNumber(2, new Day12(), 2, "465");
  }
}
