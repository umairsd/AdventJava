// Created on 12/26/23.

import Foundation
import RegexBuilder

/// --- Day 15: Lens Library ---
/// https://adventofcode.com/2023/day/15
///
class Y2023Day15: Day {
  var dayNumber: Int = 15
  var year: Int = 2023

  
  required init() {}

  func part1(_ lines: [String]) -> String {
    guard lines.count > 0 else {
      fatalError()
    }
    let tokens = lines[0]
      .split(separator: ",")
      .map { String($0.trimmingCharacters(in: .whitespaces)).customHash() }
    let result = tokens.map { $0 }.reduce(0, +)
    return "\(result)"
  }

  
  func part2(_ lines: [String]) -> String {
    let boxes = parseTokens(lines[0])
    var focusingPowers: [Int] = []
    for box in boxes {
      for lens in box.lenses {
        focusingPowers.append(lens.focusingPower(in: box))
      }
    }

    let result = focusingPowers.reduce(0, +)
    return "\(result)"
  }
}


// MARK: - Parsing

extension Y2023Day15 {
  private static let labelRef = Reference(String.self)
  private static let operationRef = Reference(Operation.self)
  private static let focalLengthRef = Reference(Int.self)

  private static let tokenRegex = Regex {
    TryCapture(as: labelRef) {
      OneOrMore(.word)
    } transform: { w in
      String(w)
    }
    TryCapture(as: operationRef) {
      One(.any)
    } transform: { w in
      Operation.init(rawValue: String(w))
    }
    Optionally {
      TryCapture(as: focalLengthRef) {
        ZeroOrMore(.digit)
      } transform: { w in
        Int(w)
      }
    }
  }


  fileprivate func parseTokens(_ line: String) -> [Box] {
    var boxes: [Box] = []
    (0..<256).forEach { boxes.append(Box(boxId: $0)) }

    line.split(separator: ",")
      .map { String($0.trimmingCharacters(in: .whitespacesAndNewlines)) }
      .forEach { parseToken($0, into: &boxes) }

    return boxes
  }


  fileprivate func parseToken(_ token: String, into boxes: inout [Box]) {
    guard let match = token.firstMatch(of: Self.tokenRegex) else {
      return
    }
    let lensName = match[Self.labelRef]
    let operation = match[Self.operationRef]
    let boxId = lensName.customHash()
    assert(boxId <= 255)
    let box = boxes[boxId]

    switch operation {
    case .dash:
      box.removeLens(named: lensName)
      break

    case .equal:
      let focalLength = match[Self.focalLengthRef]
      let lens = Lens(name: lensName, focalLength: focalLength)
      box.addLens(lens)
      break
    }
  }

}


// MARK: - Helper Types

fileprivate class Box {
  let boxId: Int
  var lenses: [Lens]

  init(boxId: Int, lenses: [Lens] = []) {
    self.boxId = boxId
    self.lenses = lenses
  }

  func removeLens(named lensName: String) {
    lenses.removeAll { $0.name == lensName }
  }

  func addLens(_ lens: Lens) {
    if let indexToReplace = lenses.firstIndex(where: { $0.name == lens.name }) {
      lenses[indexToReplace] = lens
    } else {
      lenses.append(lens)
    }
  }
}


fileprivate enum Operation: String {
  case dash = "-"
  case equal = "="
}


fileprivate struct Lens {
  let name: String
  let focalLength: Int

  func focusingPower(in box: Box) -> Int {
    guard let index = box.lenses.firstIndex(where: { $0.name == self.name }) else {
      fatalError()
    }
    let slotNumber = index + 1
    let power = (box.boxId + 1) * slotNumber * focalLength
    return power
  }
}


extension String {
  fileprivate func customHash() -> Int {
    var result = 0
    for c in self {
      guard let ascii = c.asciiValue else {
        continue
      }
      result += Int(ascii)
      result *= 17
      result %= 256
    }
    return result
  }
}
