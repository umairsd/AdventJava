package com.umair.aoc.y2021;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day07Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runPartForDayWithFileNumber(1, new Day07(), 1, "37");
  }

  @Test
  void testPart1_Full() {
    runPartForDayWithFileNumber(1, new Day07(), 2, "336721");
  }

  @Test
  void testPart2_Example() {
    runPartForDayWithFileNumber(2, new Day07(), 1, "168");
  }

  @Test
  void testPart2_Full() {
    runPartForDayWithFileNumber(2, new Day07(), 2, "91638945");
  }
}
