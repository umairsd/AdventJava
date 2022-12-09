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
    TallestNeighbor[][] tallestNeighbors = new TallestNeighbor[rowCount][columnCount];
    for (int r = 0; r < rowCount; r++) {
      for (int c = 0; c < columnCount; c++) {
        tallestNeighbors[r][c] = new TallestNeighbor(treeGrid[r][c]);
      }
    }

    // Fill rows first.
    for (int r = 1; r < rowCount - 1; r++) {
      // Left to right, and top to bottom.
      for (int c = 1; c < columnCount - 1; c++) {
        int treeHeight = treeGrid[r][c];
        int tallestOnLeft = tallestNeighbors[r][c - 1].fromLeft;
        tallestNeighbors[r][c].fromLeft = Math.max(treeHeight, tallestOnLeft);

        int tallestAbove = tallestNeighbors[r - 1][c].fromTop;
        tallestNeighbors[r][c].fromTop = Math.max(treeHeight, tallestAbove);
      }

      // Right to left
      for (int c = columnCount - 2; c > 0; c--) {
        int treeHeight = treeGrid[r][c];
        int tallestOnRight = tallestNeighbors[r][c + 1].fromRight;
        tallestNeighbors[r][c].fromRight = Math.max(treeHeight, tallestOnRight);
      }
    }

    for (int r = rowCount - 2; r > 0; r--) {
      // Bottom to top
      for (int c = 1; c < columnCount - 1; c++) {
        int treeHeight = treeGrid[r][c];
        int tallestBelow = tallestNeighbors[r + 1][c].fromBottom;
        tallestNeighbors[r][c].fromBottom = Math.max(treeHeight, tallestBelow);
      }
    }

    int visibleCount = rowCount * 2 + (columnCount - 2) * 2;
    for (int r = 1; r < rowCount - 1; r++) {
      for (int c = 1; c < columnCount - 1; c++) {
        int height = treeGrid[r][c];
        TallestNeighbor t = tallestNeighbors[r][c];
        boolean isVisibleFromLeft = height > t.fromLeft;
        boolean isVisibleFromRight = height > t.fromRight;
        boolean isVisibleFromTop = height > t.fromTop;
        boolean isVisibleFromBottom = height > t.fromBottom;

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
    return filenameFromDataFileNumber(1);
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
