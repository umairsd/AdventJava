package com.umair.aoc.y2022;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day17Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runPartForDayWithFileNumber(1, new Day17(), 1, "3068");
  }

  @Test
  void testPart1_Full() {
    runPartForDayWithFileNumber(1, new Day17(), 2, "3147");
  }

  @Test
  void testPart2_Example() {
    runPartForDayWithFileNumber(2, new Day17(), 1, "1514285714288");
  }

  @Test
  void testPart2_Full() {
    runPartForDayWithFileNumber(2, new Day17(), 2, "1532163742758");
  }
}
