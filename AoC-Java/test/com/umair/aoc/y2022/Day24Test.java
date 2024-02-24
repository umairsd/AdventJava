package com.umair.aoc.y2022;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day24Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runExampleForDay(new Day24(), 1, 1, "18");
  }

  @Test
  void testPart1_Full() {
    runForDay(new Day24(), 1, "269");
  }

  @Test
  void testPart2_Example() {
    runExampleForDay(new Day24(), 2, 1, "54");
  }

  @Test
  void testPart2_Full() {
    runForDay(new Day24(), 2, "825");
  }
}
