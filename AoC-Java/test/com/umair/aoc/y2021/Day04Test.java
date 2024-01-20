package com.umair.aoc.y2021;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day04Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runPartForDayWithFileNumber(1, new Day04(), 1, "4512");
  }

  @Test
  void testPart1_Full() {
    runPartForDayWithFileNumber(1, new Day04(), 2, "11536");
  }

  @Test
  void testPart2_Example() {
    runPartForDayWithFileNumber(2, new Day04(), 1, "1924");
  }

  @Test
  void testPart2_Full() {
    runPartForDayWithFileNumber(2, new Day04(), 2, "1284");
  }
}
