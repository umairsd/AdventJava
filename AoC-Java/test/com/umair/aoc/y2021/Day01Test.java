package com.umair.aoc.y2021;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day01Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runExampleForDay(new Day01(), 1, 1, "7");
  }

  @Test
  void testPart1_Full() {
    runForDay(new Day01(), 1, "1709");
  }

  @Test
  void testPart2_Example() {
    runExampleForDay(new Day01(), 2, 1, "5");
  }

  @Test
  void testPart2_Full() {
    runForDay(new Day01(), 2, "1761");
  }
}
