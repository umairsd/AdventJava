package com.umair.aoc.y2022;

import com.umair.aoc.common.Day;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Day03 extends Day {

  public Day03() {
    super(3, 2022);
  }

  @Override
  protected String part1(List<String> lines) {
    List<Rucksack> rucksacks = parseLinesToRucksacks(lines);

    long totalPriority = 0;
    for (Rucksack r : rucksacks) {
      char c = findIntersection(r.getFirst(), r.getSecond());
      long priority = getPriority(c);
      totalPriority += priority;
    }

    return Long.toString(totalPriority);
  }

  @Override
  protected String part2(List<String> lines) {
    List<Rucksack> rucksacks = parseLinesToRucksacks(lines);
    List<ElfGroup> elfGroups = parseRucksacksToElfGroups(rucksacks);

    long totalPriority = 0;
    for (ElfGroup g : elfGroups) {
      char c = g.getBadge();
      long priority = getPriority(c);
      totalPriority += priority;
    }

    return Long.toString(totalPriority);
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

    assert(common.size() >= 1);
    return common.get(0);
  }

  private static List<Rucksack> parseLinesToRucksacks(List<String> lines) {
    List<Rucksack> rucksacks = new ArrayList<>();
    for (String line : lines) {
      assert(line.length() % 2 == 0);
      List<Character> contents = line.chars().mapToObj(e -> (char) e).toList();
      rucksacks.add(new Rucksack(contents));
    }
    return rucksacks;
  }

  private static List<ElfGroup> parseRucksacksToElfGroups(List<Rucksack> rucksacks) {
    AtomicInteger counter = new AtomicInteger();

    Collection<List<Rucksack>> chunks = rucksacks.stream()
        .collect(Collectors.groupingBy(it -> counter.getAndIncrement() / 3))
        .values();

    List<ElfGroup> result = new ArrayList<>();
    for (List<Rucksack> chunk : chunks) {
      ElfGroup g = new ElfGroup();
      g.rucksacks.addAll(chunk);
      result.add(g);
    }

    return result;
  }

  private static class Rucksack {
    List<Character> rucksackContents;

    Rucksack(List<Character> contents) {
      this.rucksackContents = contents;
    }

    List<Character> getFirst() {
      assert(rucksackContents.size() % 2 == 0);
      return rucksackContents.subList(0, rucksackContents.size() / 2);
    }

    List<Character> getSecond() {
      assert(rucksackContents.size() % 2 == 0);
      return rucksackContents.subList(rucksackContents.size() / 2, rucksackContents.size());
    }
  }

  private static class ElfGroup {
    private final List<Rucksack> rucksacks = new ArrayList<>();

    Character getBadge() {
      assert(rucksacks.size() == 3);
      List<Character> common = new ArrayList<>(rucksacks.get(0).rucksackContents);
      common.retainAll(rucksacks.get(1).rucksackContents);
      common.retainAll(rucksacks.get(2).rucksackContents);

      assert(common.size() >= 1);
      return common.get(0);
    }
  }
}
