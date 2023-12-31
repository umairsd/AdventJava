// Created on 12/28/23.

import Foundation
import RegexBuilder

/// --- Day 18: Lavaduct Lagoon ---
/// https://adventofcode.com/2023/day/18
///
class Y2023Day18: Day {
  var dayNumber: Int = 18
  var year: Int = 2023
  var dataFileNumber: Int

  required init(dataFileNumber: Int) {
    self.dataFileNumber = dataFileNumber
  }

  func part1(_ lines: [String]) -> String {
    let instructions = lines.compactMap { parseInstructions($0) }
    // Create the offset for the start position so that we don't have to deal with negative
    // coordinates. This is not required though. I'm merely doing it to make debugging easier.
    let (_, _, minX, minY) = getMinMaxXY(instructions)

    let start = Position(row: (0 - minY), column: (0 - minX))
    let moves = instructions.map { Move(direction: $0.moveDirection, distance: $0.distance) }
    let polygonOutline = pathFormedBy(moves, startingAt: start).removingDuplicates()
    let result = numPointsInPolygon(polygonOutline)
    return "\(result)"
  }


  func part2(_ lines: [String]) -> String {
    let instructions = lines.compactMap { parseInstructions($0) }
    let start = Position(row: 0, column: 0)

    let moves: [Move] = instructions.compactMap{ instruction in
      guard let d = instruction.distanceFromColor(),
            let direction = instruction.directionFromColor()
      else {
        return nil
      }
      return Move(direction: direction, distance: d)
    }

    let polygonOutline = pathFormedBy(moves, startingAt: start).removingDuplicates()
    let result = numPointsInPolygon(polygonOutline)
    return "\(result)"
  }

  // MARK: - Private

  // Gets the total number of points inside the polygon, and the points on the path that make up
  // the polygon.
  private func numPointsInPolygon(_ outline: [Position]) -> Int {
    let pairs: [(Position, Position)] = (outline + [outline.first!]).pairwise()

    // Step 1: Determine the area using the Shoelace formula.
    let sum = pairs.reduce(0) { (partialResult, pair) in
      let p1 = pair.0
      let p2 = pair.1
      return partialResult + ((p1.row * p2.column) - (p2.row * p1.column))
    }
    let area = Double(sum) / 2.0

    // Step 2: Use the Pick's theorem to find the number of points inside a given shape given
    // its area.
    let numPointsInside = Int(abs(area) - 0.5 * Double(outline.count) + 1)

    // Step 3: The total number of points for this polygon
    let totalPoints = numPointsInside + outline.count
    return totalPoints
  }


  private func pathFormedBy(
    _ moves: [Move],
    startingAt start: Position
  ) -> [Position] {

    var positions: [Position] = [start]
    var currentR = start.row
    var currentC = start.column

    for move in moves {
      (0..<move.distance).forEach { _ in
        switch move.direction {
        case .up:
          currentR -= 1
        case .down:
          currentR += 1
        case .left:
          currentC -= 1
        case .right:
          currentC += 1
        }
        positions.append(Position(row: currentR, column: currentC))
      }
    }
    return positions
  }


  private func getMinMaxXY(
    _ instructions: [Instruction]
  ) -> (maxX: Int, maxY: Int, minX: Int, minY: Int) {
    var maxX = Int.min
    var maxY = Int.min
    var minX = Int.max
    var minY = Int.max

    var currentX = 0
    var currentY = 0
    for instruction in instructions {
      let distance = instruction.distance
      switch instruction.moveDirection {
      case .up:
        currentY -= distance
        minY = min(minY, currentY)
      case .down:
        currentY += distance
        maxY = max(maxY, currentY)
      case .left:
        currentX -= distance
        minX = min(minX, currentX)
      case .right:
        currentX += distance
        maxX = max(maxX, currentX)
      }
    }

    return (maxX, maxY, minX, minY)
  }
}


// MARK: - Types


fileprivate struct Move: Hashable {
  let direction: Direction
  let distance: Int
}


fileprivate struct Instruction: Hashable {
  let moveDirection: Direction
  let distance: Int
  let color: String

  func distanceFromColor() -> Int? {
    let hex = Array(color).dropLast().reduce("") { partialResult, c in
      partialResult + String(c)
    }
    return Int(hex, radix: 16)
  }

  func directionFromColor() -> Direction? {
    let d = Array(color).last
    return switch d {
    case "0":
      Direction.right
    case "1":
      Direction.down
    case "2":
      Direction.left
    case "3":
      Direction.up
    default:
      nil
    }
  }
}


fileprivate enum Direction: String {
  case up = "U"
  case down = "D"
  case left = "L"
  case right = "R"
}

fileprivate struct Color: Hashable {
  let red: String
  let green: String
  let blue: String

  func parseRGBColorComponent(_ input: String) -> CGFloat {
    guard let component = UInt32(input, radix: 16) else {
      fatalError()
    }
    let componentF = CGFloat( CGFloat(component) / 255.0)
    return componentF
  }
}


// MARK: - Parsing

fileprivate extension Y2023Day18 {
  private static let directionRef = Reference(Direction.self)
  private static let distanceRef = Reference(Int.self)
  private static let colorRef = Reference(String.self)

  private static let instructionRegex = Regex {
    TryCapture(as: directionRef) {
      One(.word)
    } transform: { w in
      Direction.init(rawValue: String(w))
    }
    " "
    TryCapture(as: distanceRef) {
      OneOrMore(.digit)
    } transform: { w in
      Int(w)
    }
    " (#"
    TryCapture(as: colorRef) {
      Repeat(1...6) {
        One(.hexDigit)
      }
    } transform: { w in
      String(w)
    }
    ")"
  }


  func parseInstructions(_ line: String) -> Instruction? {
    guard let match = line.firstMatch(of: Self.instructionRegex) else {
      return nil
    }
    let direction = match[Self.directionRef]
    let distance = match[Self.distanceRef]
    let colorString = match[Self.colorRef]
    return Instruction(moveDirection: direction, distance: distance, color: colorString)
  }
}
