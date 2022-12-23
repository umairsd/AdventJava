package com.umair.aoc.y2022;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day01Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runPartForDayWithFileNumber(1, new Day01(), 1, "24000");
  }

  @Test
  void testPart1_Full() {
    runPartForDayWithFileNumber(1, new Day01(), 2, "71780");
  }

  @Test
  void testPart2_Example() {
    runPartForDayWithFileNumber(2, new Day01(), 1, "45000");
  }

  @Test
  void testPart2_Full() {
    runPartForDayWithFileNumber(2, new Day01(), 2, "212489");
  }
}
