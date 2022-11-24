package com.umair.aoc.y2021;

import com.umair.aoc.common.Day;

import java.util.*;

public class Day04 extends Day {
  private static final int SIZE = 5;

  public Day04() {
    super(4, 2021);
  }

  /**
   * Figure out which board wins first.
   */
  @Override
  protected String part1(List<String> lines) {
    Context gameContext = buildContext(lines);

    GridNode[][] grid = new GridNode[0][0];
    int winningMove = -1;

    for (int move : gameContext.moves) {
      // For each move, get the list of addresses to update:
      List<Address> addresses = gameContext.moveToAddressesMap.get(move);
      boolean foundBingo = false;
      // Update each grid address that contains this move.
      for (Address address : addresses) {
        grid = gameContext.grids.get(address.gridId);
        grid[address.row][address.column].visited = true;

        if (isBingo(grid, address.row, address.column)) {
          winningMove = move;
          foundBingo = true;
          break;
        }
      }

      if (foundBingo) { break; }
    }

    // Generate sum of unvisited nodes
    long sumOfUnvisited = 0;
    for (GridNode[] gridRow : grid) {
      for (GridNode gridNode : gridRow) {
        if (!gridNode.visited) {
          sumOfUnvisited += gridNode.value;
        }
      }
    }

    long result = sumOfUnvisited * winningMove;
    return Long.toString(result);
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


  private static boolean isBingo(GridNode[][] grid, int row, int column) {
    // Check the row.
    boolean rowAllVisited = Arrays.stream(grid[row]).allMatch(gn -> gn.visited);
    if (rowAllVisited) {
      return true;
    }

    // Check the column.
    for (GridNode[] gridRow : grid) {
      if (!gridRow[column].visited) {
        return false;
      }
    }
    return true;
  }

  private static Context buildContext(List<String> lines) {
    Context c = new Context();
    // First line is the list of moves.
    parseMoves(lines.get(0), c);

    // Build a map of all the moves
    for (int m : c.moves) {
      c.moveToAddressesMap.put(m, new ArrayList<>());
    }

    int startingIdx = 1;
    while (startingIdx < lines.size() && lines.get(startingIdx).isBlank()) {
      startingIdx++;
    }

    // Go line by line
    int currentGridId = 0;
    int currentRow = 0;

    GridNode[][] currentGrid = new GridNode[SIZE][SIZE];
    for (int i = startingIdx; i < lines.size(); i++) {
      String line = lines.get(i);

      if (line.isBlank()) {
        // Add the current grid to the map of grids.
        c.grids.put(currentGridId, currentGrid);

        currentRow = 0;
        currentGridId++;
        currentGrid = new GridNode[SIZE][SIZE];
        continue;
      }

      // Line isn't blank. Lets go!
      String[] tokens = line.strip().split("\\s+");
      for (int column = 0; column < tokens.length; column++) {
        int move = Integer.parseInt(tokens[column]);
        currentGrid[currentRow][column] = new GridNode(move);

        List<Address> addresses = c.moveToAddressesMap.get(move);
        addresses.add(new Address(currentGridId, currentRow, column));
      }
      currentRow++;
    }

    c.grids.put(currentGridId, currentGrid);
    return c;
  }

  private static void parseMoves(String line, Context c) {
    c.moves.clear();

    String[] tokens = line.split(",");
    c.moves.addAll(Arrays.stream(tokens)
        .map(String::strip)
        .map(Integer::parseInt)
        .toList());
  }

  private static class Context {
    // List of moves
    private final List<Integer> moves = new ArrayList<>();
    // A map between a given move and the list of addresses that contain that value.
    private final Map<Integer, List<Address>> moveToAddressesMap = new HashMap<>();
    // A map between a given gridId and the corresponding grid. Grid id is assigned
    // based on the order in which grids are parsed.
    private final Map<Integer, GridNode[][]> grids = new HashMap<>();
  }

  private static class Address {
    int gridId;
    int row;
    int column;
    Address(int g, int r, int c) {
      this.gridId = g;
      this.row = r;
      this.column = c;
    }
  }

  private static class GridNode {
    int value;
    boolean visited = false;
    GridNode(int v) {
      this.value = v;
    }

    @Override
    public String toString() {
      return Integer.toString(value);
    }
  }
}
