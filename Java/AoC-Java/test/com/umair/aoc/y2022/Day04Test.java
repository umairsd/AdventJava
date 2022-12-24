package com.umair.aoc.y2022;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day04Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runPartForDayWithFileNumber(1, new Day04(), 1, "2");
  }

  @Test
  void testPart1_Full() {
    runPartForDayWithFileNumber(1, new Day04(), 2, "532");
  }

  @Test
  void testPart2_Example() {
    runPartForDayWithFileNumber(2, new Day04(), 1, "4");
  }

  @Test
  void testPart2_Full() {
    runPartForDayWithFileNumber(2, new Day04(), 2, "854");
  }
}
