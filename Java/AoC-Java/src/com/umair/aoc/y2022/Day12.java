package com.umair.aoc.y2022;

import com.umair.aoc.common.Day;

import java.util.*;

/**
 * Day 12: Hill Climbing Algorithm
 * <a href="https://adventofcode.com/2022/day/12">2022, Day-12</a>
 */
public class Day12 extends Day {

  public Day12() {
    super(12, 2022);
  }

  @Override
  protected String part1(List<String> lines) {
    char[][] grid = parseGrid(lines);
    int minPathLength = findShortestPathFromAllStartingPoints(grid, Set.of('S'));
    return Integer.toString(minPathLength);
  }

  @Override
  protected String part2(List<String> lines) {
    char[][] grid = parseGrid(lines);
    int minPathLength = findShortestPathFromAllStartingPoints(grid, Set.of('a', 'S'));
    return Integer.toString(minPathLength);
  }

  private static int findShortestPathFromAllStartingPoints(
      char[][] grid, Set<Character> startingPointValues
  ) {
    List<Point> startingPoints = new ArrayList<>();
    Point end = new Point(0, 0);
    for (int r = 0; r < grid.length; r++) {
      for (int c = 0; c < grid[r].length; c++) {
        if (startingPointValues.contains(grid[r][c])) {
          startingPoints.add(new Point(r, c));
          grid[r][c] = 'a';

        } else if (grid[r][c] == 'E') {
          end = new Point(r, c);
          grid[r][c] = 'z';
        }
      }
    }

    int minPathLength = Integer.MAX_VALUE;
    for (Point start : startingPoints) {
      int pathLength = shortestPathLength(grid, start, end);
      minPathLength = Math.min(minPathLength, pathLength);
    }

    return minPathLength;
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
   * Uses BFS to find the shortest path.
   */
  private static int shortestPathLength(char[][] grid, Point start, Point end) {
    // Whether a given point has been visited.
    Set<Point> visited = new HashSet<>();
    // Queue for maintaining the BFS expansion.
    Queue<Point> queue = new ArrayDeque<>();
    queue.add(start);

    // The shortest path length from the `start` to the given node
    Map<Point, Integer> pathLengths = new HashMap<>();
    pathLengths.put(start, 0);

    while (!queue.isEmpty()) {
      Point currentPoint = queue.poll();

      if (currentPoint.equals(end)) {
        return pathLengths.get(currentPoint);
      }

      // Expand through the neighbors.
      List<Point> neighbors = validNeighbors(grid, currentPoint, visited);
      for (Point neighbor : neighbors) {
        visited.add(neighbor);
        queue.add(neighbor);
        pathLengths.put(neighbor, pathLengths.get(currentPoint) + 1);
      }
    }

    return Integer.MAX_VALUE;
  }

  private static List<Point> validNeighbors(
      char[][] grid,
      Point point,
      Set<Point> visited
  ) {
    List<Point> possibleNeighbors = List.of(
        new Point(point.row, point.column + 1),
        new Point(point.row, point.column - 1),
        new Point(point.row - 1, point.column),
        new Point(point.row + 1, point.column));

    List<Point> neighbors = possibleNeighbors.stream()
        .filter(p -> isWithinGrid(grid, p))
        .filter(p -> !visited.contains(p))
        .filter(p ->
            getHeight(grid[p.row][p.column]) <= (getHeight(grid[point.row][point.column]) + 1))
        .toList();
    return neighbors;
  }

  private static boolean isWithinGrid(char[][] grid, Point p) {
    int rowCount = grid.length;
    int columnCount = grid[0].length;

    if (p.row < 0 || p.row >= rowCount) {
      return false;
    }
    return p.column >= 0 && p.column < columnCount;
  }

  private static int getHeight(Character c) {
    if (Character.isLowerCase(c)) {
      return c - 'a' + 1;
    } else {
      return c - 'A' + 1 + 26;
    }
  }

  private static char[][] parseGrid(List<String> lines) {
    char[][] grid = new char[lines.size()][lines.get(0).length()];
    for (int r = 0; r < lines.size(); r++) {
      String line = lines.get(r);
      grid[r] = line.toCharArray();
    }
    return grid;
  }

  private record Point(int row, int column) {
  }
}
