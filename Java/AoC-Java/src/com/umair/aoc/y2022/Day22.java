package com.umair.aoc.y2022;

import com.umair.aoc.common.Day;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Day 22: Monkey Map
 * <a href="https://adventofcode.com/2022/day/22">2022, Day-22</a>
 */
public class Day22 extends Day {

  private static final char WALL = '#';
  private static final char VOID = ' ';

  public Day22() {
    super(22, 2022);
  }

  @Override
  protected String part1(List<String> lines) {
    int splitIndex = lines.indexOf("");
    Board board = Board.parseBoard(lines.subList(0, splitIndex));
    List<Move> moves = Move.parseMoves(lines.get(lines.size() - 1));

    Position currentPosition = board.getStart();
    Direction currentDirection = Direction.RIGHT;

    for (Move m : moves) {
      switch (m.step) {
        case MOVE -> currentPosition = switch (currentDirection) {
          case DOWN -> board.moveVertically(m.distance, currentPosition);
          case LEFT -> board.moveHorizontally(-m.distance, currentPosition);
          case RIGHT -> board.moveHorizontally(m.distance, currentPosition);
          case UP -> board.moveVertically(-m.distance, currentPosition);
        };
        case TURN_LEFT -> // Counterclockwise.
            currentDirection = switch (currentDirection) {
              case RIGHT -> Direction.UP;
              case DOWN -> Direction.RIGHT;
              case LEFT -> Direction.DOWN;
              case UP -> Direction.LEFT;
            };
        case TURN_RIGHT -> // Clockwise.
            currentDirection = switch (currentDirection) {
              case RIGHT -> Direction.DOWN;
              case DOWN -> Direction.LEFT;
              case LEFT -> Direction.UP;
              case UP -> Direction.RIGHT;
            };
      }
    }

    int facing = switch (currentDirection) {
      case RIGHT -> 0;
      case DOWN -> 1;
      case LEFT -> 2;
      case UP -> 3;
    };

    int password = 1000 * (currentPosition.row + 1) + 4 * (currentPosition.column + 1) + facing;
    return Integer.toString(password);
  }

  @Override
  protected String part2(List<String> lines) {
    return null;
  }

  @Override
  protected String part1Filename() {
    return fileNameFromFileNumber(2);
  }

  @Override
  protected String part2Filename() {
    return fileNameFromFileNumber(1);
  }

  private static class Board {
    private final char[][] grid;
    private final int rowCount;
    private final int columnCount;

    Board(int rowCount, int columnCount) {
      this.rowCount = rowCount;
      this.columnCount = columnCount;
      this.grid = new char[rowCount][columnCount];
    }

    private Position getStart() {
      for (int c = 0; c < grid[0].length; c++) {
        if (grid[0][c] == '.') {
          return new Position(0, c);
        }
      }
      throw new IllegalStateException();
    }

    private void setValueAt(int row, int column, char value) {
      this.grid[row][column] = value;
    }

    private char getValueAt(int row, int column) {
      return this.grid[row][column];
    }

    private Position moveHorizontally(int distance, Position start) {
      int stepsToMove = Math.abs(distance);
      boolean isMovingRight = distance > 0;
      char value = isMovingRight ? '>' : '<';

      Optional<Integer> currentColumn = Optional.of(start.column);
      int column;

      do {
        column = currentColumn.get();
        setValueAt(start.row, column, value);

        currentColumn = getNextColumn(start.row, column, isMovingRight);
        stepsToMove--;
      } while (currentColumn.isPresent() && stepsToMove >= 0);

      return new Position(start.row, column);
    }

    private Optional<Integer> getNextColumn(
        int row,
        int column,
        boolean isMovingRight
    ) {
      int possibleNextColumn = isMovingRight
          ? (column + 1) % columnCount
          : Math.floorMod(column - 1, columnCount);

      if (possibleNextColumn == columnCount) {
        throw new IllegalStateException("This should not happen, given the logic.");

      } else if (getValueAt(row, possibleNextColumn) == VOID) {
        return getNextColumn(row, possibleNextColumn, isMovingRight);

      } else if (getValueAt(row, possibleNextColumn) == WALL) {
        return Optional.empty();

      } else {
        return Optional.of(possibleNextColumn);
      }
    }

    private Position moveVertically(int distance, Position start) {
      int stepsToMove = Math.abs(distance) + 1;
      boolean isMovingDown = distance > 0;
      char value = isMovingDown ? 'v' : '^';

      Optional<Integer> currentRow = Optional.of(start.row);
      int row;

      do {
        row = currentRow.get();
        // Start by drawing a path-value on the starting position.
        setValueAt(row, start.column, value);

        currentRow = getNextRow(row, start.column, isMovingDown);
        stepsToMove--;
      } while (currentRow.isPresent() && stepsToMove > 0);

      return new Position(row, start.column);
    }

    private Optional<Integer> getNextRow(int row, int column, boolean isMovingDown) {
      int possibleNextRow = isMovingDown
          ? (row + 1) % rowCount
          : Math.floorMod(row - 1, rowCount);

      if (possibleNextRow == rowCount) {
        throw new IllegalStateException("This should not happen, given the logic.");

      } else if (getValueAt(possibleNextRow, column) == VOID) {
        return getNextRow(possibleNextRow, column, isMovingDown);

      } else if (getValueAt(possibleNextRow, column) == WALL) {
        return Optional.empty();

      } else {
        return Optional.of(possibleNextRow);
      }
    }

    private static Board parseBoard(List<String> lines) {
      int rowCount = lines.size();
      int columnsCount = lines.stream().map(String::length).max(Integer::compare).orElseThrow();
      Board board = new Board(rowCount, columnsCount);

      for (int row = 0; row < rowCount; row++) {
        String line = lines.get(row);
        int column = 0;
        while (column < line.length()) {
          board.setValueAt(row, column, line.charAt(column));
          column++;
        }

        while (column < columnsCount) {
          board.setValueAt(row, column, VOID);
          column++;
        }
      }

      return board;
    }
  }

  private enum Direction {
    RIGHT,
    DOWN,
    LEFT,
    UP,
  }

  private enum Step {
    MOVE,
    TURN_LEFT,
    TURN_RIGHT
  }

  private record Move(Step step, int distance) {
    private static Move parseMove(String s, int startIndex, int endIndex) {
      String substring = s.substring(startIndex, endIndex);
      Move m = new Move(Step.MOVE, Integer.parseInt(substring));
      return m;
    }

    private static List<Move> parseMoves(String line) {
      int prevIndex = 0;
      int currentIndex = 0;
      List<Move> moves = new ArrayList<>();

      while (currentIndex < line.length()) {
        if (line.charAt(currentIndex) == 'R') {
          moves.add(Move.parseMove(line, prevIndex, currentIndex));
          moves.add(new Move(Step.TURN_RIGHT, 0));
          prevIndex = currentIndex + 1;

        } else if (line.charAt(currentIndex) == 'L') {
          moves.add(Move.parseMove(line, prevIndex, currentIndex));
          moves.add(new Move(Step.TURN_LEFT, 0));
          prevIndex = currentIndex + 1;
        }

        currentIndex++;
      }

      moves.add(Move.parseMove(line, prevIndex, currentIndex));
      return moves;
    }
  }

  private record Position(int row, int column) {
  }
}
