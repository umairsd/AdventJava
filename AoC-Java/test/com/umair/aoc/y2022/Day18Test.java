package com.umair.aoc.y2022;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day18Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runExampleForDay(new Day18(), 1, 1, "64");
  }

  @Test
  void testPart1_Full() {
    runForDay(new Day18(), 1, "3550");
  }

  @Test
  void testPart2_Example() {
    runExampleForDay(new Day18(), 2, 1, "58");
  }

  @Test
  void testPart2_Full() {
    runForDay(new Day18(), 2, "2028");
  }
}
