package com.umair.aoc.y2021;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day02Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runPartForDayWithFileNumber(1, new Day02(), 1, "150");
  }

  @Test
  void testPart1_Full() {
    runPartForDayWithFileNumber(1, new Day02(), 2, "2272262");
  }

  @Test
  void testPart2_Example() {
    runPartForDayWithFileNumber(2, new Day02(), 1, "900");
  }

  @Test
  void testPart2_Full() {
    runPartForDayWithFileNumber(2, new Day02(), 2, "2134882034");
  }
}
