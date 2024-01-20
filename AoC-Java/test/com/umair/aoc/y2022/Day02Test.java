package com.umair.aoc.y2022;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day02Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runPartForDayWithFileNumber(1, new Day02(), 1, "15");
  }

  @Test
  void testPart1_Full() {
    runPartForDayWithFileNumber(1, new Day02(), 2, "12586");
  }

  @Test
  void testPart2_Example() {
    runPartForDayWithFileNumber(2, new Day02(), 1, "12");
  }

  @Test
  void testPart2_Full() {
    runPartForDayWithFileNumber(2, new Day02(), 2, "13193");
  }
}
