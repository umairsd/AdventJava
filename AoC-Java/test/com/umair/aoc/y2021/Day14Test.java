package com.umair.aoc.y2021;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day14Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runExampleForDay(new Day14(), 1, 1, "1588");
  }

  @Test
  void testPart1_Full() {
    runForDay(new Day14(), 1, "2602");
  }

  @Test
  void testPart2_Example() {
    runExampleForDay(new Day14(), 2, 1, "2188189693529");
  }

  @Test
  void testPart2_Full() {
    runForDay(new Day14(), 2, "2942885922173");
  }
}
