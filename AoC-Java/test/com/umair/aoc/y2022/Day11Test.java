package com.umair.aoc.y2022;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day11Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runPartForDayWithFileNumber(1, new Day11(), 1, "10605");
  }

  @Test
  void testPart1_Full() {
    runPartForDayWithFileNumber(1, new Day11(), 2, "99840");
  }

  @Test
  void testPart2_Example() {
    runPartForDayWithFileNumber(2, new Day11(), 1, "2713310158");
  }

  @Test
  void testPart2_Full() {
    runPartForDayWithFileNumber(2, new Day11(), 2, "20683044837");
  }
}
