package com.umair.aoc.y2022;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day09Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runExampleForDay(new Day09(), 1, 1, "13");
  }

  @Test
  void testPart1_Full() {
    runForDay(new Day09(), 1, "6087");
  }

  @Test
  void testPart2_Example() {
    runExampleForDay(new Day09(), 2, 1, "1");
    // Uses file 3, which is a larger example.
    runExampleForDay(new Day09(), 2, 3, "36");
  }

  @Test
  void testPart2_Full() {
    runForDay(new Day09(), 2, "2493");
  }
}
