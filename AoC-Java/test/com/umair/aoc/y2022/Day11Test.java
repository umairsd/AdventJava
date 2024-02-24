package com.umair.aoc.y2022;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day11Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runExampleForDay(new Day11(), 1, 1, "10605");
  }

  @Test
  void testPart1_Full() {
    runForDay(new Day11(), 1, "99840");
  }

  @Test
  void testPart2_Example() {
    runExampleForDay(new Day11(), 2, 1, "2713310158");
  }

  @Test
  void testPart2_Full() {
    runForDay(new Day11(), 2, "20683044837");
  }
}
