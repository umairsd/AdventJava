package com.umair.aoc.y2021;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day05Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runPartForDayWithFileNumber(1, new Day05(), 1, "5");
  }

  @Test
  void testPart1_Full() {
    runPartForDayWithFileNumber(1, new Day05(), 2, "5576");
  }

  @Test
  void testPart2_Example() {
    runPartForDayWithFileNumber(2, new Day05(), 1, "12");
  }

  @Test
  void testPart2_Full() {
    runPartForDayWithFileNumber(2, new Day05(), 2, "18144");
  }
}
