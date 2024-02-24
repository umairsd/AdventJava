package com.umair.aoc.y2021;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day09Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runExampleForDay(new Day09(), 1, 1, "15");
  }

  @Test
  void testPart1_Full() {
    runForDay(new Day09(), 1, "468");
  }

  @Test
  void testPart2_Example() {
    runExampleForDay(new Day09(), 2, 1, "1134");
  }

  @Test
  void testPart2_Full() {
    runForDay(new Day09(), 2, "1280496");
  }
}
