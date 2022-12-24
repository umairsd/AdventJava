package com.umair.aoc.y2022;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day18Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runPartForDayWithFileNumber(1, new Day18(), 1, "64");
  }

  @Test
  void testPart1_Full() {
    runPartForDayWithFileNumber(1, new Day18(), 2, "3550");
  }

  @Test
  void testPart2_Example() {
    runPartForDayWithFileNumber(2, new Day18(), 1, "58");
  }

  @Test
  void testPart2_Full() {
    runPartForDayWithFileNumber(2, new Day18(), 2, "2028");
  }
}
