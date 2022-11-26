package com.umair.aoc.y2021;

import com.umair.aoc.common.Day;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.umair.aoc.common.Constants.ERROR;
import static com.umair.aoc.common.Constants.INPUT_EMPTY;

public class Day09 extends Day {

  public Day09() {
    super(9, 2021);
  }

  @Override
  protected String part1(List<String> lines) {
    if (lines.isEmpty()) {
      return INPUT_EMPTY;
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
        if (isLowPoint(grid, row, column)) {
          totalRiskLevel += (grid[row][column] + 1);
        }
      }
    }

    return Integer.toString(totalRiskLevel);
  }

  @Override
  protected String part2(List<String> lines) {
    if (lines.isEmpty()) {
      return INPUT_EMPTY;
    }
    int rowsCount = lines.size();
    int columnCount = lines.get(0).length();

    int[][] grid = new int[rowsCount][columnCount];
    for (int row = 0; row < lines.size(); row++) {
      grid[row] = lines.get(row).chars().map(Character::getNumericValue).toArray();
    }

    boolean[][] visited = new boolean[rowsCount][columnCount];
    List<Integer> basinSizes = new ArrayList<>();

    for (int row = 0; row < rowsCount; row++) {
      for (int column = 0; column < columnCount; column++) {
        if (isLowPoint(grid, row, column)) {
          // Start a DFS scan starting from the current lowest point.
          int basinSize = dfsVisit(grid, visited, new Coordinate(row, column));
          basinSizes.add(basinSize);
        }
      }
    }

    basinSizes.sort(Collections.reverseOrder());
    if (basinSizes.size() < 3) {
      return ERROR;
    }

    int threeLargestBasins = basinSizes.get(0) * basinSizes.get(1) * basinSizes.get(2);
    return Integer.toString(threeLargestBasins);
  }

  @Override
  protected String part1Filename() {
    return filenameFromDataFileNumber(2);
  }

  @Override
  protected String part2Filename() {
    return filenameFromDataFileNumber(2);
  }

  private static int dfsVisit(int[][] grid, boolean[][] visited, Coordinate coordinate) {
    assert(canVisit(grid, visited, coordinate));

    visited[coordinate.row][coordinate.column] = true;
    int basinSize = 1; // Visiting the current coordinate.

    for (Coordinate neighbor : neighbors(coordinate)) {
      if (canVisit(grid, visited, neighbor)) {
        basinSize += dfsVisit(grid, visited, neighbor);
      }
    }

    return basinSize;
  }

  private static boolean canVisit(int[][] grid, boolean[][] visited, Coordinate coordinate) {
    int rowCount = grid.length;
    int columnCount = grid[0].length;
    if (coordinate.row < 0 || coordinate.row >= rowCount) {
      return false;
    }
    if (coordinate.column < 0 || coordinate.column >= columnCount) {
      return false;
    }
    if (visited[coordinate.row][coordinate.column]) {
      return false;
    }
    if (grid[coordinate.row][coordinate.column] == 9) {
      return false;
    }
    return true;
  }

  private static List<Coordinate> neighbors(Coordinate c) {
    return List.of(
        new Coordinate(c.row - 1, c.column),
        new Coordinate(c.row + 1, c.column),
        new Coordinate(c.row, c.column + 1),
        new Coordinate(c.row, c.column - 1));
  }

  private static boolean isLowPoint(int[][] grid, int row, int column) {
    int currentHeight = grid[row][column];

    return currentHeight < safeGridValue(grid, row - 1, column) &&
        currentHeight < safeGridValue(grid, row + 1, column) &&
        currentHeight < safeGridValue(grid, row, column - 1) &&
        currentHeight < safeGridValue(grid, row, column + 1);
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

  private static class Coordinate {
    int row;
    int column;
    Coordinate(int row, int column) {
      this.row = row;
      this.column = column;
    }
  }
}
