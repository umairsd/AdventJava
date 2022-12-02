package com.umair.aoc.y2022;

import com.umair.aoc.common.Day;
import java.util.List;

public class Day02 extends Day {

  public Day02() {
    super(2, 2022);
  }

  @Override
  protected String part1(List<String> lines) {
    List<SpecificMoveStrategy> gameRounds = parseLinesToSpecificMoveStrategies(lines);
    long totalScore = gameRounds
        .stream()
        .map(SpecificMoveStrategy::scoreForHuman)
        .reduce(0L, Long::sum);

    return Long.toString(totalScore);
  }

  @Override
  protected String part2(List<String> lines) {
    List<DesiredOutcomeStrategy> gameRounds = parseLinesToDesiredOutcomeStrategies(lines);
    long totalScore = gameRounds
        .stream()
        .map(DesiredOutcomeStrategy::score)
        .reduce(0L, Long::sum);

    return Long.toString(totalScore);
  }

  @Override
  protected String part1Filename() {
    return filenameFromDataFileNumber(2);
  }

  @Override
  protected String part2Filename() {
    return filenameFromDataFileNumber(2);
  }

  private static List<SpecificMoveStrategy> parseLinesToSpecificMoveStrategies(List<String> lines) {
    List<SpecificMoveStrategy> strategies = lines
        .stream()
        .map(line -> {
          String[] tokens = line.strip().split(" ");
          assert (tokens.length == 2);

          GameMove elfMove = GameMove.parseString(tokens[0]);
          GameMove ourMove = GameMove.parseString(tokens[1]);
          SpecificMoveStrategy strategy = new SpecificMoveStrategy(elfMove, ourMove);
          return strategy;
        })
        .toList();
    return strategies;
  }

  private static List<DesiredOutcomeStrategy> parseLinesToDesiredOutcomeStrategies(
      List<String> lines) {
    List<DesiredOutcomeStrategy> strategies = lines
        .stream()
        .map(line -> {
          String[] tokens = line.strip().split(" ");
          assert (tokens.length == 2);

          GameMove elfMove = GameMove.parseString(tokens[0]);
          GameOutcome outcome = GameOutcome.parseString(tokens[1]);
          DesiredOutcomeStrategy strategy = new DesiredOutcomeStrategy(elfMove, outcome);
          return strategy;
        })
        .toList();
    return strategies;
  }

  /** Represents the strategy of getting to the desired outcome as specified in the guide. */
  private record DesiredOutcomeStrategy(GameMove elfMove, GameOutcome desiredOutcome) {
    long score() {
      return switch (desiredOutcome) {
        case WIN -> desiredOutcome.points + elfMove.winningCountermove().points;
        case LOSS -> desiredOutcome.points + elfMove.losingCountermove().points;
        case DRAW -> desiredOutcome.points + elfMove.points;
      };
    }
  }

  /** Represents the strategy of playing the exact move specified in the guide. */
  private record SpecificMoveStrategy(GameMove elfMove, GameMove humanMove) {
    long scoreForHuman() {
      GameOutcome outcome;
      if (humanMove == elfMove) {
        outcome = GameOutcome.DRAW;
      } else if (elfMove.winningCountermove() == humanMove) {
        outcome = GameOutcome.WIN;
      } else {
        outcome = GameOutcome.LOSS;
      }

      return outcome.points + humanMove.points;
    }
  }

  private enum GameOutcome {
    WIN(6),
    DRAW(3),
    LOSS(0);

    private final int points;

    GameOutcome(int points) {
      this.points = points;
    }

    static GameOutcome parseString(String input) {
      return switch (input) {
        case "X" -> LOSS;
        case "Y" -> DRAW;
        case "Z" -> WIN;
        default -> throw new IllegalStateException("Unexpected value: " + input);
      };
    }
  }

  private enum GameMove {
    ROCK(1),
    PAPER(2),
    SCISSORS(3);

    private final long points;

    GameMove(long points) {
      this.points = points;
    }

    GameMove winningCountermove() {
      return switch (this) {
        case ROCK -> PAPER;
        case PAPER -> SCISSORS;
        case SCISSORS -> ROCK;
      };
    }

    GameMove losingCountermove() {
      return switch (this) {
        case ROCK -> SCISSORS;
        case PAPER -> ROCK;
        case SCISSORS -> PAPER;
      };
    }

    static GameMove parseString(String input) {
      return switch (input) {
        case "A", "X" -> ROCK;
        case "B", "Y" -> PAPER;
        case "C", "Z" -> SCISSORS;
        default -> throw new IllegalStateException("Unexpected value: " + input);
      };
    }
  }
}
