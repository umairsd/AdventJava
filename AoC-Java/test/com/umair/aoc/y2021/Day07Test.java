package com.umair.aoc.y2021;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day07Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runExampleForDay(new Day07(), 1, 1, "37");
  }

  @Test
  void testPart1_Full() {
    runForDay(new Day07(), 1, "336721");
  }

  @Test
  void testPart2_Example() {
    runExampleForDay(new Day07(), 2, 1, "168");
  }

  @Test
  void testPart2_Full() {
    runForDay(new Day07(), 2, "91638945");
  }
}
