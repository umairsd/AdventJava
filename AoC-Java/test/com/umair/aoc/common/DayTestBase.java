package com.umair.aoc.common;

import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DayTestBase {

  public void runPartForDayWithFileNumber(int part, Day d, int fileNumber, String expectedValue) {
    Day dSpy = Mockito.spy(d);
    String fileName = dSpy.fileNameFromFileNumber(fileNumber);

    if (part == 1) {
      Mockito.when(dSpy.part1Filename()).thenReturn(fileName);
      assertEquals(expectedValue, dSpy.solvePart1());
    } else if (part == 2) {
      Mockito.when(dSpy.part2Filename()).thenReturn(fileName);
      assertEquals(expectedValue, dSpy.solvePart2());
    }
  }
}
