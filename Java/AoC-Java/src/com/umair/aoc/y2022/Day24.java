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
    MapState mapState = parseMap(lines);

    Point expeditionLocation = new Point(0, lines.get(0).indexOf('.'));
    Point end = new Point(lines.size() - 1, lines.get(lines.size() - 1).indexOf('.'));
    Map<Integer, MapState> stepsToMapState = buildStepsToMapState(mapState, 50);

    int steps = bfs(expeditionLocation, end, stepsToMapState);
    return Integer.toString(steps);
  }

  private static Map<Integer, MapState> buildStepsToMapState(MapState mapState, int iterations) {
    Map<Integer, MapState> stepsToMapStateMap = new HashMap<>();
    int step = 0;
    while (step < iterations) {
      System.out.println("Minute " + step + ", ");
      System.out.println(mapState);
      stepsToMapStateMap.put(step, mapState);
      mapState = mapState.getNextState();

      step++;
    }
    return stepsToMapStateMap;
  }

  private static int bfs(
      Point expeditionLocation,
      Point end,
//      MapState mapState,
      Map<Integer, MapState> stepsToMapState
  ) {
    // Queue to maintain BFS traversal.
    Queue<Step> queue = new ArrayDeque<>();
    // To mark Steps that we have seen: i.e. added to the queue.
    Set<Step> seen = new HashSet<>();

    Step step = new Step(0, expeditionLocation);
    queue.add(step);
    seen.add(step);

    while (!queue.isEmpty()) {
      Step currentStep = queue.poll();
      if (seen.contains(currentStep)) {
        continue;
      }
      if (currentStep.location.equals(end)) {
        return currentStep.stepCount;
      }

      int nextStepCount = currentStep.stepCount + 1;
      MapState nextMapState = stepsToMapState.get(nextStepCount);

      // How many neighboring positions will be open for the next state?
      List<Point> openNeighbors = nextMapState.openNeighborsOfPoint(currentStep.location);
      if (openNeighbors.size() == 0) {
        // There are no open neighbors. Just wait.
        Step nextStep = new Step(nextStepCount, currentStep.location);
        queue.add(nextStep);
        seen.add(nextStep);
      } else {
        for (Point neighbor : openNeighbors) {
          queue.add(new Step(nextStepCount, neighbor));
        }
      }
    }

    // If we never reach the end, something has gone wrong.
    throw new IllegalStateException();
  }

  private record Step(int stepCount, Point location) {}

  @Override
  protected String part2(List<String> lines) {
    return null;
  }

  @Override
  protected String part1Filename() {
    return fileNameFromFileNumber(1);
  }

  @Override
  protected String part2Filename() {
    return fileNameFromFileNumber(1);
  }

  private static MapState parseMap(List<String> lines) {
    int rowCount = lines.size();
    int columnCount = lines.get(rowCount - 1).length();
    Point bottomRight = new Point(rowCount - 1, columnCount - 1);
    Point expedition = new Point(0, lines.get(0).indexOf('.'));

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

    var mapState = new MapState(bottomRight, blizzards);
    mapState.expedition = expedition;
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
    Point expedition; // For debugging.
    Point bottomRight;
    Set<Blizzard> blizzards;

    MapState(Point bottomRight, Set<Blizzard> blizzards) {
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
      MapState nextState = new MapState(this.bottomRight, updatedBlizzards);
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

      List<Point> filteredNeighbors = neighbors.stream()
          .filter(p -> p.row > 0 && p.row < bottomRight.row)
          .filter(p -> p.column > 0 && p.column < bottomRight.column)
          .filter(p -> !blizzardLocations.contains(p))
          .toList();

      return filteredNeighbors;
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

          if (p.equals(expedition)) {
            sb.append('E');
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
