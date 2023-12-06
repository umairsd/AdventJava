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

    // List of maps, in the order in which they appear in the input.
    let mappers: [IntegerMapper] = mapSections.map { IntegerMapper(intervals: parseIntervals($0)) }
    let locations = seeds.map { seed in
      var v = seed
      for mapper in mappers {
        v = mapper.transform(v)
      }
      return v
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


  func parseIntervals(_ lines: [String]) -> [Interval] {
    let intervals = lines.compactMap { parseInterval($0) }
    return intervals
  }


  func parseInterval(_ line: String) -> Interval? {
    guard let match = line.firstMatch(of: Self.intervalLineRegex) else {
      return nil
    }
    let destinationStart = match[Self.destinationStartRef]
    let sourceStart = match[Self.sourceStartRef]
    let length = match[Self.rangeLengthRef]
    return Interval(sourceStart: sourceStart, destinationStart: destinationStart, length: length)
  }

}


// MARK: - Helper types.


/// A special map that maintains a list of `Interval`s, and transforms a given integer into a
/// new integer based on these intervals.
struct IntegerMapper {
  let intervals: [Interval]

  func transform(_ number: Int) -> Int {
    let filteredIntervals = intervals.filter { $0.contains(number) }
    assert(filteredIntervals.count <= 1)
    if filteredIntervals.count == 1 {
      return filteredIntervals.first!.transform(number)
    }
    return number
  }
}


struct Interval {
  let sourceStart: Int
  let destinationStart: Int
  let length: Int
  private let sourceEnd: Int

  init(sourceStart: Int, destinationStart: Int, length: Int) {
    self.sourceStart = sourceStart
    self.destinationStart = destinationStart
    self.length = length
    self.sourceEnd = sourceStart + length - 1
  }

  func contains(_ number: Int) -> Bool {
    return number >= sourceStart && number <= sourceEnd
  }

  func transform(_ source: Int) -> Int {
    if contains(source) {
      let delta = source - sourceStart
      return destinationStart + delta
    }
    return source
  }
}
