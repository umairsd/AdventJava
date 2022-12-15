package com.umair.aoc.y2022;

import com.umair.aoc.common.Day;

import java.util.*;

/**
 * Day 14: Regolith Reservoir
 * <a href="https://adventofcode.com/2022/day/14">2022, Day-14</a>
 */
public class Day14 extends Day {

  private static final char SAND = 'o';
  private static final char AIR = '.';
  private static final char ROCK = '#';

  public Day14() {
    super(14, 2022);
  }


  @Override
  protected String part1(List<String> lines) {
    List<List<GridPosition>> rockPositions = parseRockPositions(lines);
    char[][] sandGrid = buildSandGridWithPaths(rockPositions);

    int depositedParticleCount = 0;
    GridPosition start = new GridPosition(500, 1);
    sandGrid[start.getRow() - 1][start.getColumn()] = '+';

    while (true) {
      GridPosition p = lowestPositionDownTheColumn(sandGrid, start.getRow(), start.getColumn());
      if (p == null) {
        // The particle falls into the void.
        break;
      }

      // If the particle can fall left and down, keep attempting to fall diagonally.
      GridPosition leftDown = new GridPosition(p);
      while (leftDown != null &&
          leftDown.column - 1 >= 0 &&
          leftDown.row + 1 < sandGrid.length &&
          sandGrid[leftDown.row + 1][leftDown.column - 1] == AIR) {
        leftDown = lowestPositionDownTheColumn(sandGrid, leftDown.row + 1, leftDown.column - 1);
      }

      if (leftDown == null) {
        // The particle falls into the void.
        break;
      }
      if (!leftDown.equals(p)) {
        depositedParticleCount++;
        sandGrid[leftDown.row][leftDown.column] = SAND;
        continue;
      }

      // If the particle can fall right and down, keep attempting to fall diagonally.
      GridPosition rightDown = new GridPosition(p);
      while (rightDown != null &&
          rightDown.column + 1 < sandGrid[rightDown.row].length &&
          rightDown.row + 1 < sandGrid.length &&
          sandGrid[rightDown.row + 1][rightDown.column + 1] == AIR) {
        rightDown = lowestPositionDownTheColumn(sandGrid, rightDown.row + 1, rightDown.column + 1);
      }

      if (rightDown == null) {
        // The particle falls into the void.
        break;
      }
      if (!rightDown.equals(p)) {
        depositedParticleCount++;
        sandGrid[rightDown.row][rightDown.column] = SAND;
        continue;
      }

      // Tried left, tried right. Doesn't matter. Just go with original position.
      sandGrid[p.row][p.column] = SAND;
      depositedParticleCount++;
    }

    return Integer.toString(depositedParticleCount);
  }

  /**
   * Finds the lowest position that's air.
   */
  private static GridPosition lowestPositionDownTheColumn(
      char[][] sandGrid,
      int startingRow,
      int startingColumn
  ) {
    for (int r = startingRow; r < sandGrid.length; r++) {
      if (sandGrid[r][startingColumn] != '.') {
        // First position that's not air:
        return new GridPosition(startingColumn, r - 1);
      }
    }
    return null;
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

  private static char[][] buildSandGridWithPaths(List<List<GridPosition>> rockPositions) {
    int maxX = getMaxIntValue(rockPositions, GridPosition::getColumn);
    int maxY = getMaxIntValue(rockPositions, GridPosition::getRow);

    char[][] sandGrid = new char[maxY + 1][maxX + 1];
    for (char[] row : sandGrid) {
      Arrays.fill(row, AIR);
    }

    // Fill the grid with rock positions.
    for (List<GridPosition> path : rockPositions) {
      for (int i = 1; i < path.size(); i++) {
        drawRockPath(path.get(i - 1), path.get(i), sandGrid);
      }
    }
    return sandGrid;
  }

  private static void drawRockPath(GridPosition from, GridPosition to, char[][] sandGrid) {
    if (from.getColumn() == to.getColumn()) {
      // Vertical Line.
      int minY = Math.min(from.getRow(), to.getRow());
      int maxY = Math.max(from.getRow(), to.getRow());
      for (int r = minY; r <= maxY; r++) {
        sandGrid[r][from.getColumn()] = ROCK;
      }

    } else if (from.getRow() == to.getRow()) {
      // Horizontal line.
      int minX = Math.min(from.getColumn(), to.getColumn());
      int maxX = Math.max(from.getColumn(), to.getColumn());
      for (int c = minX; c <= maxX; c++) {
        sandGrid[from.getRow()][c] = ROCK;
      }
    }
  }

  private static int getMaxIntValue(List<List<GridPosition>> rockPositions,
                                    MapGridPositionToInt f) {
    int maxValue = rockPositions.stream()
        .map(l -> l
            .stream()
            .map(f::getInt)
            .mapToInt(v -> v)
            .max().orElseThrow(NoSuchElementException::new))
        .mapToInt(v -> v)
        .max().orElseThrow(NoSuchElementException::new);
    return maxValue;
  }

  private static List<List<GridPosition>> parseRockPositions(List<String> lines) {
    List<List<GridPosition>> result = lines.stream()
        .map(Day14::parseOneRockPosition)
        .toList();
    return result;
  }

  private static List<GridPosition> parseOneRockPosition(String line) {
    String[] tokenPositions = line.strip().split("->");
    List<GridPosition> result = Arrays.stream(tokenPositions)
        .map(s -> {
          var t = s.strip().split(",");
          return new GridPosition(Integer.parseInt(t[0].strip()), Integer.parseInt(t[1].strip()));
        })
        .toList();

    return result;
  }

  private static class GridPosition {
    int column; // Distance from left.
    int row; // Distance from top.

    GridPosition(int column, int row) {
      this.column = column;
      this.row = row;
    }

    GridPosition(GridPosition p) {
      this.row = p.row;
      this.column = p.column;
    }

    int getColumn() {
      return column;
    }

    int getRow() {
      return row;
    }

    @Override
    public String toString() {
      return "r: " + row + ", c: " + column;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      GridPosition that = (GridPosition) o;
      return column == that.column && row == that.row;
    }

    @Override
    public int hashCode() {
      return Objects.hash(column, row);
    }
  }

  @FunctionalInterface
  private interface MapGridPositionToInt {
    int getInt(GridPosition p);
  }
}
