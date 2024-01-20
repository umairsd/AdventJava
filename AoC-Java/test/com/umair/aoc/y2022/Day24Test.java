package com.umair.aoc.y2022;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day24Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runPartForDayWithFileNumber(1, new Day24(), 1, "18");
  }

  @Test
  void testPart1_Full() {
    runPartForDayWithFileNumber(1, new Day24(), 2, "269");
  }

  @Test
  void testPart2_Example() {
    runPartForDayWithFileNumber(2, new Day24(), 1, "54");
  }

  @Test
  void testPart2_Full() {
    runPartForDayWithFileNumber(2, new Day24(), 2, "825");
  }
}
