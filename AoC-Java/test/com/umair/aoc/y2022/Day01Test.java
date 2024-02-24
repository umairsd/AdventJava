package com.umair.aoc.y2022;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day01Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runExampleForDay(new Day01(), 1, 1, "24000");
  }

  @Test
  void testPart1_Full() {
    runForDay(new Day01(), 1, "71780");
  }

  @Test
  void testPart2_Example() {
    runExampleForDay(new Day01(), 2, 1, "45000");
  }

  @Test
  void testPart2_Full() {
    runForDay(new Day01(), 2, "212489");
  }
}
