package com.umair.aoc.y2022;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day13Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runExampleForDay(new Day13(), 1, 1, "13");
  }

  @Test
  void testPart1_Full() {
    runForDay(new Day13(), 1, "5717");
  }

  @Test
  void testPart2_Example() {
    runExampleForDay(new Day13(), 2, 1, "140");
  }

  @Test
  void testPart2_Full() {
    runForDay(new Day13(), 2, "25935");
  }
}
