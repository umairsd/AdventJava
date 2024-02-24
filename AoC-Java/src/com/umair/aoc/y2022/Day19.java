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

  private static final int TOTAL_ROUNDS_PART1 = 24;
  private static final int TOTAL_ROUNDS_PART2 = 32;

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

      int geodes = findMostGeodes(blueprint, TOTAL_ROUNDS_PART1 + 1, initialState);
      qualityLevel += blueprint.id * geodes;
    }

    return Integer.toString(qualityLevel);
  }

  @Override
  protected String part2(List<String> lines) {
    List<Blueprint> blueprints = lines.stream().map(Day19::parseBlueprint).limit(3).toList();
    int result = 1;

    for (Blueprint blueprint : blueprints) {
      // Start with enough ore to build an ore-robot, to bootstrap the entire operation.
      // This requires that we add an extra turn.
      ProductionState initialState = new ProductionState();
      initialState.ore = blueprint.oreSpec.oreCost;
      initialState.robotTypeToConstruct = RobotType.ORE;

      int geodes = findMostGeodes(blueprint, TOTAL_ROUNDS_PART2 + 1, initialState);
      result *= geodes;
    }

    return Integer.toString(result);
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
          if (state.canBuildRobot(blueprint.oreSpec)) {
            state.useResourcesForBuildingRobot(blueprint.oreSpec);
            isRobotConstructed = true;
          }
        }
        case CLAY -> {
          if (state.canBuildRobot(blueprint.claySpec)) {
            state.useResourcesForBuildingRobot(blueprint.claySpec);
            isRobotConstructed = true;
          }
        }
        case OBSIDIAN -> {
          if (state.canBuildRobot(blueprint.obsidianSpec)) {
            state.useResourcesForBuildingRobot(blueprint.obsidianSpec);
            isRobotConstructed = true;
          }
        }
        case GEODE -> {
          if (state.canBuildRobot(blueprint.geodeSpec)) {
            state.useResourcesForBuildingRobot(blueprint.geodeSpec);
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
        var possibleNextState = state.generateNextState(blueprint, nextRobot);
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

    int id = Integer.parseInt(m.group(1));
    RobotSpec oreSpec = new RobotSpec(RobotType.ORE,
        Integer.parseInt(m.group(2)),   // Ore
        0,                              // Clay
        0);                             // Obsidian

    RobotSpec claySpec = new RobotSpec(RobotType.CLAY,
        Integer.parseInt(m.group(3)),   // Ore
        0,                              // Clay
        0);                             // Obsidian

    RobotSpec obsidianSpec = new RobotSpec(RobotType.OBSIDIAN,
        Integer.parseInt(m.group(4)),   // Ore
        Integer.parseInt(m.group(5)),   // Clay
        0);                             // Obsidian

    RobotSpec geodeSpec = new RobotSpec(RobotType.GEODE,
        Integer.parseInt(m.group(6)),   // Ore
        0,                              // Clay
        Integer.parseInt(m.group(7)));  // Obsidian

    int maxOrePerTurn = Math.max(oreSpec.oreCost, Math.max(claySpec.oreCost, obsidianSpec.oreCost));

    Blueprint blueprint = new Blueprint(
        id,
        oreSpec,
        claySpec,
        obsidianSpec,
        geodeSpec,
        maxOrePerTurn,
        obsidianSpec.clayCost,
        geodeSpec.obsidianCost);
    return blueprint;
  }

  private record Blueprint(
      int id,
      RobotSpec oreSpec,
      RobotSpec claySpec,
      RobotSpec obsidianSpec,
      RobotSpec geodeSpec,
      int maxOrePerTurn,
      int maxClayPerTurn,
      int maxObsidianPerTurn) {
    @Override
    public String toString() {
      return "{id=" + id +
          ", " + oreSpec +
          ", " + claySpec +
          ", " + obsidianSpec +
          ", " + geodeSpec +
          ", Limits=(" + maxOrePerTurn + ", " + maxClayPerTurn + ", " + maxObsidianPerTurn + ")" +
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


    private boolean canBuildRobot(RobotSpec robotSpec) {
      return this.ore >= robotSpec.oreCost &&
          this.clay >= robotSpec.clayCost &&
          this.obsidian >= robotSpec.obsidianCost;
    }

    private void useResourcesForBuildingRobot(RobotSpec robotSpec) {
      this.ore -= robotSpec.oreCost;
      this.clay -= robotSpec.clayCost;
      this.obsidian -= robotSpec.obsidianCost;
    }

    private Optional<ProductionState> generateNextState(Blueprint blueprint, RobotType nextRobot) {
      // We can build any of the 4 robots as a next state. Let's evaluate which paths are not
      // viable.

      if (nextRobot == RobotType.OBSIDIAN && this.clayRobots == 0) {
        // No clay robots, so can't build any obsidian robots.
        return Optional.empty();
      }
      if (nextRobot == RobotType.GEODE && this.obsidianRobots == 0) {
        // No obsidian robots, so can't build any geode robots
        return Optional.empty();
      }

      // Don't build more robots for a resource than can be consumed.
      if ((nextRobot == RobotType.ORE && this.oreRobots == blueprint.maxOrePerTurn) ||
          (nextRobot == RobotType.CLAY && this.clayRobots == blueprint.maxClayPerTurn) ||
          (nextRobot == RobotType.OBSIDIAN && this.obsidianRobots == blueprint.maxObsidianPerTurn)
      ) {
        return Optional.empty();
      }

      ProductionState nextState = new ProductionState(this);
      nextState.robotTypeToConstruct = nextRobot;
      return Optional.of(nextState);
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
}
