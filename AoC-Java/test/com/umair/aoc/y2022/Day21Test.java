package com.umair.aoc.y2022;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day21Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runExampleForDay(new Day21(), 1, 1, "152");
  }

  @Test
  void testPart1_Full() {
    runForDay(new Day21(), 1, "85616733059734");
  }

  @Test
  void testPart2_Example() {
    runExampleForDay(new Day21(), 2, 1, "301");
  }

  @Test
  void testPart2_Full() {
    runForDay(new Day21(), 2, "3560324848168");
  }
}
