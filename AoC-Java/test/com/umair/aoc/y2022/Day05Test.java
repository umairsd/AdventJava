package com.umair.aoc.y2022;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day05Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runExampleForDay(new Day05(), 1, 1, "CMZ");
  }

  @Test
  void testPart1_Full() {
    runForDay(new Day05(), 1, "QNNTGTPFN");
  }

  @Test
  void testPart2_Example() {
    runExampleForDay(new Day05(), 2, 1, "MCD");
  }

  @Test
  void testPart2_Full() {
    runForDay(new Day05(), 2, "GGNPJBTTR");
  }

}
