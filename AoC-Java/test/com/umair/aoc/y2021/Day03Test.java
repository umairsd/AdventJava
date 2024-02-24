package com.umair.aoc.y2021;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day03Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runExampleForDay(new Day03(), 1, 1, "198");
  }

  @Test
  void testPart1_Full() {
    runForDay(new Day03(), 1, "2648450");
  }

  @Test
  void testPart2_Example() {
    runExampleForDay(new Day03(), 2, 1, "230");
  }

  @Test
  void testPart2_Full() {
    runForDay(new Day03(), 2, "2845944");
  }
}
