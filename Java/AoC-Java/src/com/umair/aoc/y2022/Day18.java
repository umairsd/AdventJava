package com.umair.aoc.y2022;

import com.umair.aoc.common.Day;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day18 extends Day {

  public Day18() {
    super(18, 2022);
  }

  @Override
  protected String part1(List<String> lines) {
    List<Cube> cubes = lines.stream().map(Day18::parseCube).toList();
    int allExposedSides = countExposedSides(cubes);
    return Integer.toString(allExposedSides);
  }

  @Override
  protected String part2(List<String> lines) {
    List<Cube> lavaCubes = lines.stream().map(Day18::parseCube).toList();
    Box box = buildBox(lavaCubes);
    // Set the state of all lava cubes.
    lavaCubes.forEach(c -> box.setCubeState(c, CubeState.LAVA));
    // Start filling the box with water. Uses Flood-Fill
    floodFillWithWater(box);

    // Find all cubes whose state is AIR. These are the inner cubes that were not filled with
    // water during the previous step.
    List<Cube> unfilledInnerCubes = box.cubeStateMap.entrySet().stream()
        .filter(e -> e.getValue() == CubeState.AIR)
        .map(Map.Entry::getKey)
        .toList();


    int allExposedSides = countExposedSides(lavaCubes);
    int innerExposedSides = countExposedSides(unfilledInnerCubes);
    int outerExposedSides = allExposedSides - innerExposedSides;
    return Integer.toString(outerExposedSides);
  }

  @Override
  protected String part1Filename() {
    return filenameFromDataFileNumber(2);
  }

  @Override
  protected String part2Filename() {
    return filenameFromDataFileNumber(2);
  }

  private static int countExposedSides(List<Cube> cubes) {
    int totalExposedSides = cubes.size() * 6;

    for (int i = 0; i < cubes.size(); i++) {
      for (int j = i + 1; j < cubes.size(); j++) {
        var c1 = cubes.get(i);
        var c2 = cubes.get(j);
        if (areCubesTouching(c1, c2)) {
          totalExposedSides -= 2;
        }
      }
    }
    return totalExposedSides;
  }

  private static void floodFillWithWater(Box box) {
    Queue<Cube> queue = new ArrayDeque<>();
    queue.add(box.min);
    // Set of cubes that have been added to the queue.
    Set<Cube> processed = new HashSet<>();
    processed.add(box.min);

    while (!queue.isEmpty()) {
      Cube c = queue.poll();
      assert(box.getCubeState(c) == CubeState.AIR);
      // Fill the cube with water.
      box.setCubeState(c, CubeState.WATER);

      List<Cube> neighbors = box.neighborsInside(c);
      for (Cube n : neighbors) {
        if (box.getCubeState(n) == CubeState.AIR && !processed.contains(n)) {
          queue.add(n);
          processed.add(n);
        }
      }
    }
  }

  private static boolean areCubesTouching(Cube c1, Cube c2) {
    boolean result = (c1.x == c2.x && c1.y == c2.y && Math.abs(c1.z - c2.z) == 1) ||
        (c1.x == c2.x && c1.z == c2.z && Math.abs(c1.y - c2.y) == 1) ||
        (c1.z == c2.z && c1.y == c2.y && Math.abs(c1.x - c2.x) == 1);
    return result;
  }

  private static final Pattern linePattern = Pattern.compile("(.*),(.*),(.*)");

  private static Cube parseCube(String line) {
    Matcher m = linePattern.matcher(line);
    if (!m.matches()) {
      throw new IllegalStateException("Bad input line: " + line);
    }
    int x = Integer.parseInt(m.group(1).strip());
    int y = Integer.parseInt(m.group(2).strip());
    int z = Integer.parseInt(m.group(3).strip());
    return new Cube(x, y, z);
  }

  private static Box buildBox(List<Cube> cubes) {
    int minX = cubes.stream().mapToInt(Cube::x).min().orElseThrow() - 1;
    int minY = cubes.stream().mapToInt(Cube::y).min().orElseThrow() - 1;
    int minZ = cubes.stream().mapToInt(Cube::z).min().orElseThrow() - 1;
    Cube minCube = new Cube(minX, minY, minZ);

    int maxX = cubes.stream().mapToInt(Cube::x).max().orElseThrow() + 1;
    int maxY = cubes.stream().mapToInt(Cube::y).max().orElseThrow() + 1;
    int maxZ = cubes.stream().mapToInt(Cube::z).max().orElseThrow() + 1;
    Cube maxCube = new Cube(maxX, maxY, maxZ);

    Box box = new Box(minCube, maxCube);
    return box;
  }

  private record Cube(int x, int y, int z) {}

  private enum CubeState {
    LAVA,
    AIR,
    WATER
  }

  private static class Box {
    Cube min;
    Cube max;
    Map<Cube, CubeState> cubeStateMap = new HashMap<>();

    private Box(Cube min, Cube max) {
      this.min = min;
      this.max = max;
      fillBoxWith(this, CubeState.AIR);
    }

    private void setCubeState(Cube cube, CubeState state) {
      cubeStateMap.put(cube, state);
    }

    private CubeState getCubeState(Cube cube) {
      if (!cubeStateMap.containsKey(cube)) {
        throw new IllegalStateException("Cube is not inside the box: " + cube);
      }
      return cubeStateMap.get(cube);
    }

    private boolean isInside(Cube cube) {
      boolean validX = (cube.x >= min.x && cube.x <= max.x);
      boolean validY = (cube.y >= min.y && cube.y <= max.y);
      boolean validZ = (cube.z >= min.z && cube.z <= max.z);
      return validX && validY && validZ;
    }

    private List<Cube> neighborsInside(Cube cube) {
      List<Cube> neighbors =  List.of(
          new Cube(cube.x, cube.y, cube.z + 1),
          new Cube(cube.x, cube.y, cube.z - 1),
          new Cube(cube.x, cube.y + 1, cube.z),
          new Cube(cube.x, cube.y - 1, cube.z),
          new Cube(cube.x + 1, cube.y, cube.z),
          new Cube(cube.x - 1, cube.y, cube.z)
      );

      List<Cube> filtered = neighbors.stream()
          .filter(this::isInside)
          .toList();
      return filtered;
    }

    private static void fillBoxWith(Box box, CubeState state) {
      for (int x = box.min.x; x <= box.max.x; x++) {
        for (int y = box.min.y; y <= box.max.y; y++) {
          for (int z = box.min.z; z <= box.max.z; z++) {
            box.setCubeState(new Cube(x, y, z), state);
          }
        }
      }
    }
  }
}
