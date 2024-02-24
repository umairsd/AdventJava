package com.umair.aoc.y2022;

import com.umair.aoc.common.DayTestBase;
import com.umair.aoc.util.FileUtils;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day15Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    Day15 day = new Day15();
    String fileName = FileUtils.exampleFilenameForDay(day, 1);
    List<String> lines = day.readData(fileName);
    assertEquals("26", day.part1(lines, 10L));
  }

  @Test
  void testPart1_Full() {
    runForDay(new Day15(), 1, "4951427");
  }

  @Test
  void testPart2_Example() {
    runExampleForDay(new Day15(), 2, 1, "56000011");
  }

  @Test
  void testPart2_Full() {
    runForDay(new Day15(), 2, "13029714573243");
  }
}
