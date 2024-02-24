package com.umair.aoc.y2022;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day20Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runExampleForDay(new Day20(), 1, 1, "3");
  }

  @Test
  void testPart1_Full() {
    runForDay(new Day20(), 1, "3700");
  }

  @Test
  void testPart2_Example() {
    runExampleForDay(new Day20(), 2, 1, "1623178306");
  }

  @Test
  void testPart2_Full() {
    runForDay(new Day20(), 2, "10626948369382");
  }
}
