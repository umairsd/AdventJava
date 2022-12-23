package com.umair.aoc.y2022;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day21Test {

  @Test
  void testPart1_Example() {
    Day21 dSpy = Mockito.spy(new Day21());
    String fileName = dSpy.fileNameFromFileNumber(1);
    Mockito.when(dSpy.part1Filename()).thenReturn(fileName);

    assertEquals("152", dSpy.solvePart1());
  }

  @Test
  void testPart1_Full() {
    Day21 dSpy = Mockito.spy(new Day21());
    String fileName = dSpy.fileNameFromFileNumber(2);
    Mockito.when(dSpy.part1Filename()).thenReturn(fileName);

    assertEquals("85616733059734", dSpy.solvePart1());
  }

  @Test
  void testPart2_Example() {
    Day21 dSpy = Mockito.spy(new Day21());
    String fileName = dSpy.fileNameFromFileNumber(1);
    Mockito.when(dSpy.part2Filename()).thenReturn(fileName);

    assertEquals("301", dSpy.solvePart2());
  }

  @Test
  void testPart2_Full() {
    Day21 dSpy = Mockito.spy(new Day21());
    String fileName = dSpy.fileNameFromFileNumber(2);
    Mockito.when(dSpy.part2Filename()).thenReturn(fileName);

    assertEquals("3560324848168", dSpy.solvePart2());
  }
}
