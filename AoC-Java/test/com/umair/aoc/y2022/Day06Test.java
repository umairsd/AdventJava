package com.umair.aoc.y2022;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day06Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runExampleForDay(new Day06(), 1, 1, "7");
  }

  @Test
  void testPart1_Full() {
    runForDay(new Day06(), 1, "1855");
  }

  @Test
  void testPart2_Example() {
    runExampleForDay(new Day06(), 2, 1, "19");
  }

  @Test
  void testPart2_Full() {
    runForDay(new Day06(), 2, "3256");
  }
}
