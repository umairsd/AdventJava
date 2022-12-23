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
    Cave cave = new Cave();
    long time = 0;
    long maxCaveY = 0;
    List<Long> caveHeightsPerStep = new ArrayList<>();

    for (int rockNum = 0; rockNum < 100_000; rockNum++) {
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
      caveHeightsPerStep.add(maxCaveY);
    }

    // Calculate the height difference of the cave between each rock.
    List<Long> heightDeltas = new ArrayList<>();
    for (int i = 1; i < caveHeightsPerStep.size(); i++) {
      long previous = caveHeightsPerStep.get(i - 1);
      long current = caveHeightsPerStep.get(i);
      heightDeltas.add(current - previous);
    }


    // To detect a loop, use a marker pattern. This marker pattern is made up of height deltas
    // e.g. (-1, 2, 3, 3, 1), that will be used to find other parts of the height differences that
    // follow the same pattern.
    // The constants below are arbitrarily chosen.
    int loopBeginIndex = 2201;
    int markerLength = 53;

    // This is the marker pattern of height deltas that we want to use to find a loop.
    List<Long> markerSegment = heightDeltas.subList(loopBeginIndex, loopBeginIndex + markerLength);

    long loopHeight = -1;
    long loopLength = -1;
    long caveHeightPriorToLoop = caveHeightsPerStep.get(loopBeginIndex - 1);

    for (int i = loopBeginIndex + markerLength; i < heightDeltas.size() - markerLength; i++) {
      var currentSubList = heightDeltas.subList(i, i + markerLength);
      if (markerSegment.equals(currentSubList)) {
        // Marker segment matches the sublist starting at index `i`.
        loopLength = i - loopBeginIndex;
        loopHeight = caveHeightsPerStep.get(i - 1) - caveHeightPriorToLoop;
        break;
      }
    }

    long totalRockCount = 1_000_000_000_000L;
    // Calculate the height at the target based on the number of loops and the height of the loop
    long numFullLoops = (totalRockCount - loopBeginIndex) / loopLength;
    long partialLoopStartIndex =  (totalRockCount - loopBeginIndex) % loopLength;
    long heightOfPartialLoop =
        caveHeightsPerStep.get(loopBeginIndex + (int)partialLoopStartIndex) - caveHeightPriorToLoop;

    long result = caveHeightPriorToLoop + (loopHeight * numFullLoops) + heightOfPartialLoop - 1;
    return Long.toString(result);
  }

  @Override
  protected String part1Filename() {
    return fileNameFromFileNumber(2);
  }

  @Override
  protected String part2Filename() {
    return fileNameFromFileNumber(2);
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

    return movedRock.points.stream().allMatch(c -> !cave.contains(c) && c.y >= 1)
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

  private static class Cave {
    private static final int CAVE_MIN_X = 0;
    private static final int CAVE_MAX_X = 6;

    private final Set<Point> points = new HashSet<>();
    private long height = 0;

    boolean contains(Point p) {
      return points.contains(p);
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

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      long maxRow = points.stream().map(Point::y).max(Long::compareTo).orElse(0L);

      for (long row = maxRow + 3; row > 0; row--) {
        for (long col = CAVE_MIN_X; col < CAVE_MAX_X; col++) {
          Point c = new Point(col, row);
          if (this.contains(c)) {
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
