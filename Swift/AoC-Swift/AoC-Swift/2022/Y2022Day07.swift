// Created on 3/21/23.

import Foundation
import RegexBuilder

class Y2022Day07: Day {

  var dayNumber: Int = 7
  var year: Int = 2022
  var dataFileNumber: Int = 2

  required init(dataFileNumber: Int) {
    self.dataFileNumber = dataFileNumber
  }

  func part1(_ lines: [String]) -> String {
    guard let root = parseFileSystem(from: lines) else {
      return ""
    }

    var filteredNodes: [FileSystemNode] = []
    root.filter(into: &filteredNodes) { $0.isDirectory() && $0.size() <= 100_000 }
    let total = filteredNodes.map { $0.size() }.reduce(0, +)
    return "\(total)"
  }
  

  func part2(_ lines: [String]) -> String {
    guard let root = parseFileSystem(from: lines) else {
      return ""
    }

    let totalUsedSpace = root.size()
    let totalFilesystemSize = 70_000_000
    let spacedNeededForUpgrade = 30_000_000

    let availableSpace = totalFilesystemSize - totalUsedSpace
    let spacedToBeDeleted = spacedNeededForUpgrade - availableSpace

    var filteredNodes: [FileSystemNode] = []
    root.filter(into: &filteredNodes) { $0.isDirectory() && $0.size() >= spacedToBeDeleted }

    let sortedSizes = filteredNodes.map { $0.size() }.sorted()
    return "\(sortedSizes.first ?? 0)"
  }
}


/// Represents contents of a directory.
fileprivate class DirectoryData {
  let name: String
  var size: Int?
  var children: [String: FileSystemNode] = [:]

  init(_ name: String) {
    self.name = name
  }
}

/// Represents contents of a file.
fileprivate class FileData {
  let name: String
  let size: Int

  init(_ name: String, size: Int) {
    self.name = name
    self.size = size
  }
}

fileprivate enum FileSystemNode {
  case file(data: FileData)
  case directory(data: DirectoryData)

  func isFile() -> Bool {
    switch self {
      case .file:
        return true
      case .directory:
        return false
    }
  }

  func isDirectory() -> Bool {
    return !isFile()
  }

  func size() -> Int {
    switch self {
      case .file(let fData):
        return fData.size

      case .directory(let dData):
        guard let size = dData.size else {
          fatalError("Error: data has not been parsed completely.")
        }
        return size
    }
  }

  func filter(into result: inout [FileSystemNode], _ isIncluded: (FileSystemNode) -> Bool) {
    if isIncluded(self) {
      result.append(self)
    }

    switch self {
      case .file:
        return
      case .directory(let dData):
        for childNode in dData.children.values {
          childNode.filter(into: &result, isIncluded)
        }
    }
  }

}


// MARK: - Parsing

extension Y2022Day07 {

  private static let listFilesCommand = "$ ls"
  private static let goUpOneLevelCommand = "$ cd .."
  private static let nameRef = Reference(String.self)
  private static let sizeRef = Reference(Int.self)

  private static let commandCD = Regex {
    "$ cd "
    TryCapture(as: nameRef) {
      OneOrMore(.any)
    } transform: { match in
      String(match)
    }
  }

  private static let commandLS = Regex {
    "$ ls"
  }

  private static let directoryRegex = Regex {
    "dir "
    TryCapture(as: nameRef) {
      OneOrMore(.any)
    } transform: { match in
      String(match)
    }
  }

  private static let fileRegex = Regex {
    TryCapture(as: sizeRef) {
      OneOrMore(.digit)
    } transform: { match in
      Int(match)
    }
    " "
    TryCapture(as: nameRef) {
      OneOrMore(.any)
    } transform: { match in
      String(match)
    }
  }


  private func parseFileSystem(from lines: [String]) -> FileSystemNode? {
    guard let root = parseRawDataIntoTree(from: lines) else {
      return nil
    }

    // Second phase: Compute directory sizes
    traverseToPopulateDirectorySize(root)
    return root
  }

  @discardableResult
  private func traverseToPopulateDirectorySize(_ root: FileSystemNode) -> Int {
    switch root {
      case .file(let fData):
        return fData.size

      case .directory(let dData):
        if let size = dData.size {
          return size
        }
        var total = 0
        for childNode in dData.children.values {
          total += traverseToPopulateDirectorySize(childNode)
        }
        dData.size = total
        return total
    }
  }

  private func parseRawDataIntoTree(from lines: [String]) -> FileSystemNode? {
    guard !lines.isEmpty else { return nil }
    let root = FileSystemNode.directory(data: DirectoryData("/"))

    // A stack to maintain the current directory.
    var stack: [FileSystemNode] = []
    stack.append(root)
    var currentNode = root

    for line in lines.suffix(from: 1) {
      if line == Self.listFilesCommand || line.isEmpty {
        continue
      }
      else if line == Self.goUpOneLevelCommand {
        // Going up a level.
        stack.removeLast()
        guard let last = stack.last else {
          fatalError("")
        }
        currentNode = last
      }
      else if line.starts(with: Self.commandCD) {
        guard let match = line.firstMatch(of: Self.commandCD) else {
          continue
        }
        guard case let .directory(directoryData) = currentNode else {
          fatalError("WTF: Somehow a file got added to the stack.")
        }

        let childDirectoryName = match[Self.nameRef]
        guard let childNode = directoryData.children[childDirectoryName] else {
          // Assumption: The set of input commands will always do an `ls` inside a directory
          // before `cd` into a subdirectory.
          fatalError("Error: Have no done an `ls` prior to `cd` into a subdirectory.")
        }

        currentNode = childNode
        stack.append(currentNode)
      }
      else {
        guard case let .directory(directoryData) = currentNode else {
          fatalError("WTF: Somehow a file got added to the stack.")
        }
        guard let (childName, childNode) = parseFileSystemNode(from: line) else {
          continue
        }

        directoryData.children[childName] = childNode
      }
    }
    return root
  }

  private func parseFileSystemNode(from line: String?) -> (String, FileSystemNode)? {
    guard let line = line else {
      return nil
    }

    if line.starts(with: Self.fileRegex) {
      guard let match = line.firstMatch(of: Self.fileRegex) else {
        return nil
      }
      let name = match[Self.nameRef]
      let file = FileData(name, size: match[Self.sizeRef])
      return (name, .file(data: file))
    }
    else if line.starts(with: Self.directoryRegex) {
      guard let match = line.firstMatch(of: Self.directoryRegex) else {
        return nil
      }
      let name = match[Self.nameRef]
      let directory = DirectoryData(name)
      return (name, .directory(data: directory))
    }
    return nil
  }
}
