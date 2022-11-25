package com.umair.aoc.y2021;

import com.umair.aoc.common.Day;

import java.util.*;
import java.util.stream.Collectors;

public class Day06 extends Day {

  private static final int TIME_TO_SPAWN_PARENT = 6;
  private static final int TIME_TO_SPAWN_CHILD = 8;

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

    long numFish = startingFish.size();
    return Long.toString(numFish);
  }

  @Override
  protected String part2(List<String> lines) {
    List<Lanternfish> startingFish = Arrays.stream(lines.get(0).strip().split(","))
        .map(String::strip)
        .map(Integer::parseInt)
        .map(Lanternfish::new)
        .collect(Collectors.toCollection(ArrayList::new)); // creates a mutable collection.

    // For each generation, store the count of fish created, and their current timer.
    List<Generation> spawnedGenerations = new ArrayList<>();
    long totalFish = startingFish.size();

    int iterationCount = 0;
    while (iterationCount < 256) {
      long spawnedByOriginal = startingFish
          .stream()
          .map(Lanternfish::update)
          .flatMap(Optional::stream)
          .count();

      long spawned = 0;

      for (Generation gen : spawnedGenerations) {
        // Find any generations where the timer is 0.
        if (gen.timer == 0) {
          // Reset the timer, and create new fish by adding to `spawned`.
          gen.timer = TIME_TO_SPAWN_PARENT;
          spawned += gen.fishCount;
        } else {
          gen.timer -= 1;
        }
      }

      long totalSpawned = spawnedByOriginal + spawned;
      spawnedGenerations.add(new Generation(totalSpawned, TIME_TO_SPAWN_CHILD));

      totalFish += totalSpawned;
      iterationCount++;
    }

    return Long.toString(totalFish);
  }

  @Override
  protected String part1Filename() {
    return filenameFromDataFileNumber(2);
  }

  @Override
  protected String part2Filename() {
    return filenameFromDataFileNumber(2);
  }

  private static class Lanternfish {
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
        timer = TIME_TO_SPAWN_PARENT;
        Lanternfish spawn = new Lanternfish(TIME_TO_SPAWN_CHILD);
        return Optional.of(spawn);
      } else {
        timer--;
        return Optional.empty();
      }
    }
  }

  private static class Generation {
    long fishCount;
    long timer;
    Generation(long fishCount, long timer) {
      this.fishCount = fishCount;
      this.timer = timer;
    }
  }
}
