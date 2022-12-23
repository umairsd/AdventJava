package com.umair.aoc.y2022;

import com.umair.aoc.common.Day;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day10 extends Day {

  private static final int MAX_CYCLES = 240;
  private static final int CRT_ROWS_COUNT = 6;
  private static final char LIT = '#';
  private static final char DARK = '.';

  public Day10() {
    super(10, 2022);
  }

  @Override
  protected String part1(List<String> lines) {
    Set<Integer> checkpoints = new HashSet<>(List.of(20, 60, 100, 140, 180, 220));
    int[] registerHistory = parseRegisterHistoryAtCycleEnd(lines);
    long signalStrength = checkpoints
        .stream()
        .reduce(0, (subTotal, e) -> subTotal + registerHistory[e - 1] * e);
    return Long.toString(signalStrength);
  }

  @Override
  protected String part2(List<String> lines) {
    int crtWidth = MAX_CYCLES / CRT_ROWS_COUNT;
    char[][] crt = new char[CRT_ROWS_COUNT][crtWidth];
    for (char[] row : crt) {
      Arrays.fill(row, ' ');
    }

    int spritePosition = 1;
    // Each index of the register history is the value at the end of that cycle.
    int[] registerHistory = parseRegisterHistoryAtCycleEnd(lines);

    // `cycle` is the next cycle that will end.
    for (int cycle = 1; cycle < MAX_CYCLES; cycle++) {
      int row = (cycle - 1) / crtWidth;
      int column = (cycle - 1) % crtWidth;

      int delta = Math.abs(spritePosition - column);
      crt[row][column] = delta <= 1 ? LIT : DARK;

      // End cycle.
      spritePosition = registerHistory[cycle];
    }

    StringBuilder sb = new StringBuilder("\n");
    for (char[] row : crt) {
      for (char pixel : row) {
        sb.append(pixel);
      }
      sb.append("\n");
    }
    return sb.toString();
  }

  @Override
  protected String part1Filename() {
    return fileNameFromFileNumber(3);
  }

  @Override
  protected String part2Filename() {
    return fileNameFromFileNumber(3);
  }

  /**
   * The value at each index i represents the value of the register X at the end of the cycle i.
   */
  private static int[] parseRegisterHistoryAtCycleEnd(List<String> lines) {
    int[] registerHistory = new int[MAX_CYCLES];
    int registerX = 1;
    // The next cycle that will end.
    int cycle = 0;

    for (String line : lines) {
      if (cycle > MAX_CYCLES - 1) {
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
    return registerHistory;
  }
}
