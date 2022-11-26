package com.umair.aoc.y2021;

import com.umair.aoc.common.Day;

import java.util.List;
import java.util.Stack;

import static com.umair.aoc.common.Constants.INPUT_EMPTY;

public class Day10 extends Day {

  public Day10() {
    super(10, 2021);
  }

  @Override
  protected String part1(List<String> lines) {
    if (lines.isEmpty()) {
      return INPUT_EMPTY;
    }

    long totalScore = 0;
    for (String line : lines) {
      Stack<Character> stack = new Stack<>();

      for (Character c : line.toCharArray()) {
        if (isOpening(c)) {
          stack.push(c);
          continue;
        }

        if (stack.isEmpty()) {
          totalScore += getScore(c);
          break;
        }

        if (getMatching(c) == stack.peek()) {
          stack.pop();
        } else {
          totalScore += getScore(c);
          break;
        }
      }
    }

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

  private static Character getMatching(Character c) {
    return switch(c) {
      case ')' -> '(';
      case '(' -> ')';
      case '[' -> ']';
      case ']' -> '[';
      case '{' -> '}';
      case '}' -> '{';
      case '<' -> '>';
      case '>' -> '<';
      default -> 'a';
    };
  }

  private static boolean isOpening(Character c) {
    return switch (c) {
      case '(', '[', '{', '<' -> true;
      default -> false;
    };
  }

  private static long getScore(Character c) {
    return switch (c) {
      case ')' -> 3;
      case ']' -> 57;
      case '}' -> 1197;
      case '>' -> 25137;
      default -> 0;
    };
  }
}
