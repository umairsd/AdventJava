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
    List<Pixel> pixels = lines.stream().map(Day18::parsePixel).toList();
    int allExposedSides = countExposedSides(pixels);
    return Integer.toString(allExposedSides);
  }

  /**
   * Approach:
   * - Create a box that completely surrounds the lava object.
   * - Set each point (pixel) within the box as filled with AIR.
   * - Set the pixels in the box that denote lava with state LAVA.
   * - Fill the rest of the box with water. Use Flood-Fill algorithm.
   */
  @Override
  protected String part2(List<String> lines) {
    List<Pixel> lavaPixels = lines.stream().map(Day18::parsePixel).toList();
    Box box = buildBox(lavaPixels);
    // Set the state of all lava pixels (cubes).
    lavaPixels.forEach(c -> box.setPixelState(c, PixelState.LAVA));
    // Start filling the box with water. Uses Flood-Fill
    floodFillWithWater(box);

    // Find all pixels whose state is AIR. These are the inner pixels that were not filled with
    // water during the previous step.
    List<Pixel> unfilledInnerPixels = box.pixelStateMap.entrySet().stream()
        .filter(e -> e.getValue() == PixelState.AIR)
        .map(Map.Entry::getKey)
        .toList();


    int allExposedSides = countExposedSides(lavaPixels);
    int innerExposedSides = countExposedSides(unfilledInnerPixels);
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

  private static int countExposedSides(List<Pixel> pixels) {
    int totalExposedSides = pixels.size() * 6;

    for (int i = 0; i < pixels.size(); i++) {
      for (int j = i + 1; j < pixels.size(); j++) {
        var c1 = pixels.get(i);
        var c2 = pixels.get(j);
        if (arePixelsTouching(c1, c2)) {
          totalExposedSides -= 2;
        }
      }
    }
    return totalExposedSides;
  }

  private static void floodFillWithWater(Box box) {
    Queue<Pixel> queue = new ArrayDeque<>();
    queue.add(box.min);
    // Pixels that have been added to the queue. Need this so that points are not processed a 2nd
    // time
    Set<Pixel> processed = new HashSet<>();
    processed.add(box.min);

    while (!queue.isEmpty()) {
      Pixel c = queue.poll();
      assert(box.getPixelState(c) == PixelState.AIR);
      // Fill the pixel with water.
      box.setPixelState(c, PixelState.WATER);

      List<Pixel> neighbors = box.neighborsInside(c);
      for (Pixel n : neighbors) {
        if (box.getPixelState(n) == PixelState.AIR && !processed.contains(n)) {
          queue.add(n);
          processed.add(n);
        }
      }
    }
  }

  private static boolean arePixelsTouching(Pixel c1, Pixel c2) {
    boolean result = (c1.x == c2.x && c1.y == c2.y && Math.abs(c1.z - c2.z) == 1) ||
        (c1.x == c2.x && c1.z == c2.z && Math.abs(c1.y - c2.y) == 1) ||
        (c1.z == c2.z && c1.y == c2.y && Math.abs(c1.x - c2.x) == 1);
    return result;
  }

  private static final Pattern linePattern = Pattern.compile("(.*),(.*),(.*)");

  private static Pixel parsePixel(String line) {
    Matcher m = linePattern.matcher(line);
    if (!m.matches()) {
      throw new IllegalStateException("Bad input line: " + line);
    }
    int x = Integer.parseInt(m.group(1).strip());
    int y = Integer.parseInt(m.group(2).strip());
    int z = Integer.parseInt(m.group(3).strip());
    return new Pixel(x, y, z);
  }

  private static Box buildBox(List<Pixel> pixels) {
    int minX = pixels.stream().mapToInt(Pixel::x).min().orElseThrow() - 1;
    int minY = pixels.stream().mapToInt(Pixel::y).min().orElseThrow() - 1;
    int minZ = pixels.stream().mapToInt(Pixel::z).min().orElseThrow() - 1;
    Pixel minPixel = new Pixel(minX, minY, minZ);

    int maxX = pixels.stream().mapToInt(Pixel::x).max().orElseThrow() + 1;
    int maxY = pixels.stream().mapToInt(Pixel::y).max().orElseThrow() + 1;
    int maxZ = pixels.stream().mapToInt(Pixel::z).max().orElseThrow() + 1;
    Pixel maxPixel = new Pixel(maxX, maxY, maxZ);

    Box box = new Box(minPixel, maxPixel);
    return box;
  }

  private record Pixel(int x, int y, int z) {}

  private enum PixelState {
    LAVA,
    AIR,
    WATER
  }

  private static class Box {
    private final Pixel min;
    private final Pixel max;
    private final Map<Pixel, PixelState> pixelStateMap = new HashMap<>();

    private Box(Pixel min, Pixel max) {
      this.min = min;
      this.max = max;
      fillBoxWith(this, PixelState.AIR);
    }

    private void setPixelState(Pixel pixel, PixelState state) {
      pixelStateMap.put(pixel, state);
    }

    private PixelState getPixelState(Pixel pixel) {
      if (!pixelStateMap.containsKey(pixel)) {
        throw new IllegalStateException("Pixel is not inside the box: " + pixel);
      }
      return pixelStateMap.get(pixel);
    }

    private boolean isInside(Pixel pixel) {
      boolean validX = (pixel.x >= min.x && pixel.x <= max.x);
      boolean validY = (pixel.y >= min.y && pixel.y <= max.y);
      boolean validZ = (pixel.z >= min.z && pixel.z <= max.z);
      return validX && validY && validZ;
    }

    private List<Pixel> neighborsInside(Pixel pixel) {
      List<Pixel> neighbors =  List.of(
          new Pixel(pixel.x, pixel.y, pixel.z + 1),
          new Pixel(pixel.x, pixel.y, pixel.z - 1),
          new Pixel(pixel.x, pixel.y + 1, pixel.z),
          new Pixel(pixel.x, pixel.y - 1, pixel.z),
          new Pixel(pixel.x + 1, pixel.y, pixel.z),
          new Pixel(pixel.x - 1, pixel.y, pixel.z)
      );

      List<Pixel> filtered = neighbors.stream()
          .filter(this::isInside)
          .toList();
      return filtered;
    }

    private static void fillBoxWith(Box box, PixelState state) {
      for (int x = box.min.x; x <= box.max.x; x++) {
        for (int y = box.min.y; y <= box.max.y; y++) {
          for (int z = box.min.z; z <= box.max.z; z++) {
            box.setPixelState(new Pixel(x, y, z), state);
          }
        }
      }
    }
  }
}
