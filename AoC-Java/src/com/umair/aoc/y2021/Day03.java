package com.umair.aoc.y2021;

import com.umair.aoc.common.Constants;
import com.umair.aoc.util.DataUtils;
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
    lines = DataUtils.removeBlankLines(lines);
    if (lines.isEmpty()) {
      return Constants.INPUT_EMPTY;
    }

    int[] countsOfOneDigit = netCountOfOnesAtEachIndex(lines);
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

  /**
   * Finds the life support rating.
   */
  @Override
  protected String part2(List<String> lines) {
    lines = DataUtils.removeBlankLines(lines);
    if (lines.isEmpty()) {
      return Constants.INPUT_EMPTY;
    }

    // Part-1: Compute the oxygen generator rating.
    // To find oxygen generator rating, determine the most common value (0 or 1) in the current
    // bit position, and keep only numbers with that bit in that position. If 0 and 1 are equally
    // common, keep values with a 1 in the position being considered.
    List<String> filteredLines = lines;
    int index = 0; // Start with the most significant digit.

    while (filteredLines.size() > 1) {
      int[] countsOfOneDigit = netCountOfOnesAtEachIndex(filteredLines);
      char mostCommonDigit = countsOfOneDigit[index] >= 0 ? '1' : '0';
      int idx = index; // Needed to use the index within the lambda.
      filteredLines = filteredLines
          .stream()
          .filter(l -> l.charAt(idx) == mostCommonDigit)
          .collect(Collectors.toList());

      index += 1;
    }
    long oxygenGeneratorRating = Long.parseLong(filteredLines.get(0), 2);

    // Part-2: Compute the CO2 scrubber rating.
    // To find CO2 scrubber rating, determine the least common value (0 or 1) in the current bit
    // position, and keep only numbers with that bit in that position. If 0 and 1 are equally
    // common, keep values with a 0 in the position being considered.
    filteredLines = lines;
    // Start with the most significant digit.
    index = 0;

    while (filteredLines.size() > 1) {
      int[] countsOfOneDigit = netCountOfOnesAtEachIndex(filteredLines);
      char leastCommonDigit = countsOfOneDigit[index] >= 0 ? '0' : '1';

      int idx = index; // Needed to use the index within the lambda.
      filteredLines = filteredLines
          .stream()
          .filter(l -> l.charAt(idx) == leastCommonDigit)
          .collect(Collectors.toList());
      index += 1;
    }
    long cO2ScrubberRating = Long.parseLong(filteredLines.get(0), 2);

    // Part 3: Calculate the life support rating.
    long lifeSupportRating = oxygenGeneratorRating * cO2ScrubberRating;
    return Long.toString(lifeSupportRating);
  }

  @Override
  protected String part1Filename() {
    return fileNameFromFileNumber(2);
  }

  @Override
  protected String part2Filename() {
    return fileNameFromFileNumber(2);
  }

  /**
   * Counts the *net* count of the 1 bit at each position. Net count of 1s in a position is
   * obtained by the (total count of 1s - total count of 0s).
   * <p></p>
   * Assumes: each element in `lines` is the same width.
   */
  private static int[] netCountOfOnesAtEachIndex(List<String> lines) {
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
