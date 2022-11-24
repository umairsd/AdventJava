package com.umair.aoc.y2021;

import com.umair.aoc.common.Constants;
import com.umair.aoc.common.Day;

import java.util.List;
import java.util.stream.Collectors;

public class Day03 extends Day {

  public Day03() {
    super(3, 2021);
  }

  /**
   * Finds the power consumption.
   */
  @Override
  protected String part1(List<String> lines) {
    if (lines.isEmpty()) {
      return Constants.INPUT_EMPTY;
    }

    int[] countsOfOneDigit = countsOneBitPerPosition(lines);
    long gamma = 0;
    long epsilon = 0;

    // Each bit in the gamma rate can be determined by finding the most common bit in the
    // corresponding position of all numbers in the diagnostic report.
    // Similarly, epsilon rate can be determined by finding the least common bit.
    for (int count : countsOfOneDigit) {
      gamma = gamma << 1;
      epsilon = epsilon << 1;
      if (count > 0) {
        // Most common bit is a 1.
        gamma = gamma | 1;
      } else {
        // Most common bit is a 0.
        epsilon = epsilon | 1;
      }
    }

    long powerConsumption = gamma * epsilon;
    return Long.toString(powerConsumption);
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

  /**
   * Counts the *net* count of the 1 bit at each position. Net count of 1s in a position is
   * obtained by the (total count of 1s - total count of 0s).
   * <p></p>
   * Assumes: each element in `lines` is the same width.
   */
  private static int[] countsOneBitPerPosition(List<String> lines) {
    if (lines.isEmpty()) { throw new IllegalArgumentException("The lines array is empty."); }

    int[] result = new int[lines.get(0).length()];
    for (String line : lines) {
      for (int i = 0; i < line.length(); i++) {
        char c = line.charAt(i);
        // If `c` is 1, increment the count. Else, decrement.
        result[i] += (c == '1' ? 1 : -1);
      }
    }

    return result;
  }
}
