package com.umair.aoc.y2022;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day12Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runExampleForDay(new Day12(), 1, 1, "31");
  }

  @Test
  void testPart1_Full() {
    runForDay(new Day12(), 1, "472");
  }

  @Test
  void testPart2_Example() {
    runExampleForDay(new Day12(), 2, 1, "29");
  }

  @Test
  void testPart2_Full() {
    runForDay(new Day12(), 2, "465");
  }
}
