package com.umair.aoc.y2022;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day19Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runExampleForDay(new Day19(), 1, 1, "33");
  }

  @Test
  void testPart1_Full() {
    runForDay(new Day19(), 1, "960");
  }

  @Test
  void testPart2_Example() {
    // This takes a long time (3+ minutes).
//    runExampleForDay(new Day19(), 2, 1, "3472");
  }

  @Test
  void testPart2_Full() {
    runForDay(new Day19(), 2, "2040");
  }
}
