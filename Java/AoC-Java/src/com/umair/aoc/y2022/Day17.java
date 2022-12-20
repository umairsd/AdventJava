package com.umair.aoc.y2022;

import com.umair.aoc.common.Day;

import java.util.*;
import java.util.stream.IntStream;

/**
 * Day 17: Pyroclastic Flow
 * <a href="https://adventofcode.com/2022/day/17">2022, Day-17</a>
 */
public class Day17 extends Day {

  private static final int CAVE_MIN_X = 0;
  private static final int CAVE_MAX_X = 6;
  private static final int ROCK_COUNT = 5;

  public Day17() {
    super(17, 2022);
  }

  @Override
  protected String part1(List<String> lines) {
    List<JetDirection> jetDirections = parseJetDirections(lines);
    Set<RockCoord> cave = new HashSet<>();

    int time = 0;
    int maxCaveY;
    for (int rockNum = 0; rockNum < 2022; rockNum++) {
      maxCaveY = cave.stream().map(RockCoord::y).max(Integer::compare).orElse(0);

      Rock rock = generateRock(rockNum % ROCK_COUNT, maxCaveY + 4);
      boolean canMove = true;
      while (canMove) {
        JetDirection jet = jetDirections.get(time % jetDirections.size());

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

      // Once the rock stops moving, add the new position of the rock to the cave.
      cave.addAll(rock.coordinates);
    }

    int result = cave.stream().map(RockCoord::y).max(Integer::compare).orElseThrow();
    return Integer.toString(result);
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

  /**
   * Applies the jet, and moves the rock if possible. If not possible to move the rock to the new
   * position, returns .empty().
   */
  private static Optional<Rock> maybeApplyJet(
      Rock rock,
      JetDirection jetDirection,
      Set<RockCoord> cave
  ) {
    Rock movedRock = null;

    switch (jetDirection) {
      case LEFT -> {
        // Generate rock if the new position is within the boundary of the cave.
        if (rock.coordinates.stream().allMatch(c -> c.x > CAVE_MIN_X)) {
          movedRock = new Rock(rock.coordinates.stream()
              .map(c -> new RockCoord(c.x - 1, c.y))
              .toList());
        }
      }
      case RIGHT -> {
        // Generate rock if the new position is within the boundary of the cave.
        if (rock.coordinates.stream().allMatch(c -> c.x < CAVE_MAX_X)) {
          movedRock = new Rock(rock.coordinates.stream()
              .map(c -> new RockCoord(c.x + 1, c.y))
              .toList());
        }
      }
    }

    return movedRock != null && movedRock.coordinates.stream().noneMatch(cave::contains)
        ? Optional.of(movedRock)
        : Optional.empty();
  }

  /**
   * If the rock can fall down by one, generates the new position of the rock and has it fall.
   * Otherwise, returns .empty(), indicating that the rock cannot fall anymore.
   */
  private static Optional<Rock> maybeFall(Rock rock, Set<RockCoord> cave) {
    Rock movedRock = new Rock(rock.coordinates.stream()
        .map(c -> new RockCoord(c.x, c.y - 1))
        .toList());

    return movedRock.coordinates.stream().allMatch(c -> !cave.contains(c) && c.y >= 1)
        ? Optional.of(movedRock)
        : Optional.empty();
  }


  /**
   * Each rock appears so that its left edge is two units away from the left wall and its bottom
   * edge is three units above the highest rock in the room. This
   *
   * @param step    The current step of the simulation.
   * @param bottomY The bottomY coordinate of the rock.
   * @return Rock for the given step.
   */
  private static Rock generateRock(int step, int bottomY) {
    int x = 2;
    // Horizontal Line
    Rock r0 = new Rock(IntStream.range(2, 6).mapToObj(i -> new RockCoord(i, bottomY)).toList());

    // Plus sign
    Rock r1 = new Rock(List.of(
        new RockCoord(x + 1, bottomY + 2),
        new RockCoord(x, bottomY + 1),
        new RockCoord(x + 1, bottomY + 1),
        new RockCoord(x + 2, bottomY + 1),
        new RockCoord(x + 1, bottomY)
    ));

    // Reverse L.
    Rock r2 = new Rock(List.of(
        new RockCoord(x + 2, bottomY + 2),
        new RockCoord(x + 2, bottomY + 1),
        new RockCoord(x, bottomY), new RockCoord(x + 1, bottomY), new RockCoord(x + 2, bottomY)
    ));

    // Vertical Line
    Rock r3 = new Rock(IntStream.range(0, 4).mapToObj(i -> new RockCoord(x, i + bottomY)).toList());

    // Square
    Rock r4 = new Rock(List.of(
        new RockCoord(x, bottomY + 1), new RockCoord(x + 1, bottomY + 1),
        new RockCoord(x, bottomY), new RockCoord(x + 1, bottomY)
    ));

    return switch (step % ROCK_COUNT) {
      case 0 -> r0;
      case 1 -> r1;
      case 2 -> r2;
      case 3 -> r3;
      case 4 -> r4;
      default -> throw new IllegalStateException("Bad value for step: " + step);
    };
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

  private record Rock(List<RockCoord> coordinates) {
  }

  private record RockCoord(int x, int y) {
  }

  @SuppressWarnings("unused")
  private static String debugDave(Set<RockCoord> cave) {
    StringBuilder sb = new StringBuilder();
    int maxRow = cave.stream().map(RockCoord::y).max(Integer::compareTo).orElse(0);

    for (int row = maxRow + 3; row > 0; row--) {
      for (int col = 0; col < 7; col++) {
        RockCoord c = new RockCoord(col, row);
        if (cave.contains(c)) {
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
