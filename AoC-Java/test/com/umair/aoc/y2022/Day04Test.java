package com.umair.aoc.y2022;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day04Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runExampleForDay(new Day04(), 1, 1, "2");
  }

  @Test
  void testPart1_Full() {
    runForDay(new Day04(), 1, "532");
  }

  @Test
  void testPart2_Example() {
    runExampleForDay(new Day04(), 2, 1, "4");
  }

  @Test
  void testPart2_Full() {
    runForDay(new Day04(), 2, "854");
  }
}
