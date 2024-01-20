package com.umair.aoc.y2022;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day07Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runPartForDayWithFileNumber(1, new Day07(), 1, "95437");
  }

  @Test
  void testPart1_Full() {
    runPartForDayWithFileNumber(1, new Day07(), 2, "1886043");
  }

  @Test
  void testPart2_Example() {
    runPartForDayWithFileNumber(2, new Day07(), 1, "24933642");
  }

  @Test
  void testPart2_Full() {
    runPartForDayWithFileNumber(2, new Day07(), 2, "3842121");
  }
}
