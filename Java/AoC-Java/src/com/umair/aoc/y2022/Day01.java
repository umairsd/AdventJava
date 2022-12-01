package com.umair.aoc.y2022;

import com.umair.aoc.common.Day;
import java.util.List;

import static com.umair.aoc.common.Constants.INPUT_EMPTY;

public class Day01 extends Day {

  public Day01() {
    super(1, 2022);
  }

  @Override
  protected String part1(List<String> lines) {
    if (lines.isEmpty()) { return INPUT_EMPTY; }

    long maxCalories = Long.MIN_VALUE;
    long currentTotal = 0;

    for (String line : lines) {
      if (line.isEmpty()) {
        maxCalories = Math.max(maxCalories, currentTotal);
        // Reset state.
        currentTotal = 0;
        continue;
      }

      long calories = Long.parseLong(line);
      currentTotal += calories;
    }

    return Long.toString(maxCalories);
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
