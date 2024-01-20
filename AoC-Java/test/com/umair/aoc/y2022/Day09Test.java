package com.umair.aoc.y2022;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day09Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runPartForDayWithFileNumber(1, new Day09(), 1, "13");
  }

  @Test
  void testPart1_Full() {
    runPartForDayWithFileNumber(1, new Day09(), 2, "6087");
  }

  @Test
  void testPart2_Example() {
    runPartForDayWithFileNumber(2, new Day09(), 1, "1");
    // Uses file 3, which is a larger example.
    runPartForDayWithFileNumber(2, new Day09(), 3, "36");
  }

  @Test
  void testPart2_Full() {
    runPartForDayWithFileNumber(2, new Day09(), 2, "2493");
  }
}
