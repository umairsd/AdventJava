package com.umair.aoc.y2022;

import com.umair.aoc.common.Day;

import java.util.*;
import java.util.stream.Collectors;

public class Day23 extends Day {

  public Day23() {
    super(23, 2022);
  }

  @Override
  protected String part1(List<String> lines) {
    List<Elf> elves = parseElves(lines);

    Set<Position> occupiedPositions = elves.stream()
        .map(e -> e.currentPosition)
        .collect(Collectors.toSet());
    List<Direction> movementOrder = new LinkedList<>(List.of(
        Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST));

    int round = 0;
    while (round++ < 10) {
      // System.out.println("-- Starting round: " + round + "\n" + debugElves(elves));
      if (!executeOneRound(elves, occupiedPositions, movementOrder)) {
        break;
      }

      // Update the preferred direction of movement.
      var d = movementOrder.remove(0);
      movementOrder.add(d);
      // Update occupied positions.
      occupiedPositions = elves.stream().map(e -> e.currentPosition).collect(Collectors.toSet());
    }


    int minRow = elves.stream().mapToInt(e -> e.currentPosition.row).min().orElseThrow();
    int maxRow = elves.stream().mapToInt(e -> e.currentPosition.row).max().orElseThrow();
    int minColumn = elves.stream().mapToInt(e -> e.currentPosition.column).min().orElseThrow();
    int maxColumn = elves.stream().mapToInt(e -> e.currentPosition.column).max().orElseThrow();

    int area = (maxRow - minRow + 1) * (maxColumn - minColumn + 1);
    int emptyTileCount = area - elves.size();

    return Integer.toString(emptyTileCount);
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

  /**
   * Return value indicates if any elves moved in the round.
   */
  private static boolean executeOneRound(
      List<Elf> elves,
      Set<Position> occupiedPositions,
      List<Direction> movementOrder
  ) {
    // The set of elves that will move during a round.
    Set<Elf> elvesToMove = new HashSet<>();
    // The map of proposed move positions, to the number of elves that made that proposal.
    Map<Position, Integer> proposalsCount = new HashMap<>();


    // First half: Each elf proposes positions.
    for (Elf elf : elves) {
      if (!elf.shouldMove(occupiedPositions)) {
        continue;
      }

      // Attempt to propose a move position, if possible.
      var proposedPosition = elf.possibleMove(occupiedPositions, movementOrder);
      if (proposedPosition.isPresent()) {
        elvesToMove.add(elf);
        elf.proposedPosition = proposedPosition.get();
        int currentCount = proposalsCount.getOrDefault(elf.proposedPosition, 0);
        proposalsCount.put(elf.proposedPosition, currentCount + 1);
      }
    }

    if (elvesToMove.size() == 0) {
      // No elves will move in this round, so end the round.
      return false;
    }

    // Second half: Move the elves that need to move.
    for (Elf elf : elvesToMove) {
      // If this was the only elf that proposed moving to this position, make the move!
      if (proposalsCount.get(elf.proposedPosition) == 1) {
        elf.setCurrentPosition(elf.proposedPosition); // Updates neighbors as well.
      }
      elf.proposedPosition = null;
    }

    return true;
  }

  private enum Direction {
    NORTH,
    NORTH_EAST,
    NORTH_WEST,
    SOUTH,
    SOUTH_EAST,
    SOUTH_WEST,
    WEST,
    EAST
  }

  private static List<Elf> parseElves(List<String> lines) {
    List<Elf> elves = new ArrayList<>();

    for (int row = 0; row < lines.size(); row++) {
      String line = lines.get(row);
      for (int column = 0; column < line.length(); column++) {
        if (line.charAt(column) == '#') {
          elves.add(new Elf(new Position(row, column)));
        }
      }
    }
    return elves;
  }

  private static class Elf {
    private Position currentPosition;
    private Position proposedPosition;
    private Map<Direction, Position> neighborsMap = Map.of();

    Elf(Position p) {
      this.currentPosition = p;
      updateNeighbors();
    }

    private void setCurrentPosition(Position p) {
      this.currentPosition = p;
      updateNeighbors();
    }

    private void updateNeighbors() {
      var p = this.currentPosition;
      neighborsMap = Map.of(
          Direction.NORTH_WEST, new Position(p.row - 1, p.column - 1),
          Direction.NORTH, new Position(p.row - 1, p.column),
          Direction.NORTH_EAST, new Position(p.row - 1, p.column + 1),

          Direction.WEST, new Position(p.row, p.column - 1),
          Direction.EAST, new Position(p.row, p.column + 1),

          Direction.SOUTH_WEST, new Position(p.row + 1, p.column - 1),
          Direction.SOUTH, new Position(p.row + 1, p.column),
          Direction.SOUTH_EAST, new Position(p.row + 1, p.column + 1)
      );
    }

    private boolean shouldMove(Set<Position> occupiedPositions) {
      for (Position n : neighborsMap.values()) {
        if (occupiedPositions.contains(n)) {
          return true;
        }
      }
      return false;
    }

    private Optional<Position> possibleMove(
        Set<Position> occupiedPositions,
        List<Direction> movementOrder) {

      for (Direction d : movementOrder) {
        if (d == Direction.NORTH && isNorthEmpty(occupiedPositions, this)) {
          return Optional.of(neighborsMap.get(Direction.NORTH));

        } else if (d == Direction.SOUTH && isSouthEmpty(occupiedPositions, this)) {
          return Optional.of(neighborsMap.get(Direction.SOUTH));

        } else if (d == Direction.WEST && isWestEmpty(occupiedPositions, this)) {
          return Optional.of(neighborsMap.get(Direction.WEST));

        } else if (d == Direction.EAST && isEastEmpty(occupiedPositions, this)) {
          return Optional.of(neighborsMap.get(Direction.EAST));
        }
      }

      return Optional.empty();
    }

    private static boolean isNorthEmpty(Set<Position> occupiedPositions, Elf elf) {
      boolean hasNorth = occupiedPositions.contains(elf.neighborsMap.get(Direction.NORTH));
      boolean hasNorthEast = occupiedPositions.contains(elf.neighborsMap.get(Direction.NORTH_EAST));
      boolean hasNorthWest = occupiedPositions.contains(elf.neighborsMap.get(Direction.NORTH_WEST));
      return !hasNorthEast && !hasNorth && !hasNorthWest;
    }
    private static boolean isSouthEmpty(Set<Position> occupiedPositions, Elf elf) {
      boolean hasSouth = occupiedPositions.contains(elf.neighborsMap.get(Direction.SOUTH));
      boolean hasSouthEast = occupiedPositions.contains(elf.neighborsMap.get(Direction.SOUTH_EAST));
      boolean hasSouthWest = occupiedPositions.contains(elf.neighborsMap.get(Direction.SOUTH_WEST));
      return !hasSouthEast && !hasSouth && !hasSouthWest;
    }
    private static boolean isEastEmpty(Set<Position> occupiedPositions, Elf elf) {
      boolean hasEast = occupiedPositions.contains(elf.neighborsMap.get(Direction.EAST));
      boolean hasNorthEast = occupiedPositions.contains(elf.neighborsMap.get(Direction.NORTH_EAST));
      boolean hasSouthEast = occupiedPositions.contains(elf.neighborsMap.get(Direction.SOUTH_EAST));
      return !hasNorthEast && !hasEast && !hasSouthEast;
    }
    private static boolean isWestEmpty(Set<Position> occupiedPositions, Elf elf) {
      boolean hasWest = occupiedPositions.contains(elf.neighborsMap.get(Direction.WEST));
      boolean hasNorthWest = occupiedPositions.contains(elf.neighborsMap.get(Direction.NORTH_WEST));
      boolean hasSouthWest = occupiedPositions.contains(elf.neighborsMap.get(Direction.SOUTH_WEST));
      return !hasNorthWest && !hasWest && !hasSouthWest;
    }
  }

  private record Position(int row, int column) {}

  private static String debugElves(List<Elf> elves) {
    Set<Position> occupiedPositions = elves.stream()
        .map(e -> e.currentPosition)
        .collect(Collectors.toSet());
    StringBuilder sb = new StringBuilder();

    int minRow = elves.stream().mapToInt(e -> e.currentPosition.row).min().orElseThrow();
    int maxRow = elves.stream().mapToInt(e -> e.currentPosition.row).max().orElseThrow();
    int minColumn = elves.stream().mapToInt(e -> e.currentPosition.column).min().orElseThrow();
    int maxColumn = elves.stream().mapToInt(e -> e.currentPosition.column).max().orElseThrow();

    for (int row = minRow; row <= maxRow; row++) {
      for (int column = minColumn; column <= maxColumn; column++) {
        var p = new Position(row, column);
        if (occupiedPositions.contains(p)) {
          sb.append("#");
        } else {
          sb.append(".");
        }
      }
      sb.append("\n");
    }
    sb.append("\n");
    return sb.toString();
  }
}
