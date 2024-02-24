package com.umair.aoc.y2022;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day23Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runExampleForDay(new Day23(), 1, 0, "25");
    runExampleForDay(new Day23(), 1, 1, "110");
  }

  @Test
  void testPart1_Full() {
    runForDay(new Day23(), 1, "3862");
  }

  @Test
  void testPart2_Example() {
    runExampleForDay(new Day23(), 2, 1, "20");
  }

  @Test
  void testPart2_Full() {
    runForDay(new Day23(), 2, "913");
  }
}
