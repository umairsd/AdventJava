package com.umair.aoc.y2022;

import com.umair.aoc.common.Day;

import javax.swing.text.html.Option;
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

    while (true) {
      GridPosition start = new GridPosition(0, 500);
      Optional<GridPosition> newPosition = moveParticle(sandGrid, start);

      while (newPosition.isPresent()) {
        if (newPosition.get().equals(start)) {
          sandGrid[start.row][start.column] = SAND;
          depositedParticleCount++;
          break;
        }

        start = newPosition.get();
        newPosition = moveParticle(sandGrid, start);
      }

      if (newPosition.isEmpty()) {
        // The particle fell into the void.
        break;
      }
    }

    return Integer.toString(depositedParticleCount);
  }

  @Override
  protected String part2(List<String> lines) {
    List<List<GridPosition>> rockPositions = parseRockPositions(lines);
    char[][] sandGrid = buildSandGridWithPaths(rockPositions);
    // The last row is made from rocks.
    for (int c = 0; c < sandGrid[0].length; c++) {
      sandGrid[sandGrid.length - 1][c] = ROCK;
    }

    int depositedParticleCount = 0;

    // Note: Add a cache for a particle's path to optimize run-time. Without the cache, ~900ms.
    // With the cache, ~70ms.
    Stack<GridPosition> pathCache = new Stack<>();

    while (true) {
      GridPosition start = new GridPosition(0, 500);
      Optional<GridPosition> newPosition = moveParticle(sandGrid, start);

      if (newPosition.isPresent() && newPosition.get().equals(start)) {
        // The new position covers the starting point. We cannot make forward progress, so add
        // the sand particle, and exit.
        sandGrid[start.row][start.column] = SAND;
        depositedParticleCount++;
        break;
      }

      // From the cache, pop the last available position. If the position contains AIR, set the
      // new position to this value.
      while (!pathCache.isEmpty()) {
        GridPosition pos = pathCache.pop();
        if (sandGrid[pos.row][pos.column] == AIR) {
          newPosition = Optional.of(pos);
          break;
        }
      }

      while (newPosition.isPresent()) {
        if (newPosition.get().equals(start)) {
          sandGrid[start.row][start.column] = SAND;
          depositedParticleCount++;
          break;
        }

        pathCache.push(newPosition.get());
        start = newPosition.get();
        newPosition = moveParticle(sandGrid, start);
      }
    }

    return Integer.toString(depositedParticleCount);
  }

  @Override
  protected String part1Filename() {
    return filenameFromDataFileNumber(2);
  }

  @Override
  protected String part2Filename() {
    return filenameFromDataFileNumber(2);
  }

  /**
   * Attempts to move the particle. If the return value is:
   * - Optional.empty(), indicates that the particle has fallen through the abyss.
   * - Position that's the same as the starting position => particle has come to rest.
   * - New position of the particle.
   */
  private static Optional<GridPosition> moveParticle(
      char[][] sandGrid,
      GridPosition startingPosition
  ) {
    // Falling into the abyss!
    if (startingPosition.row + 1 == sandGrid.length) {
      return Optional.empty();
    }

    Optional<GridPosition> nextPosition = neighbors(startingPosition)
        .stream()
        .filter(p -> isWithinCave(sandGrid, p) && sandGrid[p.row][p.column] == AIR)
        .findFirst();

    if (nextPosition.isPresent()) {
      return nextPosition;
    }
    // Returning the starting position indicates that the particle can move no further.
    return Optional.of(startingPosition);
  }

  private static boolean isWithinCave(char[][] cave, GridPosition p) {
    return p.row < cave.length && p.column >= 0 && p.column < cave[0].length;
  }

  private static List<GridPosition> neighbors(GridPosition p) {
    return List.of(
      new GridPosition(p.row + 1, p.column),      // down
      new GridPosition(p.row + 1, p.column - 1),  // down-left
      new GridPosition(p.row + 1, p.column + 1)   // down-right
    );
  }

  private static char[][] buildSandGridWithPaths(List<List<GridPosition>> rockPositions) {
    int maxY = getMaxIntValue(rockPositions, p -> p.row);
    int maxX = getMaxIntValue(rockPositions, p -> p.column) + 500;

    char[][] sandGrid = new char[maxY + 3][maxX];
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
