package com.umair.aoc.y2021;

import com.umair.aoc.common.Day;

import java.util.ArrayList;
import java.util.Collections;
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
    Stack<Character> stack = new Stack<>();
    for (String line : lines) {
      stack.clear();

      for (Character c : line.toCharArray()) {
        if (isOpening(c)) {
          stack.push(c);
        } else if (!stack.isEmpty() && getMatching(c) == stack.peek()) {
          stack.pop();
        } else {
          totalScore += getSyntaxCheckerScore(c);
          break;
        }
      }
    }

    return Long.toString(totalScore);
  }

  @Override
  protected String part2(List<String> lines) {
    if (lines.isEmpty()) {
      return INPUT_EMPTY;
    }

    List<Long> scores = new ArrayList<>();
    Stack<Character> stack = new Stack<>();

    for (String line : lines) {
      stack.clear();
      boolean isCorrupted = false;
      for (Character c : line.toCharArray()) {
        if (isOpening(c)) {
          stack.push(c);
        } else if (!stack.isEmpty() && getMatching(c) == stack.peek()) {
          stack.pop();
        } else {
          isCorrupted = true;
          break;
        }
      }

      if (isCorrupted) {
        continue;
      }
        // At this point, we have an incomplete line.
      long totalScore = 0;
      while (!stack.isEmpty()) {
        Character top = stack.pop();
        totalScore *= 5;
        totalScore += getAutoCompleteScore(getMatching(top));
      }
      scores.add(totalScore);
    }

    Collections.sort(scores);

    assert(scores.size() % 2 == 1);
    return Long.toString(scores.get(scores.size() / 2));
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

  private static long getSyntaxCheckerScore(Character c) {
    return switch (c) {
      case ')' -> 3;
      case ']' -> 57;
      case '}' -> 1197;
      case '>' -> 25137;
      default -> 0;
    };
  }

  private static long getAutoCompleteScore(Character c) {
    return switch (c) {
      case ')' -> 1;
      case ']' -> 2;
      case '}' -> 3;
      case '>' -> 4;
      default -> 0;
    };
  }
}
