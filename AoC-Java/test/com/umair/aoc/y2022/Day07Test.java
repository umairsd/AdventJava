package com.umair.aoc.y2022;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

class Day07Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runExampleForDay(new Day07(), 1, 1, "95437");
  }

  @Test
  void testPart1_Full() {
    runForDay(new Day07(), 1, "1886043");
  }

  @Test
  void testPart2_Example() {
    runExampleForDay(new Day07(), 2, 1, "24933642");
  }

  @Test
  void testPart2_Full() {
    runForDay(new Day07(), 2, "3842121");
  }
}
