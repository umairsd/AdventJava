// Created on 4/3/23.

import Foundation
import RegexBuilder

/// --- Day 9: Rope Bridge ---
class Y2022Day09: Day {
  var dayNumber: Int = 9
  var year: Int = 2022

  required init() {}


  func part1(_ lines: [String]) -> String {
    let moves = parseMoves(from: lines)

    var head = Position(row: 0, column: 0)
    var tail = Position(row: 0, column: 0)
    var tailPositions = Set<Position>()
    tailPositions.insert(tail)

    for move in moves {
      var step = 0
      while step < move.distance {
        // Move the head by one step.
        head = head.moveByOne(move.direction)
        // Move the tail such that it follows the head.
        tail = tail.translatePosition(to: head)

        tailPositions.insert(tail)
        step += 1
      }
    }

    return "\(tailPositions.count)"
  }


  func part2(_ lines: [String]) -> String {
    let moves = parseMoves(from: lines)

    guard let rope = Rope(length: 10) else {
      fatalError("Unable to create a rope.")
    }

    var tailPositions = Set<Position>()
    tailPositions.insert(rope.tail)

    for move in moves {
      var step = 0
      while step < move.distance {
        // Move the head by one step.
        rope.head = rope.head.moveByOne(move.direction)

        // Move all the other knots.
        for i in 1..<rope.knots.count {
          // Move the i-th knot such that it follows the knot (i - 1)
          let previousKnot = rope.knots[i - 1]
          rope.knots[i] = rope.knots[i].translatePosition(to: previousKnot)
        }

        tailPositions.insert(rope.tail)
        step += 1
      }
    }

    return "\(tailPositions.count)"
  }
}


// MARK: - Helper Data Types

fileprivate class Rope {
  var knots: [Position]

  var head: Position {
    get {
      return knots.first!
    }
    set {
      knots[0] = newValue
    }
  }

  var tail: Position {
    return knots.last!
  }

  init?(length: Int) {
    guard length >= 2 else {
      return nil
    }
    knots = []
    for _ in 0..<length {
      knots.append(Position(row: 0, column: 0))
    }
  }
}

fileprivate enum Direction: String {
  case up = "U"
  case down = "D"
  case left = "L"
  case right = "R"
}

fileprivate struct Move {
  let direction: Direction
  let distance: Int
}

fileprivate extension Position {

  func translatePosition(to p: Position) -> Position {
    // Case-1: `p` is one unit away in any direction. No need to move.
    if isTouching(p) {
      return Position(row: self.row, column: self.column)
    }

    // Case-2: `p` is two units away along horizontal or vertical direction.
    if abs(self.row - p.row) == 0 {
      let columnDelta = p.column - self.column
      assert(abs(columnDelta) == 2, "The current position will never be two units away from `p`.")
      let position = columnDelta < 0 ? moveByOne(.left) : moveByOne(.right)
      return position
    }

    if abs(self.column - p.column) == 0 {
      let rowDelta = p.row - self.row
      let position = rowDelta < 0 ? moveByOne(.up) : moveByOne(.down)
      return position
    }

    // Case-3: `p` is diagonally away.
    let rowDelta = (p.row - self.row).signum()
    let columnDelta = (p.column - self.column).signum()
    return Position(row: self.row + rowDelta, column: self.column + columnDelta)
  }


  func moveByOne(_ direction: Direction) -> Position {
    let d: Position
    switch direction {
      case .up:
        d = Position(row: self.row - 1, column: self.column)
      case .down:
        d = Position(row: self.row + 1, column: self.column)
      case .left:
        d = Position(row: self.row, column: self.column - 1)
      case .right:
        d = Position(row: self.row, column: self.column + 1)
    }
    return d
  }


  func isTouching(_ p: Position) -> Bool {
    let vDistance = abs(self.column - p.column)
    let hDistance = abs(self.row - p.row)
    return vDistance <= 1 && hDistance <= 1
  }
}


// MARK: - Parsing

extension Y2022Day09 {

  private static let directionRef = Reference(Direction.self)
  private static let distanceRef = Reference(Int.self)

  private static let moveParsingRegex = Regex {
    TryCapture(as: directionRef) {
      One(.word)
    } transform: { match in
      Direction(rawValue: String(match))
    }
    " "
    TryCapture(as: distanceRef) {
      OneOrMore(.digit)
    } transform: { match in
      Int(match)
    }
  }


  fileprivate func parseMoves(from lines: [String]) -> [Move] {
    let moves: [Move] = lines.compactMap { parseMove($0) }
    return moves
  }


  fileprivate func parseMove(_ line: String) -> Move? {
    guard let match = line.firstMatch(of: Self.moveParsingRegex) else {
      return nil
    }

    let move = Move(direction: match[Self.directionRef], distance: match[Self.distanceRef])
    return move
  }
}
