package com.umair.aoc.y2021;

import com.umair.aoc.common.Day;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Day 6: Lanternfish
 * <a href="https://adventofcode.com/2021/day/6">2021, Day-06</a>
 */
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
    String[] initialFish = lines.get(0).strip().split(",");
    Map<Long, Long> timerToCountMap = Arrays.stream(initialFish)
        .map(String::strip)
        .map(Long::parseLong)
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    List<Generation> generations = new ArrayList<>();
    for (Map.Entry<Long, Long> entry : timerToCountMap.entrySet()) {
      Generation g = new Generation(entry.getValue(), entry.getKey());
      generations.add(g);
    }

    long totalFish = initialFish.length;

    int iterationCount = 0;
    while (iterationCount < 256) {
      long spawned = 0;

      for (Generation gen : generations) {
        // Find any generations where the timer is 0.
        if (gen.timer == 0) {
          // Reset the timer, and create new fish by adding to `spawned`.
          gen.timer = TIME_TO_SPAWN_PARENT;
          spawned += gen.fishCount;
        } else {
          gen.timer -= 1;
        }
      }

      generations.add(new Generation(spawned, TIME_TO_SPAWN_CHILD));

      totalFish += spawned;
      iterationCount++;
    }

    return Long.toString(totalFish);
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
