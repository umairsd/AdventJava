package com.umair.aoc.y2022;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day16Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runPartForDayWithFileNumber(1, new Day16(), 1, "1651");
  }

  @Test
  void testPart1_Full() {
    runPartForDayWithFileNumber(1, new Day16(), 2, "1659");
  }

  @Test
  void testPart2_Example() {
    runPartForDayWithFileNumber(2, new Day16(), 1, "1707");
  }

  @Test
  void testPart2_Full() {
    runPartForDayWithFileNumber(2, new Day16(), 2, "2382");
  }
}
