package com.umair.aoc.y2022;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day22Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runPartForDayWithFileNumber(1, new Day22(), 1, "6032");
  }

  @Test
  void testPart1_Full() {
    runPartForDayWithFileNumber(1, new Day22(), 2, "136054");
  }

  @Test
  void testPart1Alternate_Full() {
    Day22 dSpy = Mockito.spy(new Day22());
    String fileName = dSpy.fileNameFromFileNumber(2);
    List<String> lines = dSpy.readData(fileName);

    Mockito.when(dSpy.readData(fileName)).thenReturn(lines);
    assertEquals("136054", dSpy.part1Alternate(lines));
  }

  @Test
  void testPart2_Example() {
  }

  @Test
  void testPart2_Full() {
    runPartForDayWithFileNumber(1, new Day22(), 2, "122153");
  }
}
