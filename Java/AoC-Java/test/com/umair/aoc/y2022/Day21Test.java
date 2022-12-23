package com.umair.aoc.y2022;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day21Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runPartForDayWithFileNumber(1, new Day21(), 1, "152");
  }

  @Test
  void testPart1_Full() {
    runPartForDayWithFileNumber(1, new Day21(), 2, "85616733059734");
  }

  @Test
  void testPart2_Example() {
    runPartForDayWithFileNumber(2, new Day21(), 1, "301");
  }

  @Test
  void testPart2_Full() {
    runPartForDayWithFileNumber(2, new Day21(), 2, "3560324848168");
  }
}
