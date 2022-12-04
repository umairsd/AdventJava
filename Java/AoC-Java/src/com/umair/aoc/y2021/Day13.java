package com.umair.aoc.y2021;

import com.umair.aoc.common.Day;

import java.awt.*;
import java.util.List;
import java.util.NoSuchElementException;

public class Day13 extends Day {

  private static final Character DOT = '#';

  public Day13() {
    super(13, 2021);
  }

  @Override
  protected String part1(List<String> lines) {
    int splitIndex = lines.indexOf("");
    List<Point> points = parsePoints(lines.subList(0, splitIndex));
    Instruction firstInstruction =
        parseInstructions(lines.subList(splitIndex + 1, lines.size())).get(0);

    char[][] paper = buildDottedPaper(points);

    char[][] foldedPaper = foldPaper(paper, firstInstruction);

    int dots = countDots(foldedPaper);
    return Integer.toString(dots);
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

  private static char[][] foldPaper(char[][] paper, Instruction instruction) {
    return switch(instruction.foldDirection) {
      case UP -> foldUp(paper, instruction.foldPoint);
      case LEFT -> foldDown(paper, instruction.foldPoint);
    };
  }

  private static char[][] foldUp(char[][] paper, int yFoldPoint) {
    int paperLength = paper.length;
    int paperWidth = paper[0].length;

    int lengthOfBottom = paperLength - yFoldPoint - 1;
    // yFoldPoint is the length of the top half, and lengthToFold is the length of the bottom half.
    int lengthToFold = Math.min(yFoldPoint, lengthOfBottom);

    for (int rowToFold = 1; rowToFold <= lengthToFold; rowToFold++) {
      int source = yFoldPoint + rowToFold;
      int rowToUpdate = yFoldPoint - rowToFold;

      for (int column = 0; column < paperWidth; column++) {
        if (paper[source][column] == DOT) {
          paper[rowToUpdate][column] = paper[source][column];
        }
      }
    }

    // Build a new foldedPaper.
    char[][] foldedPaper = new char[yFoldPoint][paperWidth];
    for (int row = 0; row < foldedPaper.length; row++) {
      for (int column = 0; column < foldedPaper[row].length; column++) {
        foldedPaper[row][column] = paper[row][column];
      }
    }

    return foldedPaper;
  }

  private static char[][] foldDown(char[][] paper, int xFoldPoint) {
    int paperLength = paper.length;
    int paperWidth = paper[0].length;

    int widthOfRight = paperWidth - xFoldPoint - 1;
    int numColumnsToFold = Math.min(xFoldPoint, widthOfRight);

    for (int col = 1; col <= numColumnsToFold; col++) {
      int source = xFoldPoint + col;
      int columnToUpdate = xFoldPoint - col;

      for (int row = 0; row < paperLength; row++) {
        if (paper[row][source] == DOT) {
          paper[row][columnToUpdate] = paper[row][source];
        }
      }
    }

    // Build a new foldedPaper.
    char[][] foldedPaper = new char[paperLength][xFoldPoint];
    for (int row = 0; row < foldedPaper.length; row++) {
      for (int column = 0; column < foldedPaper[row].length; column++) {
        foldedPaper[row][column] = paper[row][column];
      }
    }

    return foldedPaper;
  }

  private static int countDots(char[][] dottedPaper) {
    int dots = 0;
    for (char[] line : dottedPaper) {
      for (char c : line) {
        dots += (c == DOT ? 1 : 0);
      }
    }
    return dots;
  }

  private static char[][] buildDottedPaper(List<Point> points) {
    int maxX = points.stream()
        .map(Point::getX)
        .mapToInt(i -> i)
        .max()
        .orElseThrow(NoSuchElementException::new);

    int maxY = points.stream()
        .map(Point::getY)
        .mapToInt(i -> i)
        .max()
        .orElseThrow(NoSuchElementException::new);

    char[][] dottedPaper = new char[maxY + 1][maxX + 1];

    // For easier debugging, initialize the paper with blanks.
    for (int r = 0; r <= maxY; r++) {
      for (int c = 0; c <= maxX; c++) {
        dottedPaper[r][c] = '.';
      }
    }

    // Draw the dots.
    for (Point p : points) {
      dottedPaper[p.y][p.x] = DOT;
    }

    return dottedPaper;
  }

  private static List<Point> parsePoints(List<String> lines) {
    List<Point> points = lines
        .stream()
        .map(ln -> {
          String[] tokens = ln.strip().split(",");
          int x = Integer.parseInt(tokens[0].strip());
          int y = Integer.parseInt(tokens[1].strip());
          return new Point(x, y);
        })
        .toList();
    return points;
  }

  private static List<Instruction> parseInstructions(List<String> lines) {
    List<Instruction> instructions = lines
        .stream()
        .map(Day13::parseInstruction)
        .toList();
    return instructions;
  }

  private static Instruction parseInstruction(String line) {
    String[] tokens = line.strip().split(" ");
    assert(tokens.length == 3);
    String[] dimensionTokens = tokens[2].strip().split("=");

    FoldDirection fd = FoldDirection.parseFoldDirection(dimensionTokens[0].strip());
    int foldPoint = Integer.parseInt(dimensionTokens[1].strip());
    Instruction instruction = new Instruction(fd, foldPoint);
    return instruction;
  }

  private static class Point {
    private final int x;
    private final int y;

    Point(int x, int y) {
      this.x = x;
      this.y = y;
    }
    int getX() {
      return x;
    }
    int getY() {
      return y;
    }
  }

  private static class Instruction {
    FoldDirection foldDirection;
    int foldPoint;
    Instruction(FoldDirection foldDirection, int foldPoint) {
      this.foldDirection = foldDirection;
      this.foldPoint = foldPoint;
    }
  }

  private enum FoldDirection {
    UP,
    LEFT;

    static FoldDirection parseFoldDirection(String s) {
      return switch(s) {
        case "x" -> LEFT;
        case "y" -> UP;
        default -> throw new IllegalStateException();
      };
    }
  }
}
