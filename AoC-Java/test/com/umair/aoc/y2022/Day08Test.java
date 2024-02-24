package com.umair.aoc.y2022;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day08Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runExampleForDay(new Day08(), 1, 1, "21");
  }

  @Test
  void testPart1_Full() {
    runForDay(new Day08(), 1, "1792");
  }

  @Test
  void testPart2_Example() {
    runExampleForDay(new Day08(), 2, 1, "8");
  }

  @Test
  void testPart2_Full() {
    runForDay(new Day08(), 2, "334880");
  }
}
