package com.umair.aoc.y2021;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day03Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runPartForDayWithFileNumber(1, new Day03(), 1, "198");
  }

  @Test
  void testPart1_Full() {
    runPartForDayWithFileNumber(1, new Day03(), 2, "2648450");
  }

  @Test
  void testPart2_Example() {
    runPartForDayWithFileNumber(2, new Day03(), 1, "230");
  }

  @Test
  void testPart2_Full() {
    runPartForDayWithFileNumber(2, new Day03(), 2, "2845944");
  }
}
