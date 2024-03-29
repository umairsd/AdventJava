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
    List<Integer> positions = Arrays.stream(lines.get(0).split(","))
        .map(String::strip)
        .map(Integer::parseInt)
        .toList();

    int maxPosition = positions.stream().max(Integer::compare).orElse(0);
    long minFuelNeeded = Long.MAX_VALUE;

    for (int newP = 0; newP < maxPosition; newP++) {
      long fuelToNewPosition = 0;
      for (int p : positions) {
        // Use sum of arithmetic progression formula.
        long n = Math.abs(p - newP);
        long d = 1;
        long a = 1;
        long sum = (n * (2 * a + (n - 1) * d)) / 2;

        fuelToNewPosition += sum;
      }

      minFuelNeeded = Math.min(minFuelNeeded, fuelToNewPosition);
    }

    return Long.toString(minFuelNeeded);
  }
}
