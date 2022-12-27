package com.umair.aoc.y2022;

import com.umair.aoc.common.Day;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Day 24: Blizzard Basin
 * <a href="https://adventofcode.com/2022/day/24">2022, Day-24</a>
 */
public class Day24 extends Day {

  public Day24() {
    super(24, 2022);
  }

  @Override
  protected String part1(List<String> lines) {
    MapState mapState = parseMapState(lines);

    int steps = bfs(mapState.start, mapState.end, mapState, 0);
    return Integer.toString(steps);
  }

  private static int bfs(
      Point start,
      Point end,
      MapState initial,
      int stepsSoFar
  ) {

    // A map between the current step number, and the corresponding map state.
    Map<Integer, MapState> stepsToMapState = new HashMap<>();
    stepsToMapState.put(stepsSoFar, initial);
    // Queue to maintain BFS traversal.
    Queue<Step> queue = new ArrayDeque<>();
    queue.add(new Step(0, start));
    // To mark Steps that we have seen and processed.
    Set<Step> seen = new HashSet<>();

    while (!queue.isEmpty()) {
      Step currentStep = queue.poll();
      if (seen.contains(currentStep)) {
        continue;
      }
      if (currentStep.location.equals(end)) {
        return currentStep.stepCount;
      }
      // We've processed the current step. Add it to the set of seen steps.
      seen.add(currentStep);

      int nextStepCount = currentStep.stepCount + 1;
      if (!stepsToMapState.containsKey(nextStepCount)) {
        // If the map does not contain a mapping for the next state, generate one from the current
        // state.
        MapState currentState = stepsToMapState.get(currentStep.stepCount);
        stepsToMapState.put(nextStepCount, currentState.getNextState());
      }
      MapState nextMapState = stepsToMapState.get(nextStepCount);

      // How many neighboring positions will be open for the next state?
      List<Point> openNeighbors = nextMapState.openNeighborsOfPoint(currentStep.location);
      if (openNeighbors.size() == 0) {
        // There are no open neighbors. Just wait.
        queue.add(new Step(nextStepCount, currentStep.location));

      } else {
        for (Point neighbor : openNeighbors) {
          queue.add(new Step(nextStepCount, neighbor));
        }
      }
    }

    // If we never reach the end, something has gone wrong.
    throw new IllegalStateException("We never reached the end location.");
  }

  private record Step(int stepCount, Point location) {}

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

  private static MapState parseMapState(List<String> lines) {
    int rowCount = lines.size();
    int columnCount = lines.get(rowCount - 1).length();

    Point bottomRight = new Point(rowCount - 1, columnCount - 1);
    Point start = new Point(0, lines.get(0).indexOf('.'));
    Point end = new Point(lines.size() - 1, lines.get(lines.size() - 1).indexOf('.'));

    Set<Blizzard> blizzards = new HashSet<>();
    for (int row = 0; row < rowCount; row++) {
      String line = lines.get(row);
      for (int column = 0; column < columnCount; column++) {
        var direction = directionFromChar(line.charAt(column));
        if (direction.isPresent()) {
          Point p = new Point(row, column);
          blizzards.add(new Blizzard(p, direction.get()));
        }
      }
    }

    var mapState = new MapState(start, end, bottomRight, blizzards);
    return mapState;
  }

  private static Optional<Direction> directionFromChar(char c) {
    return switch (c) {
      case '^' -> Optional.of(Direction.NORTH);
      case 'v' -> Optional.of(Direction.SOUTH);
      case '>' -> Optional.of(Direction.EAST);
      case '<' -> Optional.of(Direction.WEST);
      default -> Optional.empty();
    };
  }

  private static class MapState {
    private final Point start;
    private final Point end;
    private final Point bottomRight;
    private final Set<Blizzard> blizzards;

    MapState(Point start, Point end, Point bottomRight, Set<Blizzard> blizzards) {
      this.start = start;
      this.end = end;
      this.bottomRight = bottomRight;
      this.blizzards = blizzards;
    }

    private int getNextRowForBlizzard(Blizzard b) {
      int row = switch (b.direction) {
        case NORTH -> {
          int newRow = b.position.row - 1;
          if (newRow == 0) {
            newRow = bottomRight.row - 1;
          }
          yield newRow;
        }

        case SOUTH -> {
          int newRow = b.position.row + 1;
          if (newRow == bottomRight.row) {
            newRow = 1;
          }
          yield newRow;
        }

        case EAST, WEST -> b.position.row;
      };
      return row;
    }

    private int getNextColumnForBlizzard(Blizzard b) {
      int column = switch (b.direction) {
        case NORTH, SOUTH -> b.position.column;

        case EAST -> {
          int newColumn = b.position.column + 1;
          if (newColumn == bottomRight.column) {
            newColumn = 1;
          }
          yield newColumn;
        }

        case WEST -> {
          int newColumn = b.position.column - 1;
          if (newColumn == 0) {
            newColumn = bottomRight.column - 1;
          }
          yield newColumn;
        }
      };
      return column;
    }

    /**
     * Given the current state, generates the next iteration of the map.
     */
    private MapState getNextState() {
      Set<Blizzard> updatedBlizzards = blizzardsAfterOneStep(this);
      MapState nextState = new MapState(this.start, this.end, this.bottomRight, updatedBlizzards);
      return nextState;
    }

    /**
     * For the given point, find the list of points that are open (i.e. can be moved to).
     */
    private List<Point> openNeighborsOfPoint(Point point) {
      List<Point> neighbors = List.of(
          new Point(point.row, point.column + 1),
          new Point(point.row, point.column - 1),
          new Point(point.row + 1, point.column),
          new Point(point.row - 1, point.column)
      );

      Set<Point> blizzardLocations = blizzards.stream()
          .map(Blizzard::position)
          .collect(Collectors.toSet());

      List<Point> filteredNeighbors = new ArrayList<>();
      for (Point p : neighbors) {
        if (p.equals(end) || (isInBounds(p) && !blizzardLocations.contains(p))) {
          filteredNeighbors.add(p);
        }
      }
      return filteredNeighbors;
    }

    /**
     * Whether the current point is within the bounds of this map.
     */
    private boolean isInBounds(Point p) {
      return p.row > 0 &&
          p.row < bottomRight.row &&
          p.column > 0 &&
          p.column < bottomRight.column;
    }

    /**
     * Move each blizzard by one, and generate a new set of blizzards.
     */
    private static Set<Blizzard> blizzardsAfterOneStep(MapState mapState) {
      Set<Blizzard> movedBlizzards = mapState.blizzards.stream()
          .map(b -> new Blizzard(
              new Point(mapState.getNextRowForBlizzard(b), mapState.getNextColumnForBlizzard(b)),
              b.direction))
          .collect(Collectors.toSet());
      return movedBlizzards;
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();

      Map<Point, List<Blizzard>> blizzardLocations = new HashMap<>();
      for (Blizzard b : blizzards) {
        var currentBlizzards = blizzardLocations.getOrDefault(b.position, new ArrayList<>());
        currentBlizzards.add(b);
        blizzardLocations.put(b.position, currentBlizzards);
      }

      for (int row = 0; row <= bottomRight.row; row++) {
        for (int column = 0; column <= bottomRight.column; column++) {
          Point p = new Point(row, column);

          if (p.equals(start) || p.equals(end)) {
            sb.append('.');
          } else if (row == 0 || row == bottomRight.row ||
              column == 0 || column == bottomRight.column) {
            sb.append('#');
          } else {
            if (blizzardLocations.containsKey(p)) {
              List<Blizzard> blizzards = blizzardLocations.get(p);
              if (blizzards.size() == 1) {
                char c = switch(blizzards.get(0).direction) {
                  case NORTH -> '^';
                  case SOUTH -> 'v';
                  case EAST -> '>';
                  case WEST -> '<';
                };
                sb.append(c);
              } else {
                sb.append(blizzards.size());
              }
            } else {
              sb.append('.');
            }
          }
        }
        sb.append("\n");
      }
      return sb.toString();
    }
  }

  private enum Direction {
    NORTH,
    SOUTH,
    EAST,
    WEST
  }

  private record Blizzard(Point position, Direction direction) {
  }

  private record Point(int row, int column) {
  }
}
