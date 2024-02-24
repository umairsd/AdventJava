package com.umair.aoc.y2022;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day14Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runExampleForDay(new Day14(), 1, 1, "24");
  }

  @Test
  void testPart1_Full() {
    runForDay(new Day14(), 1, "1072");
  }

  @Test
  void testPart2_Example() {
    runExampleForDay(new Day14(), 2, 1, "93");
  }

  @Test
  void testPart2_Full() {
    runForDay(new Day14(), 2, "24659");
  }
}
