package com.umair.aoc.y2021;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day11Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runPartForDayWithFileNumber(1, new Day11(), 1, "1656");
  }

  @Test
  void testPart1_Full() {
    runPartForDayWithFileNumber(1, new Day11(), 2, "1673");
  }

  @Test
  void testPart2_Example() {
    runPartForDayWithFileNumber(2, new Day11(), 1, "195");
  }

  @Test
  void testPart2_Full() {
    runPartForDayWithFileNumber(2, new Day11(), 2, "279");
  }
}
