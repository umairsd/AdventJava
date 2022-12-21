package com.umair.aoc.y2022;

import com.umair.aoc.common.Day;

import java.util.*;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Day 17: Pyroclastic Flow
 * <a href="https://adventofcode.com/2022/day/17">2022, Day-17</a>
 */
public class Day17 extends Day {

  private static final int ROCK_COUNT = 5;

  public Day17() {
    super(17, 2022);
  }

  @Override
  protected String part1(List<String> lines) {
    List<JetDirection> jetDirections = parseJetDirections(lines);
    Cave cave = new Cave();

    long time = 0;
    long maxCaveY = 0;
    for (int rockNum = 0; rockNum < 2022; rockNum++) {
      int rockIndex = rockNum % ROCK_COUNT;
      Rock rock = generateRock(rockIndex, maxCaveY + 4);

      boolean canMove = true;
      while (canMove) {
        JetDirection jet = jetDirections.get( (int)(time % jetDirections.size()));

        var maybeMovedRock = maybeApplyJet(rock, jet, cave);
        if (maybeMovedRock.isPresent()) { // Moved successfully.
          rock = maybeMovedRock.get();
        }

        var maybeLowerRock = maybeFall(rock, cave);
        if (maybeLowerRock.isPresent()) {
          rock = maybeLowerRock.get();
        } else {
          canMove = false;
        }

        time++;
      }

      // Once the rock stops moving, add the rock to the cave.
      cave.addRock(rock);
      maxCaveY = cave.getHeight();
    }

    long result = cave.getHeight();
    return Long.toString(result);
  }

  @Override
  protected String part2(List<String> lines) {
    List<JetDirection> jetDirections = parseJetDirections(lines);
    Map<MapKey, CaveState> checkpointsMap = new HashMap<>();
    Cave cave = new Cave();

    long maxCaveY = 0;
    long rockDropped = 1;
    int topCount = 20;
    int jetIndex = 0;
//    long targetDropCount = 2022;
    long targetDropCount = 1000000000000L;

    while (rockDropped <= targetDropCount) {
      int rockIndex = (int)((rockDropped - 1) % ROCK_COUNT);
      Rock rock = generateRock(rockIndex, maxCaveY + 4);

      boolean canMove = true;
      while (canMove) {
        JetDirection jet = jetDirections.get(jetIndex);

        var maybeMovedRock = maybeApplyJet(rock, jet, cave);
        if (maybeMovedRock.isPresent()) { // Moved successfully.
          rock = maybeMovedRock.get();
        }

        var maybeLowerRock = maybeFall(rock, cave);
        if (maybeLowerRock.isPresent()) {
          rock = maybeLowerRock.get();
        } else {
          canMove = false;
        }

        jetIndex = (jetIndex + 1) % jetDirections.size();
      }

      // Once the rock stops moving, add the new position of the rock to the cave.
      cave.addRock(rock);
      maxCaveY = cave.getHeight();

      var caveTopStr = cave.topNToString(topCount);
      var prevJetIndex = (jetIndex - 1) % jetDirections.size();
      var key = new MapKey(rockIndex, prevJetIndex, caveTopStr);

      if (checkpointsMap.containsKey(key)) {
        CaveState prevState = checkpointsMap.get(key);
        long cycleSize = rockDropped - prevState.droppedRockCount;
        long cycles = (targetDropCount - rockDropped) / cycleSize;
        rockDropped += cycles * cycleSize;

        long rowsToCopy = topCount;
        long startingSrcY = prevState.caveHeight - rowsToCopy;
        long startingDestY = prevState.caveHeight + cycles * cycleSize;
        copyPartOfCave(cave, startingSrcY, rowsToCopy, startingDestY);

        checkpointsMap.clear();
      } else {
        CaveState state = new CaveState(rockDropped, maxCaveY);
        checkpointsMap.put(key, state);
      }

      rockDropped++;
    }

    long result = cave.getHeight();
    return Long.toString(result);
  }

  @Override
  protected String part1Filename() {
    return filenameFromDataFileNumber(2);
  }

  @Override
  protected String part2Filename() {
    return filenameFromDataFileNumber(1);
  }

  /**
   * Applies the jet, and moves the rock if possible. If not possible to move the rock to the new
   * position, returns .empty().
   */
  private static Optional<Rock> maybeApplyJet(Rock rock, JetDirection jetDirection, Cave cave) {
    Rock movedRock = null;

    switch (jetDirection) {
      case LEFT -> {
        // Generate rock if the new position is within the boundary of the cave.
        if (rock.points.stream().allMatch(c -> c.x > cave.getMinX())) {
          movedRock = new Rock(rock.points.stream()
              .map(c -> new Point(c.x - 1, c.y))
              .toList());
        }
      }
      case RIGHT -> {
        // Generate rock if the new position is within the boundary of the cave.
        if (rock.points.stream().allMatch(c -> c.x < cave.getMaxX())) {
          movedRock = new Rock(rock.points.stream()
              .map(c -> new Point(c.x + 1, c.y))
              .toList());
        }
      }
    }

    return movedRock != null && movedRock.points.stream().noneMatch(cave.points::contains)
        ? Optional.of(movedRock)
        : Optional.empty();
  }

  /**
   * If the rock can fall down by one, generates the new position of the rock and has it fall.
   * Otherwise, returns .empty(), indicating that the rock cannot fall anymore.
   */
  private static Optional<Rock> maybeFall(Rock rock, Cave cave) {
    Rock movedRock = new Rock(rock.points.stream()
        .map(c -> new Point(c.x, c.y - 1))
        .toList());

    return movedRock.points.stream().allMatch(c -> !cave.points.contains(c) && c.y >= 1)
        ? Optional.of(movedRock)
        : Optional.empty();
  }

  /**
   * Each rock appears so that its left edge is two units away from the left wall and its bottom
   * edge is three units above the highest rock in the room. This
   *
   * @param rockIndex The current step of the simulation.
   * @param bottomY The bottomY coordinate of the rock.
   * @return Rock for the given step.
   */
  private static Rock generateRock(int rockIndex, long bottomY) {
    int x = 2;
    // Horizontal Line
    Rock r0 = new Rock(IntStream.range(2, 6).mapToObj(i -> new Point(i, bottomY)).toList());

    // Plus sign
    Rock r1 = new Rock(List.of(
        new Point(x + 1, bottomY + 2),
        new Point(x, bottomY + 1),
        new Point(x + 1, bottomY + 1),
        new Point(x + 2, bottomY + 1),
        new Point(x + 1, bottomY)
    ));

    // Reverse L.
    Rock r2 = new Rock(List.of(
        new Point(x + 2, bottomY + 2),
        new Point(x + 2, bottomY + 1),
        new Point(x, bottomY), new Point(x + 1, bottomY), new Point(x + 2, bottomY)
    ));

    // Vertical Line
    Rock r3 = new Rock(IntStream.range(0, 4).mapToObj(i -> new Point(x, i + bottomY)).toList());

    // Square
    Rock r4 = new Rock(List.of(
        new Point(x, bottomY + 1), new Point(x + 1, bottomY + 1),
        new Point(x, bottomY), new Point(x + 1, bottomY)
    ));

    return switch (rockIndex) {
      case 0 -> r0;
      case 1 -> r1;
      case 2 -> r2;
      case 3 -> r3;
      case 4 -> r4;
      default -> throw new IllegalStateException("Bad value for step: " + rockIndex);
    };
  }

  private static void copyPartOfCave(Cave cave, long startingSrcY, long rowsToCopy, long startingDestY) {
    for (long y = startingSrcY; y < (startingSrcY + rowsToCopy); y++) {
      for (long x = cave.getMinX(); x <= cave.getMaxX(); x++) {
        Point p = new Point(x, y);
        if (cave.contains(p)) {
          long newY = p.y + startingDestY;
          cave.addPoint(new Point(p.x, newY));
        }
      }
    }
  }

  private record MapKey(long rockIndex, long jetIndex, String caveTop) {}

  private record CaveState(long droppedRockCount, long caveHeight) {}

  private static class Cave {
    private static final int CAVE_MIN_X = 0;
    private static final int CAVE_MAX_X = 6;

    private final Set<Point> points = new HashSet<>();
    private long height = 0;

    boolean contains(Point p) {
      return points.contains(p);
    }

    void addPoint(Point p) {
      points.add(p);
    }

    void addRock(Rock rock) {
      long maxY = height;
      for (var p : rock.points) {
        maxY = Math.max(maxY, p.y);
        points.add(p);
      }
      height = maxY;
    }

    long getHeight() {
      return height;
    }

    long getMinX() {
      return CAVE_MIN_X;
    }

    long getMaxX() {
      return CAVE_MAX_X;
    }

    String topNToString(long n) {
      StringBuilder sb = new StringBuilder();
      long maxRow = points.stream().map(Point::y).max(Long::compareTo).orElse(0L);

      for (long row = maxRow; row > maxRow - n; row--) {
        for (long col = CAVE_MIN_X; col < CAVE_MAX_X; col++) {
          Point c = new Point(col, row);
          if (points.contains(c)) {
            sb.append("#");
          } else {
            sb.append(".");
          }
        }
        sb.append("\n");
      }
      return sb.toString();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      long maxRow = points.stream().map(Point::y).max(Long::compareTo).orElse(0L);

      for (long row = maxRow + 3; row > 0; row--) {
        for (long col = CAVE_MIN_X; col < CAVE_MAX_X; col++) {
          Point c = new Point(col, row);
          if (points.contains(c)) {
            sb.append("#");
          } else {
            sb.append(".");
          }
        }
        sb.append("\n");
      }
      return sb.toString();
    }
  }

  private static List<JetDirection> parseJetDirections(List<String> lines) {
    List<JetDirection> jetDirections = lines.get(0).chars()
        .mapToObj(i -> (char) i)
        .map(c -> switch (c) {
          case '>' -> JetDirection.RIGHT;
          case '<' -> JetDirection.LEFT;
          default -> throw new IllegalArgumentException();
        })
        .toList();

    return jetDirections;
  }

  private enum JetDirection {
    LEFT,
    RIGHT
  }

  private record Rock(List<Point> points) {}

  private record Point(long x, long y) {}
}
