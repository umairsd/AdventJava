package com.umair.aoc.y2022;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day06Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runPartForDayWithFileNumber(1, new Day06(), 1, "7");
  }

  @Test
  void testPart1_Full() {
    runPartForDayWithFileNumber(1, new Day06(), 2, "1855");
  }

  @Test
  void testPart2_Example() {
    runPartForDayWithFileNumber(2, new Day06(), 1, "19");
  }

  @Test
  void testPart2_Full() {
    runPartForDayWithFileNumber(2, new Day06(), 2, "3256");
  }
}
