// Created on 12/5/23.

import Foundation
import RegexBuilder

/// --- Day 5: If You Give A Seed A Fertilizer ---
/// https://adventofcode.com/2023/day/5
class Y2023Day05: Day {
  var dayNumber: Int = 05
  var year: Int = 2023
  var dataFileNumber: Int

  required init(dataFileNumber: Int) {
    self.dataFileNumber = dataFileNumber
  }

  func part1(_ lines: [String]) -> String {
    let seeds = parseSeeds(lines[0])

    let mapSections: [[String]] = lines[1...]
      .split(separator: "")
      .map { Array($0) }
    assert(mapSections.count == 7)
    // List of transform groups, in the order in which they appear in the input.
    let groups: [TransformGroup] = mapSections
      .map { TransformGroup(transforms: parseTransforms($0)) }


    let locations = seeds.map { seed in
      var transformedResult = seed
      for group in groups {
        transformedResult = group.transform(transformedResult)
      }
      return transformedResult
    }

    let result = locations.min() ?? -1
    return "\(result)"
  }

  
  func part2(_ lines: [String]) -> String {
    ""
  }
}


// MARK: - Parsing.

extension Y2023Day05 {

  private static let seedListRef = Reference([Int].self)
  private static let destinationStartRef = Reference(Int.self)
  private static let sourceStartRef = Reference(Int.self)
  private static let rangeLengthRef = Reference(Int.self)

  private static let seedsRegex = Regex {
    "seeds: "
    TryCapture(as: seedListRef) {
      OneOrMore(.any)
    } transform: { match in
      let tokens = match.split(separator: " ")
      return tokens.compactMap { Int($0) }
    }
  }

  private static let intervalLineRegex = Regex {
    TryCapture(as: destinationStartRef) {
      OneOrMore(.digit)
    } transform: { match in
      Int(match)
    }
    " "
    TryCapture(as: sourceStartRef) {
      OneOrMore(.digit)
    } transform: { match in
      Int(match)
    }
    " "
    TryCapture(as: rangeLengthRef) {
      OneOrMore(.digit)
    } transform: { match in
      Int(match)
    }
  }


  func parseSeeds(_ line: String) -> [Int] {
    guard let match = line.firstMatch(of: Self.seedsRegex) else {
      return []
    }
    let seeds = match[Self.seedListRef]
    return seeds
  }


  func parseTransforms(_ lines: [String]) -> [Transform] {
    let t = lines.compactMap { parseTransform($0) }
    return t
  }


  func parseTransform(_ line: String) -> Transform? {
    guard let match = line.firstMatch(of: Self.intervalLineRegex) else {
      return nil
    }
    let destinationStart = match[Self.destinationStartRef]
    let sourceStart = match[Self.sourceStartRef]
    let length = match[Self.rangeLengthRef]
    return Transform(sourceStart: sourceStart, destinationStart: destinationStart, length: length)
  }

}


// MARK: - Helper types.


/// A special map that maintains a list of `Tranform`s, and transforms a given integer into a
/// new integer based on these intervals.
struct TransformGroup {
  let transforms: [Transform]

  func transform(_ number: Int) -> Int {
    let filteredTransforms = transforms.filter { $0.contains(number) }
    assert(filteredTransforms.count <= 1)
    if filteredTransforms.count == 1 {
      return filteredTransforms.first!.apply(number)
    }
    return number
  }
}


struct Transform {
  let sourceRange: Range<Int>
  let destinationStart: Int
  let length: Int

  init(sourceStart: Int, destinationStart: Int, length: Int) {
    self.sourceRange = sourceStart..<(sourceStart + length)
    self.destinationStart = destinationStart
    self.length = length
  }

  func contains(_ number: Int) -> Bool {
    sourceRange.contains(number)
  }

  func apply(_ number: Int) -> Int {
    if sourceRange.contains(number) {
      let delta = number - sourceRange.lowerBound
      return destinationStart + delta
    }
    return number
  }
}
