package com.umair.aoc.y2022;

import com.umair.aoc.common.Day;

import java.util.*;
import java.util.List;

public class Day09 extends Day {

  public Day09() {
    super(9, 2022);
  }

  /**
   * Note: Part 1 can be refactored to use the `move` and `follow` model that's being used in
   * part-2. However, I am leaving this as is, as this approach is a bit more brute force-y, and
   * somewhat easier to parse and understand.
   */
  @Override
  protected String part1(List<String> lines) {
    List<Move> moves = parseMoves(lines);
    Set<Point> visited = new HashSet<>();

    Point head = new Point(0, 0);
    Point tail = new Point(0, 0);
    visited.add(tail);

    for (Move m : moves) {
      int count = m.distance;
      while (count > 0) {
        switch (m.direction) {
          case UP -> moveUpByOne(head, tail);
          case DOWN -> moveDownByOne(head, tail);
          case LEFT -> moveLeftByOne(head, tail);
          case RIGHT -> moveRightByOne(head, tail);
        }

        visited.add(new Point(tail));
        count--;
      }
    }

    int count = visited.size();
    return Integer.toString(count);
  }

  @Override
  protected String part2(List<String> lines) {
    List<Move> moves = parseMoves(lines);
    Set<Point> visited = new HashSet<>();

    List<Point> points = buildStartingPoints();
    visited.add(points.get(points.size() - 1));

    for (Move m : moves) {
      int count = m.distance;
      while (count > 0) {
        // Move the head.
        move(points.get(0), m.direction);

        // Have all the points follow the head.
        for (int i = 1; i < points.size(); i++) {
          follow(points.get(i - 1), points.get(i));
        }

        // Mark the tail's position.
        visited.add(new Point(points.get(points.size() - 1)));
        count--;
      }
    }

    int count = visited.size();
    return Integer.toString(count);
  }

  private static List<Point> buildStartingPoints() {
    return List.of(
        new Point(0, 0),
        new Point(0, 0),
        new Point(0, 0),
        new Point(0, 0),
        new Point(0, 0),
        new Point(0, 0),
        new Point(0, 0),
        new Point(0, 0),
        new Point(0, 0),
        new Point(0, 0)
    );
  }

  @Override
  protected String part1Filename() {
    return filenameFromDataFileNumber(2);
  }

  @Override
  protected String part2Filename() {
    return filenameFromDataFileNumber(2);
  }

  private static void move(Point point, Direction direction) {
    switch (direction) {
      case UP -> point.y++;
      case DOWN -> point.y--;
      case LEFT -> point.x--;
      case RIGHT -> point.x++;
    }
  }

  private static void follow(Point leading, Point trailing) {
    int deltaX = leading.x - trailing.x;
    int deltaY = leading.y - trailing.y;
    // The trailing point does not move if the leading point is "adjacent" to it.
    if (Math.abs(deltaX) <= 1 && Math.abs(deltaY) <= 1) {
      return;
    }

    // Can be replaced by: trailing.x += Math.signum(deltaX);
    if (deltaX > 0) { // move Right.
      trailing.x++;
    } else if (deltaX < 0) { // move Left.
      trailing.x--;
    }

    // Can be replaced by: trailing.y += Math.signum(deltaY);
    if (deltaY > 0) { // move Up.
      trailing.y++;
    } else if (deltaY < 0) { // move Down.
      trailing.y--;
    }
  }

  private static void moveLeftByOne(Point head, Point tail) {
    if (head.y == tail.y) { // Same row
      if (head.x < tail.x) { // Head to the left of tail
        head.x--;
        tail.x--;
      } else {
        head.x--;
      }

    } else {
      if (head.x >= tail.x) {
        // If on the same column, or head to the right of tail, only head moves.
        head.x--;
      } else {
        // Head is to the left of the tail. Head moves left, and tail moves diagonally.
        head.x--;
        tail.y = head.y;
        tail.x--;
      }
    }
  }

  private static void moveRightByOne(Point head, Point tail) {
    if (head.y == tail.y) { // Same row
      if (head.x > tail.x) { // Head to the right of tail
        head.x++;
        tail.x++;
      } else {
        head.x++;
      }

    } else {
      if (head.x <= tail.x) {
        // If on the same column, or head to the left of tail, only head moves.
        head.x++;
      } else {
        // Head is to the right of the tail. Head moves right, and tail moves diagonally.
        head.x++;
        tail.y = head.y;
        tail.x++;
      }
    }
  }

  private static void moveUpByOne(Point head, Point tail) {
    if (head.x == tail.x) { // Head and tail are on the same column.
      if (head.y > tail.y) {
        // If head is above the tail, then both move up.
        head.y++;
        tail.y++;
      } else {
        // Otherwise, head moves up, but tail stays put.
        head.y++;
      }

    } else {
      if (head.y <= tail.y) {
        // If on the same row, or head is below the tail, only head moves.
        head.y++;
      } else {
        // If head is above the tail, then head moves up, and tail moves diagonally.
        head.y++;
        tail.x = head.x;
        tail.y++;
      }
    }
  }

  private static void moveDownByOne(Point head, Point tail) {
    if (head.x == tail.x) { // Head and tail are on the same column.
      if (head.y < tail.y) {
        // If head is below the tail, then both move down.
        head.y--;
        tail.y--;
      } else {
        // Otherwise, head moves down, but tail stays put.
        head.y--;
      }

    } else {
      if (head.y >= tail.y) {
        // If on the same row, or head is above the tail, only head moves.
        head.y--;
      } else {
        // If head is above the below, then head moves down, and tail moves diagonally.
        head.y--;
        tail.x = head.x;
        tail.y--;
      }
    }
  }

  private static List<Move> parseMoves(List<String> lines) {
    List<Move> moves = new ArrayList<>();
    for (String line : lines) {
      String[] tokens = line.strip().split(" ");
      int distance = Integer.parseInt(tokens[1]);

      Direction d = switch (tokens[0]) {
        case "R" -> Direction.RIGHT;
        case "L" -> Direction.LEFT;
        case "U" -> Direction.UP;
        case "D" -> Direction.DOWN;
        default -> throw new IllegalArgumentException();
      };

      moves.add(new Move(d, distance));
    }
    return moves;
  }

  private enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT
  }

  private static class Move {
    Direction direction;
    int distance;

    public Move(Direction direction, int distance) {
      this.direction = direction;
      this.distance = distance;
    }
  }

  private static class Point {
    int y;
    int x;

    Point(int x, int y) {
      this.x = x;
      this.y = y;
    }

    Point(Point p) {
      this.x = p.x;
      this.y = p.y;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Point point = (Point) o;
      return y == point.y && x == point.x;
    }

    @Override
    public int hashCode() {
      return Objects.hash(y, x);
    }
  }
}
