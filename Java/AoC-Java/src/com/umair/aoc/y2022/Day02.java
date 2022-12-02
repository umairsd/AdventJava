package com.umair.aoc.y2022;

import com.umair.aoc.common.Day;

import java.util.List;

public class Day02 extends Day {

  public Day02() {
    super(2, 2022);
  }

  @Override
  protected String part1(List<String> lines) {
    List<GameRound> rounds = parseLinesToGameRounds(lines);
    long totalScore = rounds
        .stream()
        .map(GameRound::scoreForHuman)
        .reduce(0L, Long::sum);

    return Long.toString(totalScore);
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

  private static List<GameRound> parseLinesToGameRounds(List<String> lines) {
    List<GameRound> rounds = lines
        .stream()
        .map(line -> {
          String[] tokens = line.strip().split(" ");
          assert(tokens.length == 2);

          GameMove elfMove = GameMove.stringToMove(tokens[0]);
          GameMove ourMove = GameMove.stringToMove(tokens[1]);
          GameRound round = new GameRound(elfMove, ourMove);
          return round;
        })
        .toList();
    return rounds;
  }

  private record GameRound(GameMove elfMove, GameMove humanMove) {
    long scoreForHuman() {
        GameOutcome outcome;
        if (humanMove == elfMove) {
          outcome = GameOutcome.DRAW;
        } else if (elfMove.beatingMove() == humanMove) {
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
  }

  private enum GameMove {
    ROCK(1),
    PAPER(2),
    SCISSORS(3);

    private final long points;
    GameMove(long points) {
      this.points = points;
    }

    GameMove beatingMove() {
      return switch (this) {
        case ROCK -> PAPER;
        case PAPER -> SCISSORS;
        case SCISSORS -> ROCK;
      };
    }

    static GameMove stringToMove(String input) {
      return switch (input) {
        case "A", "X" -> GameMove.ROCK;
        case "B", "Y" -> GameMove.PAPER;
        case "C", "Z" -> GameMove.SCISSORS;
        default -> throw new IllegalStateException("Unexpected value: " + input);
      };
    }
  }
}
