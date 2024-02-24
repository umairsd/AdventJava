package com.umair.aoc.y2022;

import com.umair.aoc.common.DayTestBase;
import com.umair.aoc.util.FileUtils;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day22Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runExampleForDay(new Day22(), 1, 1, "6032");
  }

  @Test
  void testPart1_Full() {
    runForDay(new Day22(), 1, "136054");
  }

  @Test
  void testPart1Alternate_Full() {
    Day22 day = new Day22();
    String fileName = FileUtils.dataFilenameForDay(day);
    List<String> lines = day.readData(fileName);

    assertEquals("136054", day.part1Alternate(lines));
  }

  @Test
  void testPart2_Example() {
  }

  @Test
  void testPart2_Full() {
    runForDay(new Day22(), 2, "122153");
  }
}
