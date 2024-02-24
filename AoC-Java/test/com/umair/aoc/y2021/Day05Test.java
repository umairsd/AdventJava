package com.umair.aoc.y2021;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day05Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runExampleForDay(new Day05(), 1, 1, "5");
  }

  @Test
  void testPart1_Full() {
    runForDay(new Day05(), 1, "5576");
  }

  @Test
  void testPart2_Example() {
    runExampleForDay(new Day05(), 2, 1, "12");
  }

  @Test
  void testPart2_Full() {
    runForDay(new Day05(), 2, "18144");
  }
}
