package com.umair.aoc.y2022;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day13Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runPartForDayWithFileNumber(1, new Day13(), 1, "13");
  }

  @Test
  void testPart1_Full() {
    runPartForDayWithFileNumber(1, new Day13(), 2, "5717");
  }

  @Test
  void testPart2_Example() {
    runPartForDayWithFileNumber(2, new Day13(), 1, "140");
  }

  @Test
  void testPart2_Full() {
    runPartForDayWithFileNumber(2, new Day13(), 2, "25935");
  }
}
