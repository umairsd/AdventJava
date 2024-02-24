package com.umair.aoc.common;

import com.umair.aoc.util.FileUtils;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DayTestBase {

  public void runForDay(Day day, int part, String expectedValue) {
    String fileName = FileUtils.dataFilenameForDay(day);
    List<String> lines = day.readData(fileName);

    if (part == 1) {
      assertEquals(expectedValue, day.part1(lines));
    } else {
      assertEquals(expectedValue, day.part2(lines));
    }
  }

  public void runExampleForDay(Day day, int part, int fileNumber, String expectedValue) {
    String fileName = FileUtils.exampleFilenameForDay(day, fileNumber);
    List<String> lines = day.readData(fileName);

    if (part == 1) {
      assertEquals(expectedValue, day.part1(lines));
    } else {
      assertEquals(expectedValue, day.part2(lines));
    }
  }
}
