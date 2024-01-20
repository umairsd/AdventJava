package com.umair.aoc.y2022;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day20Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runPartForDayWithFileNumber(1, new Day20(), 1, "3");
  }

  @Test
  void testPart1_Full() {
    runPartForDayWithFileNumber(1, new Day20(), 2, "3700");
  }

  @Test
  void testPart2_Example() {
    runPartForDayWithFileNumber(2, new Day20(), 1, "1623178306");
  }

  @Test
  void testPart2_Full() {
    runPartForDayWithFileNumber(2, new Day20(), 2, "10626948369382");
  }
}
