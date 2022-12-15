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
    GridPosition start = new GridPosition(1, 500);
    sandGrid[start.row - 1][start.column] = '+';

    while (true) {
      GridPosition newPosition = moveParticle(sandGrid, start);
      if (newPosition == null) {
        break;
      }
      sandGrid[newPosition.row][newPosition.column] = SAND;
      depositedParticleCount++;
    }

    return Integer.toString(depositedParticleCount);
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
    return filenameFromDataFileNumber(2);
  }

  private static GridPosition moveParticle(char[][] sandGrid, GridPosition startingPosition) {
    // Can the particle move down? If so, go!
    GridPosition p = lowestPositionDownTheColumn(sandGrid, startingPosition.row, startingPosition.column);
    if (p == null) {
      return null; // Fell into the void.
    }

    // If the particle can fall left and down, start from that position.
    if (p.column - 1 >= 0 &&
        p.row + 1 < sandGrid.length &&
        sandGrid[p.row + 1][p.column - 1] == AIR) {
      GridPosition newStartPosition = new GridPosition(p.row + 1, p.column - 1);
      GridPosition viaLeft = moveParticle(sandGrid, newStartPosition);
      return viaLeft;

    } else if (p.column + 1 < sandGrid[p.row].length &&
        p.row + 1 < sandGrid.length &&
        sandGrid[p.row + 1][p.column + 1] == AIR) {
      GridPosition newStartPosition = new GridPosition(p.row + 1, p.column + 1);
      GridPosition viaRight = moveParticle(sandGrid, newStartPosition);
      return viaRight;
    } else {
      // The particle fell down, but couldn't fall to left or right. This is the final position.
      return p;
    }
  }
  
  private static GridPosition lowestPositionDownTheColumn(
      char[][] sandGrid,
      int startingRow,
      int startingColumn
  ) {
    for (int r = startingRow; r < sandGrid.length; r++) {
      if (sandGrid[r][startingColumn] != '.') {
        // First position that's not air:
        return new GridPosition(r - 1, startingColumn);
      }
    }
    return null;
  }

  private static char[][] buildSandGridWithPaths(List<List<GridPosition>> rockPositions) {
    int maxX = getMaxIntValue(rockPositions, p -> p.column);
    int maxY = getMaxIntValue(rockPositions, p -> p.row);

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
    if (from.column == to.column) {
      // Vertical Line.
      int minY = Math.min(from.row, to.row);
      int maxY = Math.max(from.row, to.row);
      for (int r = minY; r <= maxY; r++) {
        sandGrid[r][from.column] = ROCK;
      }

    } else if (from.row == to.row) {
      // Horizontal line.
      int minX = Math.min(from.column, to.column);
      int maxX = Math.max(from.column, to.column);
      for (int c = minX; c <= maxX; c++) {
        sandGrid[from.row][c] = ROCK;
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
          var x = Integer.parseInt(t[0].strip());
          var y = Integer.parseInt(t[1].strip());
          return new GridPosition(y, x);
        })
        .toList();

    return result;
  }

  private record GridPosition(int row, int column){}

  @FunctionalInterface
  private interface MapGridPositionToInt {
    int getInt(GridPosition p);
  }
}
