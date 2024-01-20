// Created on 12/5/23.

import Foundation
import RegexBuilder

/// --- Day 5: If You Give A Seed A Fertilizer ---
/// https://adventofcode.com/2023/day/5
class Y2023Day05: Day {
  var dayNumber: Int = 05
  var year: Int = 2023

  required init() {}

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
        transformedResult = group.convert(transformedResult)
      }
      return transformedResult
    }

    let result = locations.min() ?? -1
    return "\(result)"
  }


  func part2(_ lines: [String]) -> String {
    let seedRanges = parseSeedRanges(lines[0])

    let mapSections: [[String]] = lines[1...]
      .split(separator: "")
      .map { Array($0) }
    assert(mapSections.count == 7)
    // List of transform groups, in the order in which they appear in the input.
    let groups: [TransformGroup] = mapSections
      .map { TransformGroup(transforms: parseTransforms($0)) }

    var convertedRanges: [ClosedRange<Int>] = []
    for seedRange in seedRanges {
      var inputRanges = [seedRange]

      for transformGroup in groups {
        var transformedResult: [ClosedRange<Int>] = []
        for range in inputRanges {
          let x = transformGroup.convert(range)
          transformedResult.append(contentsOf: x)
        }
        // Output of each transform step becomes the input for the next step.
        inputRanges = transformedResult
      }
      convertedRanges.append(contentsOf: inputRanges)
    }

    let sortedRanges = convertedRanges.sorted { r1, r2  in
      r1.lowerBound < r2.lowerBound
    }
    let result = sortedRanges.first?.lowerBound ?? -1
    return "\(result)"
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


  func parseSeedRanges(_ line: String) -> [ClosedRange<Int>] {
    guard let match = line.firstMatch(of: Self.seedsRegex) else {
      return []
    }
    let data = match[Self.seedListRef]
    var seedRanges: [ClosedRange<Int>] = []
    var i = 0
    while i < (data.count - 1) {
      seedRanges.append(data[i]...(data[i] + data[i + 1] - 1))
      i += 2
    }
    return seedRanges
  }


  func parseTransforms(_ lines: [String]) -> [Transform] {
    let t = lines.compactMap { parseTransform($0) }
    return t
  }


  func parseTransform(_ line: String) -> Transform? {
    guard let match = line.firstMatch(of: Self.intervalLineRegex) else {
      return nil
    }
    return Transform(
      sourceStart: match[Self.sourceStartRef],
      destinationStart: match[Self.destinationStartRef],
      rangeLength: match[Self.rangeLengthRef])
  }

}


// MARK: - Helper types.


/// A special map that maintains a list of `Tranform`s, and transforms a given integer into a
/// new integer based on these intervals.
struct TransformGroup {
  let transforms: [Transform]

  /// Transforms a single number.
  func convert(_ number: Int) -> Int {
    let filteredTransforms = transforms.filter { $0.contains(number) }
    assert(filteredTransforms.count <= 1)
    if filteredTransforms.count == 1 {
      return filteredTransforms.first!.convert(number)
    }
    return number
  }


  func convert(_ range: ClosedRange<Int>) -> [ClosedRange<Int>] {
    var unconvertedRanges = [range]
    var result: [ClosedRange<Int>] = []

    for transform in transforms {
      var rangesToBeProcessed: [ClosedRange<Int>] = []

      while !unconvertedRanges.isEmpty {
        let unconvertedRange = unconvertedRanges.removeFirst()
        let mapping = transform.convert(unconvertedRange)

        if let converted = mapping.converted {
          result.append(converted)
          unconvertedRanges.append(contentsOf: mapping.unconverted)
        } else {
          // If no part of the range could be converted, it means that it either translates as is
          // or needs to be considered by the next transform.
          rangesToBeProcessed.append(unconvertedRange)
        }
      }

      // The input of the next step is the output of the current step.
      unconvertedRanges = rangesToBeProcessed
    }

    result.append(contentsOf: unconvertedRanges)
    return result
  }
}


struct Transform {
  let sourceRange: ClosedRange<Int>
  let destinationStart: Int
  let rangeLength: Int

  init(sourceStart: Int, destinationStart: Int, rangeLength: Int) {
    self.sourceRange = sourceStart...(sourceStart + rangeLength - 1)
    self.destinationStart = destinationStart
    self.rangeLength = rangeLength
  }

  func contains(_ number: Int) -> Bool {
    sourceRange.contains(number)
  }


  func convert(_ number: Int) -> Int {
    if sourceRange.contains(number) {
      return mapToRange(number)
    }
    return number
  }


  private func mapToRange(_ number: Int) -> Int {
    assert(sourceRange.contains(number))
    let delta = number - sourceRange.lowerBound
    return destinationStart + delta
  }


  /// Transforms a Range. Parts of the range that cannot be transformed are returned as new
  /// ranges.
  func convert(
    _ range: ClosedRange<Int>
  ) -> (converted: ClosedRange<Int>?, unconverted: [ClosedRange<Int>]) {

    if !range.overlaps(sourceRange) {
      // Range is entirely outside the source range.
      return (nil, [range])

    } else if range.lowerBound >= sourceRange.lowerBound && 
                range.upperBound <= sourceRange.upperBound {
      // There's overlap, and range is entirely within the source range.
      assert(sourceRange.contains(range.lowerBound))
      assert(sourceRange.contains(range.upperBound))

      let convertedRange = mapToRange(range.lowerBound) ... mapToRange(range.upperBound)
      return (convertedRange, [])

    } else if range.lowerBound < sourceRange.lowerBound {
      // There's overlap, and range starts before the source range...
      if range.upperBound <= sourceRange.upperBound {
        // (a) ... and ends within it.
        assert(sourceRange.contains(range.upperBound))

        // The first element of the converted range is the destinationStart
        let convertedRange = destinationStart ... mapToRange(range.upperBound)
        let unconvertedRange = range.lowerBound ... (sourceRange.lowerBound - 1)
        return (convertedRange, [unconvertedRange])

      } else {
        // (b) ... and ends after it.
        let convertedRange = destinationStart ... destinationStart + rangeLength - 1

        let leftUnconvertedRange = range.lowerBound ... (sourceRange.lowerBound - 1)
        let rightUnconvertedRange = (sourceRange.upperBound + 1) ... range.upperBound
        return (convertedRange, [leftUnconvertedRange, rightUnconvertedRange])
      }

    } else {
      // There's overlap, and the range starts after the source range's start, and ends after
      // the source range's end.
      assert(contains(range.lowerBound))
      let convertedRange = mapToRange(range.lowerBound) ... destinationStart + rangeLength - 1
      let unconvertedRange = (sourceRange.upperBound + 1) ... range.upperBound
      return (convertedRange, [unconvertedRange])
    }
  }

}
