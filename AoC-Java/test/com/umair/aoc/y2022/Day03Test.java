package com.umair.aoc.y2022;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day03Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runExampleForDay(new Day03(), 1, 1, "157");
  }

  @Test
  void testPart1_Full() {
    runForDay(new Day03(), 1, "8298");
  }

  @Test
  void testPart2_Example() {
    runExampleForDay(new Day03(), 2, 1, "70");
  }

  @Test
  void testPart2_Full() {
    runForDay(new Day03(), 2, "2708");
  }
}
