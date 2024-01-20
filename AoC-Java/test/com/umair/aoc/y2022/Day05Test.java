package com.umair.aoc.y2022;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day05Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runPartForDayWithFileNumber(1, new Day05(), 1, "CMZ");
  }

  @Test
  void testPart1_Full() {
    runPartForDayWithFileNumber(1, new Day05(), 2, "QNNTGTPFN");
  }

  @Test
  void testPart2_Example() {
    runPartForDayWithFileNumber(2, new Day05(), 1, "MCD");
  }

  @Test
  void testPart2_Full() {
    runPartForDayWithFileNumber(2, new Day05(), 2, "GGNPJBTTR");
  }

}
