package com.umair.aoc.y2022;

import com.umair.aoc.common.Day;

import java.util.List;

public class Day08 extends Day {

  public Day08() {
    super(8, 2022);
  }

  @Override
  protected String part1(List<String> lines) {
    int[][] treeGrid = parseTreeGrid(lines);

    int rowCount = treeGrid.length;
    int columnCount = treeGrid[0].length;

    int[][] tallestOnLeft = new int[rowCount][columnCount];
    for (int r = 0; r < rowCount; r++) {
      // For the first element in a row, there is nothing to the left, hence the height is zero.
      for (int c = 1; c < columnCount; c++) {
        tallestOnLeft[r][c] = Math.max(tallestOnLeft[r][c - 1], treeGrid[r][c - 1]);
      }
    }

    int[][] tallestOnRight = new int[rowCount][columnCount];
    for (int r = 0; r < rowCount; r++) {
      for (int c = columnCount - 2; c >= 0; c--) {
        tallestOnRight[r][c] = Math.max(tallestOnRight[r][c + 1], treeGrid[r][c + 1]);
      }
    }

    int[][] tallestOnTop = new int[rowCount][columnCount];
    for (int r = 1; r < rowCount; r++) {
      for (int c = 0; c < columnCount; c++) {
        tallestOnTop[r][c] = Math.max(tallestOnTop[r - 1][c], treeGrid[r - 1][c]);
      }
    }

    int[][] tallestOnBottom = new int[rowCount][columnCount];
    for (int r = rowCount - 2; r >= 0; r--) {
      for (int c = 0; c < columnCount; c++) {
        tallestOnBottom[r][c] = Math.max(tallestOnBottom[r + 1][c], treeGrid[r + 1][c]);
      }
    }

    int visibleCount = rowCount * 2 + (columnCount - 2) * 2;
    for (int r = 1; r < rowCount - 1; r++) {
      for (int c = 1; c < columnCount - 1; c++) {
        int height = treeGrid[r][c];

        boolean isVisibleFromLeft = height > tallestOnLeft[r][c];
        boolean isVisibleFromRight = height > tallestOnRight[r][c];
        boolean isVisibleFromTop = height > tallestOnTop[r][c];
        boolean isVisibleFromBottom = height > tallestOnBottom[r][c];

        if (isVisibleFromBottom || isVisibleFromTop || isVisibleFromLeft || isVisibleFromRight) {
          visibleCount++;
        }
      }
    }

    return Integer.toString(visibleCount);
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

  private static int[][] parseTreeGrid(List<String> lines) {
    int rowCount = lines.size();
    int columnCount = lines.get(0).length();

    int[][] grid = new int[rowCount][columnCount];
    for (int r = 0; r < rowCount; r++) {
      String line = lines.get(r);
      for (int c = 0; c < columnCount; c++) {
        grid[r][c] = Character.getNumericValue(line.charAt(c));
      }
    }
    return grid;
  }

  private static class TallestNeighbor {
    int fromLeft;
    int fromRight;
    int fromTop;
    int fromBottom;

    TallestNeighbor(int value) {
      fromLeft = value;
      fromRight = value;
      fromTop = value;
      fromBottom = value;
    }

    @Override
    public String toString() {
      return "(l: " + fromLeft + ", r: " + fromRight + "), (t: " + fromTop + ", b: " + fromBottom + ")";
    }
  }
}
