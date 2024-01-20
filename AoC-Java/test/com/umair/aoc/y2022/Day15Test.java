package com.umair.aoc.y2022;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day15Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    Day15 dSpy = Mockito.spy(new Day15());
    String fileName = dSpy.fileNameFromFileNumber(1);

    Mockito.when(dSpy.part1Filename()).thenReturn(fileName);
    Mockito.when(dSpy.getRowNumPart1()).thenReturn(10L);

    assertEquals("26", dSpy.solvePart1());
  }

  @Test
  void testPart1_Full() {
    runPartForDayWithFileNumber(1, new Day15(), 2, "4951427");
  }

  @Test
  void testPart2_Example() {
    runPartForDayWithFileNumber(2, new Day15(), 1, "56000011");
  }

  @Test
  void testPart2_Full() {
    runPartForDayWithFileNumber(2, new Day15(), 2, "13029714573243");
  }
}
