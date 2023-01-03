package com.umair.aoc.y2022;

import com.umair.aoc.common.Day;

import java.util.*;

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
    List<Instruction> instructions = Instruction.parseInstructions(lines.get(lines.size() - 1));

    Orientation orientation = new Orientation(board.getStart(), Direction.RIGHT);

    for (Instruction m : instructions) {
      switch (m.instructionType) {
        case FORWARD -> {
          var newPosition = switch (orientation.direction) {
            case DOWN -> board.moveVertically(m.distance, orientation.position);
            case LEFT -> board.moveHorizontally(-m.distance, orientation.position);
            case RIGHT -> board.moveHorizontally(m.distance, orientation.position);
            case UP -> board.moveVertically(-m.distance, orientation.position);
          };
          orientation = new Orientation(newPosition, orientation.direction);
        }
        case TURN_LEFT ->
            orientation = new Orientation(orientation.position, orientation.direction.turnLeft());
        case TURN_RIGHT ->
            orientation = new Orientation(orientation.position, orientation.direction.turnRight());
      }
    }

    int password = orientation.calculatePassword();
    return Integer.toString(password);
  }

  /**
   * Based on <a href="https://todd.ginsberg.com/post/advent-of-code/2022/day22/">...</a>
   */
  protected String part1Alternate(List<String> lines) {
    int splitIndex = lines.indexOf("");
    Cube cube = Cube.parseCube(lines.subList(0, splitIndex));
    List<Instruction> instructions = Instruction.parseInstructions(lines.get(lines.size() - 1));

    CubeFace startingFace = Cube.cubeFaceMap.get(1);
    Orientation finalOrientation = followInstructionsPart1(instructions, startingFace, cube);

    int password = finalOrientation.calculatePassword();
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
    return fileNameFromFileNumber(2);
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

    private Optional<Integer> getNextColumn(int row, int column, boolean isMovingRight) {
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

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      for (int row = 0; row < rowCount; row++) {
        for (int column = 0; column < columnCount; column++) {
          sb.append(grid[row][column]);
        }
        sb.append("\n");
      }

      return sb.toString();
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
    UP;

    private int points() {
      return switch (this) {
        case RIGHT -> 0;
        case DOWN -> 1;
        case LEFT -> 2;
        case UP -> 3;
      };
    }

    private Position movementOffset() {
      return switch (this) {
        case RIGHT -> new Position(0, 1);
        case DOWN -> new Position(1, 0);
        case LEFT -> new Position(0, -1);
        case UP -> new Position(-1, 0);
      };
    }

    // Anti-Clockwise.
    private Direction turnLeft() {
      return switch (this) {
        case RIGHT -> UP;
        case DOWN -> RIGHT;
        case LEFT -> DOWN;
        case UP -> LEFT;
      };
    }

    private Direction turnRight() {
      return switch (this) {
        case RIGHT -> DOWN;
        case DOWN -> LEFT;
        case LEFT -> UP;
        case UP -> RIGHT;
      };
    }
  }

  private enum InstructionType {
    FORWARD,
    TURN_LEFT,
    TURN_RIGHT
  }

  private record Instruction(InstructionType instructionType, int distance) {

    private static Instruction parseInstruction(String s, int startIndex, int endIndex) {
      String substring = s.substring(startIndex, endIndex);
      Instruction m = new Instruction(InstructionType.FORWARD, Integer.parseInt(substring));
      return m;
    }

    private static List<Instruction> parseInstructions(String line) {
      int prevIndex = 0;
      int currentIndex = 0;
      List<Instruction> instructions = new ArrayList<>();

      while (currentIndex < line.length()) {
        if (line.charAt(currentIndex) == 'R') {
          instructions.add(Instruction.parseInstruction(line, prevIndex, currentIndex));
          instructions.add(new Instruction(InstructionType.TURN_RIGHT, 0));
          prevIndex = currentIndex + 1;

        } else if (line.charAt(currentIndex) == 'L') {
          instructions.add(Instruction.parseInstruction(line, prevIndex, currentIndex));
          instructions.add(new Instruction(InstructionType.TURN_LEFT, 0));
          prevIndex = currentIndex + 1;
        }

        currentIndex++;
      }

      instructions.add(Instruction.parseInstruction(line, prevIndex, currentIndex));
      return instructions;
    }
  }

  private record Position(int row, int column) {
    private Position applyOffset(Position p) {
      return new Position(row + p.row, column + p.column);
    }
  }

  private record Orientation(Position position, Direction direction) {
    /**
     * Move by 1 in the orientation's direction.
     */
    private Orientation moveByOne() {
      return new Orientation(position.applyOffset(direction.movementOffset()), direction);
    }

    private int calculatePassword() {
      int password = 1000 * (position.row + 1) + 4 * (position.column + 1) + direction.points();
      return password;
    }
  }

  private static Orientation followInstructionsPart1(
      List<Instruction> instructions,
      CubeFace startingFace,
      Cube cube
  ) {
    CubeFace cubeFace = startingFace;
    var orientation = new Orientation(cubeFace.topLeft, Direction.RIGHT);

    for (Instruction m : instructions) {
      switch (m.instructionType) {
        case FORWARD -> {
          boolean shouldKeepMoving = true;
          int count = 0;
          while (count++ < m.distance && shouldKeepMoving) {
            Orientation nextOrientation = orientation.moveByOne();
            CubeFace nextCubeFace = cubeFace;

            if (!cubeFace.contains(nextOrientation.position)) {
              // Moved off the face of this cube. Figure out our next cube.
              var transition = cubeFace.getTransitionForDirection(orientation.direction);
              nextOrientation = new Orientation(
                  transition.transitionToFaceWithinCube(cube, orientation.position),
                  transition.enterDirection);
              nextCubeFace = cube.getCubeFaceMap().get(transition.destinationId);
            }

            if (cube.blockedPositions.contains(nextOrientation.position)) {
              // Can't move here.
              shouldKeepMoving = false;
            } else {
              orientation = nextOrientation;
              cubeFace = nextCubeFace;
            }
          }
        }
        case TURN_LEFT ->
            orientation = new Orientation(orientation.position, orientation.direction.turnLeft());
        case TURN_RIGHT ->
            orientation = new Orientation(orientation.position, orientation.direction.turnRight());
      }
    }

    return orientation;
  }

  private record Transition(int sourceId, int destinationId, Direction exitDirection, Direction enterDirection) {
    // moveByOneWithinCube
    Position transitionToFaceWithinCube(Cube cube, Position position) {
      if (exitDirection == Direction.RIGHT && enterDirection == Direction.RIGHT) {
        CubeFace destinationCubeFace = cube.getCubeFaceMap().get(destinationId);
        return new Position(position.row, destinationCubeFace.minColumn());

      } else if (exitDirection == Direction.DOWN && enterDirection == Direction.DOWN) {
        CubeFace destinationCubeFace = cube.getCubeFaceMap().get(destinationId);
        return new Position(destinationCubeFace.minRow(), position.column);

      } else if (exitDirection == Direction.UP && enterDirection == Direction.UP) {
        CubeFace destinationCubeFace = cube.getCubeFaceMap().get(destinationId);
        return new Position(destinationCubeFace.maxRow(), position.column);

      } else if (exitDirection == Direction.LEFT && enterDirection == Direction.LEFT) {
        CubeFace destinationCubeFace = cube.getCubeFaceMap().get(destinationId);
        return new Position(position.row, destinationCubeFace.maxColumn());

      } else {
        throw new IllegalStateException(
            "Part 1: No transition from " + exitDirection + " to " + enterDirection);
      }
    }
  }

  private record Cube(Set<Position> blockedPositions) {
    private static final Map<Integer, CubeFace> cubeFaceMap = new HashMap<>();

    /*
            +-----+-----+
            |     |     |
            |  1  |  2  |
            |     |     |
            +-----+-----+
            |     |
            |  3  |
            |     |
      +-----+-----+
      |     |     |
      |  4  |  5  |
      |     |     |
      +-----+-----+
      |     |
      |  6  |
      |     |
      +-----+

     */
    static {
      var cube1 = new CubeFace.CubeFaceBuilder()
          .setId(1)
          .setSize(50)
          .setTopLeft(new Position(0, 50))
          .setUp(new Transition(1, 5, Direction.UP, Direction.UP))
          .setRight(new Transition(1, 2, Direction.RIGHT, Direction.RIGHT))
          .setDown(new Transition(1, 3, Direction.DOWN, Direction.DOWN))
          .setLeft(new Transition(1, 2, Direction.LEFT, Direction.LEFT))
          .build();
      cubeFaceMap.put(cube1.id, cube1);

      cubeFaceMap.put(2, new CubeFace.CubeFaceBuilder()
          .setId(2)
          .setSize(50)
          .setTopLeft(new Position(0, 100))
          .setUp(new Transition(2, 2, Direction.UP, Direction.UP))
          .setRight(new Transition(2, 1, Direction.RIGHT, Direction.RIGHT))
          .setDown(new Transition(2, 2, Direction.DOWN, Direction.DOWN))
          .setLeft(new Transition(2, 1, Direction.LEFT, Direction.LEFT))
          .build());

      cubeFaceMap.put(3, new CubeFace.CubeFaceBuilder()
          .setId(3)
          .setSize(50)
          .setTopLeft(new Position(50, 50))
          .setUp(new Transition(3, 1, Direction.UP, Direction.UP))
          .setRight(new Transition(3, 3, Direction.RIGHT, Direction.RIGHT))
          .setDown(new Transition(3, 5, Direction.DOWN, Direction.DOWN))
          .setLeft(new Transition(3, 3, Direction.LEFT, Direction.LEFT))
          .build());

      cubeFaceMap.put(4, new CubeFace.CubeFaceBuilder()
          .setId(4)
          .setSize(50)
          .setTopLeft(new Position(100, 0))
          .setUp(new Transition(4, 6, Direction.UP, Direction.UP))
          .setRight(new Transition(4, 5, Direction.RIGHT, Direction.RIGHT))
          .setDown(new Transition(4, 6, Direction.DOWN, Direction.DOWN))
          .setLeft(new Transition(4, 5, Direction.LEFT, Direction.LEFT))
          .build());

      cubeFaceMap.put(5, new CubeFace.CubeFaceBuilder()
          .setId(5)
          .setSize(50)
          .setTopLeft(new Position(100, 50))
          .setUp(new Transition(5, 3, Direction.UP, Direction.UP))
          .setRight(new Transition(5, 4, Direction.RIGHT, Direction.RIGHT))
          .setDown(new Transition(5, 1, Direction.DOWN, Direction.DOWN))
          .setLeft(new Transition(5, 4, Direction.LEFT, Direction.LEFT))
          .build());

      cubeFaceMap.put(6, new CubeFace.CubeFaceBuilder()
          .setId(6)
          .setSize(50)
          .setTopLeft(new Position(150, 0))
          .setUp(new Transition(6, 4, Direction.UP, Direction.UP))
          .setRight(new Transition(6, 6, Direction.RIGHT, Direction.RIGHT))
          .setDown(new Transition(6, 4, Direction.DOWN, Direction.DOWN))
          .setLeft(new Transition(6, 6, Direction.LEFT, Direction.LEFT))
          .build());
    }

    private Map<Integer, CubeFace> getCubeFaceMap() {
      return Cube.cubeFaceMap;
    }

    private static Cube parseCube(List<String> lines) {
      int rowCount = lines.size();
      Set<Position> blockedPositions = new HashSet<>();

      for (int row = 0; row < rowCount; row++) {
        String line = lines.get(row);
        int column = 0;
        while (column < line.length()) {
          if (line.charAt(column) == WALL) {
            blockedPositions.add(new Position(row, column));
          }
          column++;
        }
      }

      return new Cube(blockedPositions);
    }

  }

  private record CubeFace(int id, int size, Position topLeft, Transition up, Transition right, Transition down, Transition left) {

    int minColumn() {
      return topLeft.column;
    }

    int maxColumn() {
      return topLeft.column + size - 1;
    }

    int minRow() {
      return topLeft.row;
    }

    int maxRow() {
      return topLeft.row + size - 1;
    }

    boolean contains(Position position) {
      return position.row >= minRow() && position.row <= maxRow() &&
          position.column >= minColumn() && position.column <= maxColumn();
    }

    Transition getTransitionForDirection(Direction direction) {
      return switch (direction) {
        case RIGHT -> right;
        case DOWN -> down;
        case LEFT -> left;
        case UP -> up;
      };
    }

    private static class CubeFaceBuilder {
      private int id;
      private int size;
      private Position topLeft;
      private Transition up;
      private Transition right;
      private Transition down;
      private Transition left;

      private CubeFaceBuilder setId(int id) {
        this.id = id;
        return this;
      }

      private CubeFaceBuilder setSize(int size) {
        this.size = size;
        return this;
      }

      private CubeFaceBuilder setTopLeft(Position topLeft) {
        this.topLeft = topLeft;
        return this;
      }

      private CubeFaceBuilder setUp(Transition up) {
        this.up = up;
        return this;
      }

      private CubeFaceBuilder setDown(Transition down) {
        this.down = down;
        return this;
      }

      private CubeFaceBuilder setLeft(Transition left) {
        this.left = left;
        return this;
      }

      private CubeFaceBuilder setRight(Transition right) {
        this.right = right;
        return this;
      }

      private CubeFace build() {
        return new CubeFace(id, size, topLeft, up, right, down, left);
      }
    }
  }
}
