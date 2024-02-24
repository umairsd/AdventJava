package com.umair.aoc.y2021;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day04Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runExampleForDay(new Day04(), 1, 1, "4512");
  }

  @Test
  void testPart1_Full() {
    runForDay(new Day04(), 1, "11536");
  }

  @Test
  void testPart2_Example() {
    runExampleForDay(new Day04(), 2,1,"1924");
  }

  @Test
  void testPart2_Full() {
    runForDay(new Day04(), 2, "1284");
  }
}
