package com.umair.aoc.y2021;

import com.umair.aoc.common.Day;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Day06 extends Day {

  public Day06() {
    super(6, 2021);
  }

  @Override
  protected String part1(List<String> lines) {
    List<Lanternfish> startingFish = Arrays.stream(lines.get(0).strip().split(","))
        .map(String::strip)
        .map(Integer::parseInt)
        .map(Lanternfish::new)
        .collect(Collectors.toCollection(ArrayList::new)); // creates a mutable collection.

    int iterationCount = 0;
    while (iterationCount < 80) {
      List<Lanternfish> newlySpawn = startingFish
          .stream()
          .map(Lanternfish::update)
          .flatMap(Optional::stream)
          .toList();

      startingFish.addAll(newlySpawn);
      iterationCount++;
    }

    int numFish = startingFish.size();
    return Integer.toString(numFish);
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

  private static class Lanternfish {
    private static final int PARENT_DEFAULT_SPAWN_TIME = 6;
    private static final int CHILD_SPAWN_TIME = 8;
    private int timer;

    private Lanternfish(int timer) {
      this.timer = timer;
    }

    @Override
    public String toString() {
      return Integer.toString(timer);
    }

    private Optional<Lanternfish> update() {
      // If the current value is zero, then the timer resets, and a new
      // Lanternfish is spawned.
      if (timer == 0) {
        timer = PARENT_DEFAULT_SPAWN_TIME;
        Lanternfish spawn = new Lanternfish(CHILD_SPAWN_TIME);
        return Optional.of(spawn);
      } else {
        timer--;
        return Optional.empty();
      }
    }
  }
}
