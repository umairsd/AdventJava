package com.umair.aoc.y2021;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day12Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runExampleForDay(new Day12(), 1, 1, "10");
    runExampleForDay(new Day12(), 1, 2, "19");
    runExampleForDay(new Day12(), 1, 3, "226");
  }

  @Test
  void testPart1_Full() {
    runForDay(new Day12(), 1, "5228");
  }

  @Test
  void testPart2_Example() {
    runExampleForDay(new Day12(), 2, 1, "36");
    runExampleForDay(new Day12(), 2, 2, "103");
    runExampleForDay(new Day12(), 2, 3, "3509");
  }

  @Test
  void testPart2_Full() {
    runForDay(new Day12(), 4, "131228");
  }
}
