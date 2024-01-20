package com.umair.aoc.y2021;

import com.umair.aoc.common.Day;

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

    Paper paper = buildDottedPaper(points);
    foldPaper(paper, firstInstruction);
    int dots = countDots(paper);
    return Integer.toString(dots);
  }

  @Override
  protected String part2(List<String> lines) {
    int splitIndex = lines.indexOf("");
    List<Point> points = parsePoints(lines.subList(0, splitIndex));
    List<Instruction> instructions = parseInstructions(lines.subList(splitIndex + 1, lines.size()));
    Paper paper = buildDottedPaper(points);

    instructions.forEach(instr -> foldPaper(paper, instr));

    return paper.toString();
  }

  @Override
  protected String part1Filename() {
    return fileNameFromFileNumber(2);
  }

  @Override
  protected String part2Filename() {
    return fileNameFromFileNumber(2);
  }

  private static void foldPaper(Paper paper, Instruction instruction) {
    switch(instruction.foldDirection) {
      case UP -> foldUp(paper, instruction.foldPoint);
      case LEFT -> foldLeft(paper, instruction.foldPoint);
    }
  }

  private static void foldUp(Paper paper, int yFoldPoint) {
    int lengthOfBottom = paper.effectiveLength - yFoldPoint - 1;
    int numRowsToFold = Math.min(yFoldPoint, lengthOfBottom);

    for (int r = 1; r <= numRowsToFold; r++) {
      int sourceRow = yFoldPoint + r;
      int destinationRow = yFoldPoint - r;

      for (int column = 0; column < paper.effectiveWidth; column++) {
        if (paper.contents[sourceRow][column] == DOT) {
          paper.contents[destinationRow][column] = paper.contents[sourceRow][column];
        }
      }
    }

    // Update dimensions.
    paper.setEffectiveLength(yFoldPoint);
  }

  private static void foldLeft(Paper paper, int xFoldPoint) {
    int widthOfRight = paper.effectiveWidth - xFoldPoint - 1;
    int numColumnsToFold = Math.min(xFoldPoint, widthOfRight);

    for (int col = 1; col <= numColumnsToFold; col++) {
      int sourceColumn = xFoldPoint + col;
      int destinationColumn = xFoldPoint - col;

      for (int row = 0; row < paper.effectiveLength; row++) {
        if (paper.contents[row][sourceColumn] == DOT) {
          paper.contents[row][destinationColumn] = paper.contents[row][sourceColumn];
        }
      }
    }

    // Update dimensions.
    paper.setEffectiveWidth(xFoldPoint);
  }

  private static int countDots(Paper paper) {
    int dots = 0;
    for (int row = 0; row < paper.effectiveLength; row++) {
      for (int column = 0; column < paper.effectiveWidth; column++) {
        char c = paper.contents[row][column];
        dots += (c == DOT ? 1 : 0);
      }
    }
    return dots;
  }

  private static Paper buildDottedPaper(List<Point> points) {
    int maxX = points.stream()
        .map(Point::x)
        .mapToInt(i -> i)
        .max()
        .orElseThrow(NoSuchElementException::new);

    int maxY = points.stream()
        .map(Point::y)
        .mapToInt(i -> i)
        .max()
        .orElseThrow(NoSuchElementException::new);

    char[][] paperContents = new char[maxY + 1][maxX + 1];

    // For easier debugging, initialize the paper with blanks.
    for (int r = 0; r <= maxY; r++) {
      for (int c = 0; c <= maxX; c++) {
        paperContents[r][c] = '.';
      }
    }

    // Draw the dots.
    for (Point p : points) {
      paperContents[p.y][p.x] = DOT;
    }

    return new Paper(paperContents);
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

  private record Point(int x, int y) {
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

  private static class Paper {
    private final char[][] contents;

    private int effectiveWidth;
    private int effectiveLength;

    Paper(char[][] contents) {
      this.contents = contents;
      this.effectiveLength = contents.length;
      this.effectiveWidth = contents[0].length;
    }
    void setEffectiveLength(int updatedLength) {
      if (updatedLength >= contents.length || updatedLength < 0) {
        throw new IllegalArgumentException();
      }
      this.effectiveLength = updatedLength;
    }
    void setEffectiveWidth(int updatedWidth) {
      if (updatedWidth >= contents[0].length || updatedWidth < 0) {
        throw new IllegalArgumentException();
      }
      this.effectiveWidth = updatedWidth;
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("\n");
      for (int row = 0; row < effectiveLength; row++) {
        for (int column = 0; column < effectiveWidth; column++) {
          sb.append(contents[row][column]);
        }
        sb.append("\n");
      }
      return sb.toString();
    }
  }
}
