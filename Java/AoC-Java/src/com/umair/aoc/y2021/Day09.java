package com.umair.aoc.y2021;

import com.umair.aoc.common.Constants;
import com.umair.aoc.common.Day;

import java.util.List;

public class Day09 extends Day {

  public Day09() {
    super(9, 2021);
  }

  @Override
  protected String part1(List<String> lines) {
    if (lines.isEmpty()) {
      return Constants.INPUT_EMPTY;
    }
    int rowsCount = lines.size();
    int columnCount = lines.get(0).length();

    int[][] grid = new int[rowsCount][columnCount];
    for (int row = 0; row < lines.size(); row++) {
      grid[row] = lines.get(row).chars().map(Character::getNumericValue).toArray();
    }

    int totalRiskLevel = 0;

    for (int row = 0; row < rowsCount; row++) {
      for (int column = 0; column < columnCount; column++) {
        int currentHeight = grid[row][column];

        if (currentHeight < safeGridValue(grid, row - 1, column) &&
            currentHeight < safeGridValue(grid, row + 1, column) &&
            currentHeight < safeGridValue(grid, row, column - 1) &&
            currentHeight < safeGridValue(grid, row, column + 1)) {

          totalRiskLevel += (currentHeight + 1);
        }
      }
    }

    return Integer.toString(totalRiskLevel);
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

  private static int safeGridValue(int[][] grid, int row, int column) {
    int rowCount = grid.length;
    int columnCount = grid[0].length;

    if ((row < 0 || row >= rowCount) ||
        (column < 0 || column >= columnCount)) {
      return Integer.MAX_VALUE;
    }

    return grid[row][column];
  }
}
