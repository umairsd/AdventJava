package com.umair.aoc.y2021;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day10Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runPartForDayWithFileNumber(1, new Day10(), 1, "26397");
  }

  @Test
  void testPart1_Full() {
    runPartForDayWithFileNumber(1, new Day10(), 2, "318081");
  }

  @Test
  void testPart2_Example() {
    runPartForDayWithFileNumber(2, new Day10(), 1, "288957");
  }

  @Test
  void testPart2_Full() {
    runPartForDayWithFileNumber(2, new Day10(), 2, "4361305341");
  }
}
