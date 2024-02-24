package com.umair.aoc.y2022;

import com.umair.aoc.common.Day;

import java.util.*;

public class Day07 extends Day {

  public Day07() {
    super(7, 2022);
  }

  @Override
  protected String part1(List<String> lines) {
    FSNode root = parseFilesystem(lines);
    traverseTreeToComputeDirectorySizes(root);

    long totalSizeOfSmallDirectories = traverseTreeToComputeDirectoriesBelowThreshold(root,
        100_000);
    return Long.toString(totalSizeOfSmallDirectories);
  }

  @Override
  protected String part2(List<String> lines) {
    FSNode root = parseFilesystem(lines);
    traverseTreeToComputeDirectorySizes(root);

    long totalFileSize = root.size;
    long totalFilesystemSize = 70_000_000;
    long spaceNeededForUpgrade = 30_000_000;

    long availableSpace = totalFilesystemSize - totalFileSize;
    long spaceToBeDeleted = spaceNeededForUpgrade - availableSpace;

    List<Long> sizes = new ArrayList<>();
    traverseTreeToCollectSizesAboveThreshold(root, spaceToBeDeleted, sizes);
    Collections.sort(sizes);
    return Long.toString(sizes.get(0));
  }



  /**
   * Traversal of the tree to collect all directory sizes above the given threshold.
   */
  private static void traverseTreeToCollectSizesAboveThreshold(
      FSNode root,
      long threshold,
      List<Long> collector
  ) {
    if (root.type == FileType.FILE) {
      return;
    }
    if (root.size >= threshold) {
      collector.add(root.size);
    }

    for (String childName : root.children.keySet()) {
      FSNode child = root.children.get(childName);
      traverseTreeToCollectSizesAboveThreshold(child, threshold, collector);
    }
  }

  /**
   * Traversal of the tree to compute the total size of all directories below the given threshold.
   */
  private static long traverseTreeToComputeDirectoriesBelowThreshold(FSNode root, long threshold) {
    if (root.type == FileType.FILE) {
      return 0;
    }

    long totalSize = root.size <= threshold ? root.size : 0;
    for (String childName : root.children.keySet()) {
      FSNode child = root.children.get(childName);
      totalSize += traverseTreeToComputeDirectoriesBelowThreshold(child, threshold);
    }
    return totalSize;
  }

  /**
   * Post order traversal of the tree to compute the total size of all directories.
   */
  private static long traverseTreeToComputeDirectorySizes(FSNode root) {
    if (root.type == FileType.FILE) {
      return root.size;
    }

    long totalSizeOfChildren = 0;
    for (String childName : root.children.keySet()) {
      FSNode child = root.children.get(childName);
      totalSizeOfChildren += traverseTreeToComputeDirectorySizes(child);
    }

    root.size = totalSizeOfChildren;
    return totalSizeOfChildren;
  }


  private static final String COMMAND_LS = "$ ls";
  private static final String COMMAND_CD = "$ cd";
  private static final String COMMAND_GO_UP = "$ cd ..";

  private static FSNode parseFilesystem(List<String> lines) {
    FSNode root = new FSNode("/", FileType.DIRECTORY);
    FSNode currentNode = root;

    Stack<FSNode> stack = new Stack<>();
    stack.push(root);

    for (String line : lines.stream().skip(1).toList()) {
      if (line.equals(COMMAND_LS) || line.isBlank()) {
        //noinspection UnnecessaryContinue
        continue;
      } else if (line.trim().equals(COMMAND_GO_UP)) {
        stack.pop();
        currentNode = stack.peek();

      } else if (line.trim().startsWith(COMMAND_CD)) {
        String[] tokens = line.split(" ");
        String directoryName = tokens[2].strip();

        if (currentNode.children.containsKey(directoryName)) {
          currentNode = currentNode.children.get(directoryName);
          stack.push(currentNode);
        } else {
          // Input Assumption: Must do an `ls` in a directory before changing into a subdirectory.
          // This means that the `children` dictionary already contains the key `directoryName`
          throw new IllegalArgumentException("Must do an `ls` before changing into a directory.");
        }
      } else {
        FSNode node = parseFSNode(line);
        currentNode.children.put(node.name, node);
      }
    }
    return root;
  }

  private static FSNode parseFSNode(String line) {
    if (line.strip().startsWith("dir")) {
      return parseDirectory(line);
    } else {
      return parseFile(line);
    }
  }

  private static FSNode parseFile(String line) {
    String[] tokens = line.strip().split(" ");
    assert (tokens.length == 2);
    long size = Long.parseLong(tokens[0]);
    FSNode fileNode = new FSNode(tokens[1], FileType.FILE);
    fileNode.setSize(size);
    return fileNode;
  }

  private static FSNode parseDirectory(String line) {
    String[] tokens = line.strip().split(" ");
    assert (tokens[0].equals("dir"));
    assert (tokens.length == 2);
    FSNode node = new FSNode(tokens[1], FileType.DIRECTORY);
    return node;
  }

  private enum FileType {
    FILE,
    DIRECTORY,
  }

  private static class FSNode {
    // If a file, size is the size of the file. If a directory, sum of all the children.
    private long size = 0;
    private final FileType type;
    private final String name;
    private final Map<String, FSNode> children = new HashMap<>();

    FSNode(String name, FileType type) {
      this.type = type;
      this.name = name;
    }

    private void setSize(long size) {
      this.size = size;
    }

    @Override
    public String toString() {
      return name
          + " ("
          + (type == FileType.FILE ? "f" : "d")
          + ", size="
          + size
          + ")";
    }
  }
}
