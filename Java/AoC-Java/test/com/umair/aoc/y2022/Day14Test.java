package com.umair.aoc.y2022;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day14Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runPartForDayWithFileNumber(1, new Day14(), 1, "24");
  }

  @Test
  void testPart1_Full() {
    runPartForDayWithFileNumber(1, new Day14(), 2, "1072");
  }

  @Test
  void testPart2_Example() {
    runPartForDayWithFileNumber(2, new Day14(), 1, "93");
  }

  @Test
  void testPart2_Full() {
    runPartForDayWithFileNumber(2, new Day14(), 2, "24659");
  }
}
