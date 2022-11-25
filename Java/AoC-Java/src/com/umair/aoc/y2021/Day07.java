package com.umair.aoc.y2021;

import com.umair.aoc.common.Day;

import java.util.Arrays;
import java.util.List;

public class Day07 extends Day {

  public Day07() {
    super(7, 2021);
  }

  @Override
  protected String part1(List<String> lines) {
    List<Integer> positions = Arrays.stream(lines.get(0).split(","))
        .map(String::strip)
        .map(Integer::parseInt)
        .toList();

    int maxPosition = positions.stream().max(Integer::compare).orElse(0);
    long minFuelNeeded = Long.MAX_VALUE;

    for (int newP = 0; newP < maxPosition; newP++) {
      long fuelToNewPosition = 0;
      for (int p : positions) {
        fuelToNewPosition += Math.abs(p - newP);
      }

      minFuelNeeded = Math.min(minFuelNeeded, fuelToNewPosition);
    }

    return Long.toString(minFuelNeeded);
  }

  @Override
  protected String part2(List<String> lines) {
    return null;
  }

  @Override
  protected String part1Filename() {
    return filenameFromDataFileNumber(2);
  }

  @Override
  protected String part2Filename() {
    return filenameFromDataFileNumber(1);
  }
}
