package com.umair.aoc.y2021;

import com.umair.aoc.common.Day;

import java.util.List;

import static com.umair.aoc.common.Constants.INPUT_EMPTY;

public class Day11 extends Day {

  private static final int MAX_ENERGY_LEVEL = 9;

  public Day11() {
    super(11,2021);
  }

  @Override
  protected String part1(List<String> lines) {
    if (lines.isEmpty()) {
      return INPUT_EMPTY;
    }

    Octopus[][] octopuses = parseInputToOctopuses(lines);
    int totalFlashes = 0;
    for (int step = 0; step < 100; step++) {
      totalFlashes += executeOneStep(octopuses);
    }

    return Integer.toString(totalFlashes);
  }

  @Override
  protected String part2(List<String> lines) {
    if (lines.isEmpty()) {
      return INPUT_EMPTY;
    }

    Octopus[][] octopuses = parseInputToOctopuses(lines);
    int totalOctopusCount = octopuses.length * octopuses[0].length;
    int flashesPerStep;
    int step = 0;
    do {
      step += 1;
      flashesPerStep = executeOneStep(octopuses);
    } while (flashesPerStep != totalOctopusCount);

    return Integer.toString(step);
  }

  private int executeOneStep(Octopus[][] octopuses) {
    int totalFlashes = 0;
    int flashesInOneLoop = 0;

    do {
      totalFlashes += flashesInOneLoop;
      flashesInOneLoop = 0;

      for (Octopus[] row : octopuses) {
        for (Octopus octopus : row) {
          if (octopus.state == StepState.DID_FLASH) {
            // No need to count this one, as this octopus has already flashed, and we've
            // handled its impact (updating neighbors)
            continue;
          }


          if (octopus.state == StepState.NORMAL) {
            // Increment energy by one. Default behavior.
            octopus.energy += 1;
            octopus.state = StepState.DID_INCREMENT;
          }

          if (octopus.energy > MAX_ENERGY_LEVEL) {
            octopus.state = StepState.DID_FLASH;
            flashesInOneLoop += 1;

            for (Octopus neighbor : neighboringOctopuses(octopus, octopuses)) {
              neighbor.energy += 1;
            }
          }
        }
      }

    } while (flashesInOneLoop > 0);


    // Reset state for all octopuses that flashed during this step.
    for (Octopus[] row : octopuses) {
      for (Octopus octopus : row) {
        if (octopus.state == StepState.DID_FLASH) {
          octopus.energy = 0;
        }
        octopus.state = StepState.NORMAL;
      }
    }

    return totalFlashes;
  }

  /**
   * For the given octopus, returns its valid neighbors that have not flashed yet.
   */
  private static List<Octopus> neighboringOctopuses(Octopus octopus, Octopus[][] octopuses) {
    int rowCount = octopuses.length;
    int columnCount = octopuses[0].length;

    List<Coordinate> allPositions = List.of(
        new Coordinate(octopus.position.row - 1, octopus.position.column - 1),
        new Coordinate(octopus.position.row - 1, octopus.position.column),
        new Coordinate(octopus.position.row - 1, octopus.position.column + 1),
        new Coordinate(octopus.position.row, octopus.position.column - 1),
        new Coordinate(octopus.position.row, octopus.position.column + 1),
        new Coordinate(octopus.position.row + 1, octopus.position.column - 1),
        new Coordinate(octopus.position.row + 1, octopus.position.column),
        new Coordinate(octopus.position.row + 1, octopus.position.column + 1)
    );

    List<Octopus> neighbors = allPositions
        .stream()
        .filter(p -> p.row >= 0 && p.row < rowCount && p.column >= 0 && p.column < columnCount)
        .map(p -> octopuses[p.row][p.column])
        .filter(o -> o.state != StepState.DID_FLASH)
        .toList();
    return neighbors;
  }

  private static Octopus[][] parseInputToOctopuses(List<String> lines) {
    Octopus[][] octopuses = new Octopus[lines.size()][lines.get(0).length()];
    for (int r = 0; r < lines.size(); r++) {
      char[] characters = lines.get(r).toCharArray();
      for (int c = 0; c < characters.length; c++) {
        int energy = Character.getNumericValue(characters[c]);
        octopuses[r][c] = new Octopus(energy, new Coordinate(r, c));
      }
    }

    return octopuses;
  }

  private enum StepState {
    NORMAL,
    DID_INCREMENT,
    DID_FLASH
  }

  private static class Octopus {
    private int energy;
    private StepState state = StepState.NORMAL;
    private final Coordinate position;

    Octopus(int energy, Coordinate position) {
      this.energy = energy;
      this.position = position;
    }

    @Override
    public String toString() {
      return "" + position + ", E: " + energy + ", S: " + state.toString();
    }
  }

  private static class Coordinate {
    int row;
    int column;
    Coordinate(int row, int column) {
      this.row = row;
      this.column = column;
    }

    @Override
    public String toString() {
      return "{" + row + "," + column + "}";
    }
  }
}
