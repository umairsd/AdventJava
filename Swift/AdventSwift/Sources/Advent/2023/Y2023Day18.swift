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
    let instructions = lines.compactMap { parseInstruction($0) }
    let (_, _, minX, minY) = getMinMaxXY(instructions)

    // Create the offset for the start position so that we don't have to deal with negative
    // coordinates.
    let start = Position(row: (0 - minY), column: (0 - minX))
    let path = pathFormedBy(instructions, startingAt: start)
    let polygonOutline = path.removingDuplicates()
    let result = numPointsInPolygon(polygonOutline)
    return "\(result)"
  }

  
  func part2(_ lines: [String]) -> String {
    ""
  }

  // MARK: - Private


  // Gets the total number of points inside the polygon, and the points on the path that make up
  // the polygon.
  private func numPointsInPolygon(_ outline: [Position]) -> Int {
    let pairs: [(Position, Position)] = pairwise(outline + [outline.first!])

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


  private func pairwise<T>(_ list: [T]) -> [(T, T)] {
    guard !list.isEmpty else {
      return []
    }

    var result: [(T, T)] = []
    for r in 1..<list.count {
      let e1 = list[r - 1]
      let e2 = list[r]
      result.append((e1, e2))
    }
    result.append((list.last!, list.first!))
    return result
  }


  private func pathFormedBy(
    _ instructions: [Instruction],
    startingAt start: Position
  ) -> [Position] {

    var vertices: [Position] = []
    vertices.append(start)

    var currentR = start.row
    var currentC = start.column
    for instruction in instructions {
      (0..<instruction.distance).forEach { _ in
        switch instruction.moveDirection {
        case .up:
          currentR -= 1
        case .down:
          currentR += 1
        case .left:
          currentC -= 1
        case .right:
          currentC += 1
        }
        vertices.append(Position(row: currentR, column: currentC))
      }
    }
    return vertices
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

fileprivate struct Instruction: Hashable {
  let moveDirection: Direction
  let distance: Int
  let color: Color
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
  private static let redColorRef = Reference(String.self)
  private static let greenColorRef = Reference(String.self)
  private static let blueColorRef = Reference(String.self)

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
    TryCapture(as: redColorRef) {
      Repeat(1...2) {
        One(.hexDigit)
      }
    } transform: { w in
      String(w)
    }
    TryCapture(as: greenColorRef) {
      Repeat(1...2) {
        One(.hexDigit)
      }
    } transform: { w in
      String(w)
    }
    TryCapture(as: blueColorRef) {
      Repeat(1...2) {
        One(.hexDigit)
      }
    } transform: { w in
      String(w)
    }
    ")"
  }


  func parseInstruction(_ line: String) -> Instruction? {
    guard let match = line.firstMatch(of: Self.instructionRegex) else {
      return nil
    }

    let direction = match[Self.directionRef]
    let distance = match[Self.distanceRef]

    let red = match[Self.redColorRef]
    let green = match[Self.greenColorRef]
    let blue = match[Self.blueColorRef]

    let color = Color(red: red, green: green, blue: blue)

    return Instruction(moveDirection: direction, distance: distance, color: color)
  }
}
