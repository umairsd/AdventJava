package com.umair.aoc.y2022;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day23Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runPartForDayWithFileNumber(1, new Day23(), 0, "25");
    runPartForDayWithFileNumber(1, new Day23(), 1, "110");
  }

  @Test
  void testPart1_Full() {
    runPartForDayWithFileNumber(1, new Day23(), 2, "3862");
  }

  @Test
  void testPart2_Example() {
    runPartForDayWithFileNumber(2, new Day23(), 1, "20");
  }

  @Test
  void testPart2_Full() {
    runPartForDayWithFileNumber(2, new Day23(), 2, "913");
  }
}
