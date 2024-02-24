package com.umair.aoc.y2022;

import com.umair.aoc.common.Day;

import java.util.ArrayList;
import java.util.List;

/**
 * Day 25: Full of Hot Air
 * <a href="https://adventofcode.com/2022/day/25">2022, Day-25</a>
 */
public class Day25 extends Day {

  public Day25() {
    super(25, 2022);
  }

  @Override
  protected String part1(List<String> lines) {
    List<Long> snafus = lines.stream().map(Day25::snafuToDecimal).toList();
    long decimalResult = snafus.stream().reduce(0L, Long::sum);
    String snafuResult = decimalToSnafu(decimalResult);
    return snafuResult;
  }

  @Override
  protected String part2(List<String> lines) {
    return null;
  }


  public static long snafuToDecimal(String line) {
    long number = 0;
    for (int i = 0; i < line.length(); i++) {
      int power = line.length() - i - 1;
      char c = line.charAt(i);

      long snafuDigit = snafuCharacterToDecimal(c);
      number += (long)Math.pow(5, power) * snafuDigit;
    }

    return number;
  }

  public static String decimalToSnafu(long number) {
    List<Long> base5LSBFirst = decimalToBaseFiveLSBFirst(number);
    List<Character> snafuLSBFirst = base5LSBFirstToSnafuLSBFirst(base5LSBFirst);

    StringBuilder sb = new StringBuilder();
    for (int i = snafuLSBFirst.size() - 1; i >= 0; i--) {
      sb.append(snafuLSBFirst.get(i));
    }

    return sb.toString();
  }

  private static List<Character> base5LSBFirstToSnafuLSBFirst(List<Long> base5LSBFirst) {
    List<Character> result = new ArrayList<>();
    long remainder = 0;
    for (Long aLong : base5LSBFirst) {
      long digit = aLong + remainder;

      List<Integer> snafuDigits = base5DigitToSnafuDigits(digit);
      if (snafuDigits.size() == 1) {
        result.add(digitToSnafuCharacter(snafuDigits.get(0)));
        remainder = 0;
      } else {
        result.add(digitToSnafuCharacter(snafuDigits.get(1)));
        remainder = snafuDigits.get(0);
      }
    }

    if (remainder != 0) {
      result.add(digitToSnafuCharacter(remainder));
    }

    return result;
  }

  private static List<Long> decimalToBaseFiveLSBFirst(long number) {
    if (number == 0) {
      return List.of(0L);
    }

    List<Long> base5LSBFirst = new ArrayList<>();
    while (number != 0) {
      long remainder = number % 5;
      number = number / 5;
      base5LSBFirst.add(remainder);
    }
    return base5LSBFirst;
  }

  private static long snafuCharacterToDecimal(char c) {
    return switch (c) {
      case '=' -> -2;
      case '-' -> -1;
      case '0' -> 0;
      case '1' -> 1;
      case '2' -> 2;
      default -> throw new IllegalStateException();
    };
  }

  private static Character digitToSnafuCharacter(long digit) {
    int value = (int)digit;
    return switch(value) {
      case -2 -> '=';
      case -1 -> '-';
      case 0 -> '0';
      case 1 -> '1';
      case 2 -> '2';
      default -> throw new IllegalStateException("Invalid value for snafu digit: " + digit);
    };
  }

  private static List<Integer> base5DigitToSnafuDigits(long base5Digit) {
    int value = (int) base5Digit;
    return switch(value) {
      case 0 -> List.of(0);
      case 1 -> List.of(1);
      case 2 -> List.of(2);
      case 3 -> List.of(1, -2);
      case 4 -> List.of(1, -1);
      case 5 -> List.of(1, 0);
      case 6 -> List.of(1, 1);
      case 7 -> List.of(1, 2);
      case 8 -> List.of(2, -2);
      case 9 -> List.of(2, -1);
      default -> throw new IllegalStateException();
    };

  }
}
