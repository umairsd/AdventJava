package com.umair.aoc.y2021;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day02Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runExampleForDay(new Day02(), 1, 1, "150");
  }

  @Test
  void testPart1_Full() {
    runForDay(new Day02(), 1, "2272262");
  }

  @Test
  void testPart2_Example() {
    runExampleForDay(new Day02(), 2, 1, "900");
  }

  @Test
  void testPart2_Full() {
    runForDay(new Day02(), 2, "2134882034");
  }
}
