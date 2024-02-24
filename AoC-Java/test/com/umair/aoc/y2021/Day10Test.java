package com.umair.aoc.y2021;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day10Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runExampleForDay(new Day10(), 1, 1, "26397");
  }

  @Test
  void testPart1_Full() {
    runForDay(new Day10(), 1, "318081");
  }

  @Test
  void testPart2_Example() {
    runExampleForDay(new Day10(), 2, 1, "288957");
  }

  @Test
  void testPart2_Full() {
    runForDay(new Day10(), 2, "4361305341");
  }
}
