package com.umair.aoc.y2022;

import com.umair.aoc.common.Day;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Day 19: Not Enough Minerals
 * <a href="https://adventofcode.com/2022/day/19">2022, Day-19</a>
 */
public class Day19 extends Day {

  public Day19() {
    super(19, 2022);
  }

  @Override
  protected String part1(List<String> lines) {
    List<Blueprint> blueprints = lines.stream().map(Day19::parseBlueprint).toList();
    int qualityLevel = 0;

    for (Blueprint blueprint : blueprints) {
      // Start with enough ore to build an ore-robot, to bootstrap the entire operation.
      // This requires that we add an extra turn.
      ProductionState initialState = new ProductionState();
      initialState.ore = blueprint.oreSpec.oreCost;
      initialState.robotTypeToConstruct = RobotType.ORE;

      int geodes = findMostGeodes(blueprint, 24 + 1, initialState);
      System.out.println("-- Id: " + blueprint.id + ", geodes: " + geodes);
      qualityLevel += blueprint.id * geodes;
    }

    return Integer.toString(qualityLevel);
  }

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

  private static final Pattern linePattern = Pattern.compile("Blueprint (.*): " +
      "Each ore robot costs (.*) ore. " +
      "Each clay robot costs (.*) ore. " +
      "Each obsidian robot costs (.*) ore and (.*) clay. " +
      "Each geode robot costs (.*) ore and (.*) obsidian.");

  private static Blueprint parseBlueprint(String line) {
    Matcher m = linePattern.matcher(line);
    if (!m.matches()) {
      throw new IllegalStateException("Bad input line: " + line);
    }

    String blueprintId = m.group(1);
    RobotSpec oreRobotSpec =
        new RobotSpec(RobotType.ORE, Integer.parseInt(m.group(2)), 0, 0);

    RobotSpec clayRobotSpec =
        new RobotSpec(RobotType.CLAY, 0, Integer.parseInt(m.group(3)), 0);

    RobotSpec obsidianRobotSpec = new RobotSpec(
        RobotType.OBSIDIAN,
        Integer.parseInt(m.group(4)), // Ore
        Integer.parseInt(m.group(5)), // Clay
        0);                           // Obsidian

    RobotSpec geodeRobotSpec = new RobotSpec(
        RobotType.GEODE,
        Integer.parseInt(m.group(6)),   // Ore
        0,                              // Clay
        Integer.parseInt(m.group(7)));  // Obsidian

    Blueprint blueprint = new Blueprint(
        Integer.parseInt(blueprintId),
        oreRobotSpec,
        clayRobotSpec,
        obsidianRobotSpec,
        geodeRobotSpec);

    return blueprint;
  }

  private record Blueprint(int id, RobotSpec oreSpec, RobotSpec claySpec, RobotSpec obsidianSpec, RobotSpec geodeSpec) {

    private int maxOreUsePerTurn() {
      return Math.max(
          Math.max(oreSpec.oreCost, claySpec.oreCost),
          Math.max(obsidianSpec.oreCost, geodeSpec.oreCost));
    }

    private int maxClayUsePerTurn() {
      return Math.max(
          Math.max(oreSpec.clayCost, claySpec.clayCost),
          Math.max(obsidianSpec.clayCost, geodeSpec.clayCost));
    }

    private int maxObsidianUsePerTurn() {
      return Math.max(
          Math.max(oreSpec.obsidianCost, claySpec.obsidianCost),
          Math.max(obsidianSpec.obsidianCost, geodeSpec.obsidianCost));
    }

    @Override
    public String toString() {
      return "{id=" + id +
          ", " + oreSpec +
          ", " + claySpec +
          ", " + obsidianSpec +
          ", " + geodeSpec +
          '}';
    }
  }

  private record RobotSpec(RobotType robotType, int oreCost, int clayCost, int obsidianCost) {
    @Override
    public String toString() {
      String type = switch (robotType) {
        case ORE -> "Ore";
        case CLAY -> "Clay";
        case OBSIDIAN -> "Obsidian";
        case GEODE -> "Geode";
      };
      return "{" + type + " (" + oreCost + ", " + clayCost + ", " + obsidianCost + ")}";
    }
  }

  private enum RobotType {
    ORE,
    CLAY,
    OBSIDIAN,
    GEODE
  }

  private static class ProductionState {
    int ore;
    int clay;
    int obsidian;
    int geode;

    int oreRobots;
    int clayRobots;
    int obsidianRobots;
    int geodeRobots;

    RobotType robotTypeToConstruct;

    ProductionState() {}

    ProductionState(ProductionState other) {
      this.ore = other.ore;
      this.clay = other.clay;
      this.obsidian = other.obsidian;
      this.geode = other.geode;
      this.oreRobots = other.oreRobots;
      this.clayRobots = other.clayRobots;
      this.obsidianRobots = other.obsidianRobots;
      this.geodeRobots = other.geodeRobots;
      this.robotTypeToConstruct = other.robotTypeToConstruct;
    }

    @Override
    public String toString() {
      return "{" +
          "build=" + robotTypeToConstruct + ", " +
          "material=(" + ore + ", " + clay + ", " + obsidian + ", " + geode + "), " +
          "robots=(" + oreRobots + ", " + clayRobots + ", " + obsidianRobots + ", " + geodeRobots +
          ")}";
    }
  }

  private static int findMostGeodes(
      Blueprint blueprint,
      int timeRemaining,
      ProductionState state
  ) {
    boolean isRobotConstructed = false;

    while (!isRobotConstructed && timeRemaining > 0) {
      switch (state.robotTypeToConstruct) {
        case ORE -> {
          if (state.ore >= blueprint.oreSpec.oreCost) {
            state.ore -= blueprint.oreSpec.oreCost;
            isRobotConstructed = true;
          }
        }
        case CLAY -> {
          if (state.clay >= blueprint.claySpec.clayCost) {
            state.clay -= blueprint.claySpec.clayCost;
            isRobotConstructed = true;
          }
        }
        case OBSIDIAN -> {
          if (state.ore >= blueprint.obsidianSpec.oreCost &&
              state.clay >= blueprint.obsidianSpec.clayCost) {
            state.ore -= blueprint.obsidianSpec.oreCost;
            state.clay -= blueprint.obsidianSpec.clayCost;
            isRobotConstructed = true;
          }
        }
        case GEODE -> {
          if (state.ore >= blueprint.geodeSpec.oreCost &&
              state.obsidian >= blueprint.geodeSpec.obsidianCost) {
            state.ore -= blueprint.geodeSpec.oreCost;
            state.obsidian -= blueprint.geodeSpec.obsidianCost;
            isRobotConstructed = true;
          }
        }
      }

      state.ore += state.oreRobots;
      state.clay += state.clayRobots;
      state.obsidian += state.obsidianRobots;
      state.geode += state.geodeRobots;
      timeRemaining--;

      if (isRobotConstructed) {
        switch (state.robotTypeToConstruct) {
          case ORE -> state.oreRobots++;
          case CLAY -> state.clayRobots++;
          case OBSIDIAN -> state.obsidianRobots++;
          case GEODE -> state.geodeRobots++;
        }
      }
    }

    int maxGeodes = state.geode;
    if (timeRemaining > 0) {
      var robots = List.of(RobotType.ORE, RobotType.CLAY, RobotType.OBSIDIAN, RobotType.GEODE);

      for (RobotType nextRobot : robots) {
        var possibleNextState = generateNextState(state, blueprint, nextRobot);
        if (possibleNextState.isEmpty()) {
          continue;
        }

        ProductionState nextState = possibleNextState.get();
        int numGeodes = findMostGeodes(blueprint, timeRemaining, nextState);
        maxGeodes = Math.max(maxGeodes, numGeodes);
      }
    }

    return maxGeodes;
  }

  private static Optional<ProductionState> generateNextState(
      ProductionState state,
      Blueprint blueprint,
      RobotType nextRobot
  ) {
    // We can build any of the 4 robots as a next state. Let's evaluate which paths are not
    // viable.

    if (nextRobot == RobotType.OBSIDIAN && state.clayRobots == 0) {
      // No clay robots, so can't build any obsidian robots.
      return Optional.empty();
    }
    if (nextRobot == RobotType.GEODE && state.obsidianRobots == 0) {
      // No obsidian robots, so can't build any geode robots
      return Optional.empty();
    }

    // Don't build more robots for a resource than can be consumed.
    if ((nextRobot == RobotType.ORE && state.oreRobots == blueprint.maxOreUsePerTurn()) ||
        (nextRobot == RobotType.CLAY && state.clayRobots == blueprint.maxClayUsePerTurn()) ||
        (nextRobot == RobotType.OBSIDIAN &&
            state.obsidianRobots == blueprint.maxObsidianUsePerTurn())
    ) {
      return Optional.empty();
    }

    ProductionState nextState = new ProductionState(state);
    nextState.robotTypeToConstruct = nextRobot;
    return Optional.of(nextState);
  }

  private static boolean canBuildRobot(RobotSpec robotSpec, ProductionState state) {
    return state.ore >= robotSpec.oreCost &&
        state.clay >= robotSpec.clayCost &&
        state.obsidian >= robotSpec.obsidianCost;
  }

  private static void useResourcesForBuildingRobot(RobotSpec robotSpec, ProductionState state) {
    state.ore -= robotSpec.oreCost;
    state.clay -= robotSpec.clayCost;
    state.obsidian -= robotSpec.obsidianCost;
  }
}
