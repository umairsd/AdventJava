package com.umair.aoc.y2022;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day02Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runExampleForDay(new Day02(), 1, 1, "15");
  }

  @Test
  void testPart1_Full() {
    runForDay(new Day02(), 1, "12586");
  }

  @Test
  void testPart2_Example() {
    runExampleForDay(new Day02(), 2, 1, "12");
  }

  @Test
  void testPart2_Full() {
    runForDay(new Day02(), 2, "13193");
  }
}
