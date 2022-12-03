package com.umair.aoc.y2022;

import com.umair.aoc.common.Day;

import java.util.*;

public class Day03 extends Day {

  public Day03() {
    super(3, 2022);
  }

  @Override
  protected String part1(List<String> lines) {
    List<Rucksack> rucksacks = parseLinesToRucksacks(lines);

    long totalPriority = 0;
    for (Rucksack r : rucksacks) {
      char c = findIntersection(r.first, r.second);
      long priority = getPriority(c);
      totalPriority += priority;
    }

    return Long.toString(totalPriority);
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

  private static long getPriority(Character c) {
    if (Character.isLowerCase(c)) {
      return c - 'a' + 1;
    } else {
      return c - 'A' + 1 + 26;
    }
  }

  private static Character findIntersection(List<Character> l1, List<Character> l2) {
    List<Character> common = new ArrayList<>(l1);
    common.retainAll(l2);

    assert(common.size() == 1);
    return common.get(0);
  }

  private static List<Rucksack> parseLinesToRucksacks(List<String> lines) {
    List<Rucksack> rucksacks = new ArrayList<>();
    for (String line : lines) {
      assert(line.length() % 2 == 0);
      List<Character> firstHalf = line.substring(0, line.length() / 2)
          .chars()
          .mapToObj(e -> (char) e)
          .toList();
      List<Character> secondHalf = line.substring(line.length() / 2)
          .chars()
          .mapToObj(e -> (char) e)
          .toList();

      rucksacks.add(new Rucksack(firstHalf, secondHalf));
    }
    return rucksacks;
  }

  private static class Rucksack {
    List<Character> first;
    List<Character> second;

    Rucksack(List<Character> first, List<Character> second) {
      this.first = first;
      this.second = second;
    }
  }
}
