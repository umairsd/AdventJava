package com.umair.aoc.y2021;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day14Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runPartForDayWithFileNumber(1, new Day14(), 1, "1588");
  }

  @Test
  void testPart1_Full() {
    runPartForDayWithFileNumber(1, new Day14(), 2, "2602");
  }

  @Test
  void testPart2_Example() {
    runPartForDayWithFileNumber(2, new Day14(), 1, "2188189693529");
  }

  @Test
  void testPart2_Full() {
    runPartForDayWithFileNumber(2, new Day14(), 2, "2942885922173");
  }
}
