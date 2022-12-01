package com.umair.aoc.y2022;

import com.umair.aoc.common.Day;

import java.util.*;
import java.util.stream.Stream;

import static com.umair.aoc.common.Constants.INPUT_EMPTY;

public class Day01 extends Day {

  public Day01() {
    super(1, 2022);
  }

  /** Find the elf carrying the most calories. */
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

    // Process the last line.
    maxCalories = Math.max(maxCalories, currentTotal);
    return Long.toString(maxCalories);
  }

  /** Find the top 3 elves carrying the most calories. */
  @Override
  protected String part2(List<String> lines) {
    if (lines.isEmpty()) { return INPUT_EMPTY; }

    PriorityQueue<Long> elfCalories = parseToPerElfCalories(lines);

    assert(elfCalories.size() >= 3);
    List<Long> result = Stream.generate(elfCalories::poll)
        .limit(3)
        .toList();
    long topThree = result.get(0) + result.get(1) + result.get(2);

    return Long.toString(topThree);
  }

  @Override
  protected String part1Filename() {
    return filenameFromDataFileNumber(2);
  }

  @Override
  protected String part2Filename() {
    return filenameFromDataFileNumber(2);
  }

  private static PriorityQueue<Long> parseToPerElfCalories(List<String> lines) {
    PriorityQueue<Long> elfCalories = new PriorityQueue<>(Collections.reverseOrder());
    long currentTotal = 0;

    for (String line : lines) {
      if (line.isEmpty()) {
        elfCalories.offer(currentTotal);
        // Reset state.
        currentTotal = 0;
        continue;
      }
      long calories = Long.parseLong(line);
      currentTotal += calories;
    }

    if (currentTotal != 0) {
      elfCalories.offer(currentTotal);
    }

    return elfCalories;
  }
}
