package com.umair.aoc.y2022;

import com.umair.aoc.common.Day;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day10 extends Day {

  private static final int MAX_CYCLES = 220;

  public Day10() {
    super(10, 2022);
  }

  @Override
  protected String part1(List<String> lines) {
    Set<Integer> checkpoints = new HashSet<>(List.of(20, 60, 100, 140, 180, 220));
    int[] registerHistory = new int[MAX_CYCLES + 1];
    int registerX = 1;
    // The next cycle that will end.
    int cycle = 1;

    for (String line : lines) {
      if (cycle >= MAX_CYCLES) {
        break;
      }

      if (line.strip().equals("noop")) {
        // Write the current value of X at the end of the cycle.
        registerHistory[cycle++] = registerX;
      } else {
        String[] tokens = line.split(" ");
        int value = Integer.parseInt(tokens[1].strip());
        registerHistory[cycle++] = registerX;
        registerHistory[cycle++] = registerX;

        // Update the register.
        registerX += value;
      }
    }

    // If there are no more instructions, yet we still have cycles left, fill out the value of X.
    Arrays.fill(registerHistory, cycle, registerHistory.length, registerX);

    long signalStrength = checkpoints
        .stream()
        .reduce(0, (subTotal, e) -> subTotal + registerHistory[e] * e);
    return Long.toString(signalStrength);
  }

  @Override
  protected String part2(List<String> lines) {
    return null;
  }

  @Override
  protected String part1Filename() {
    return filenameFromDataFileNumber(3);
  }

  @Override
  protected String part2Filename() {
    return filenameFromDataFileNumber(1);
  }
}
