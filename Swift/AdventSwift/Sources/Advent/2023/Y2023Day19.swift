// Created on 12/31/23.

import Foundation
import RegexBuilder


/// --- Day 19: Aplenty ---
/// https://adventofcode.com/2023/day/19
///
class Y2023Day19: Day {
  var dayNumber: Int = 19
  var year: Int = 2023
  var dataFileNumber: Int

  private static let startingWorkflowName = "in"

  required init(dataFileNumber: Int) {
    self.dataFileNumber = dataFileNumber
  }

  func part1(_ lines: [String]) -> String {
    let splits = lines.split(separator: "")
    assert(splits.count == 2)
    let workflowsMap = parseWorkflows(Array(splits[0]))
    let parts = splits[1].compactMap { parsePart($0) }

    var acceptedParts: [Part] = []
    var i = 0

    var currentWorkflowName = Self.startingWorkflowName
    while i < parts.count {
      let part = parts[i]
      let currentWorkflow = workflowsMap[currentWorkflowName]!

      let result = currentWorkflow.processPart(part)
      switch result {

      case .accepted:
        acceptedParts.append(part)
        i += 1 // handle the next part.
        currentWorkflowName = Self.startingWorkflowName
      case .rejected:
        i += 1 // handle the next part.
        currentWorkflowName = Self.startingWorkflowName
      case .routed(let name):
        currentWorkflowName = name
      }
    }

    let result = acceptedParts.map { $0.ratingsTotal() }.reduce(0, +)
    return "\(result)"
  }


  func part2(_ lines: [String]) -> String {
    return ""
  }
}

// MARK: - Types


fileprivate struct Workflow {
  let name: String
  let rules: [Rule]

  func processPart(_ part: Part) -> RuleResult {
    for rule in rules {
      guard let condition = rule.condition else {
        return rule.result
      }
      if condition.holds(for: part) {
        return rule.result
      }
    }
    fatalError()
  }
}

fileprivate struct Condition {
  let property: Property
  let operation: Operation
  let value: Int

  func holds(for part: Part) -> Bool {
    switch (property, operation) {
    case (.x, .lt):
      return part.x < value
    case (.x, .gt):
      return part.x > value

    case (.m, .lt):
      return part.m < value
    case (.m, .gt):
      return part.m > value

    case (.a, .lt):
      return part.a < value
    case (.a, .gt):
      return part.a > value

    case (.s, .lt):
      return part.s < value
    case (.s, .gt):
      return part.s > value
    }
  }
}

fileprivate struct Rule {
  let condition: Condition?
  let result: RuleResult

  init(condition: Condition? = nil, result: RuleResult) {
    self.condition = condition
    self.result = result
  }
}

fileprivate enum Operation: String {
  case lt = "<"
  case gt = ">"
}

fileprivate enum Property: String, CaseIterable {
  case x = "x"
  case m = "m"
  case a = "a"
  case s = "s"
}

fileprivate enum RuleResult {
  case accepted
  case rejected
  case routed(String)

  static func ruleResult(from input: String) -> RuleResult {
    return switch input {
    case "A":
      RuleResult.accepted
    case "R":
      RuleResult.rejected
    default:
      RuleResult.routed(input)
    }
  }
}


fileprivate struct Part {
  let x: Int
  let m: Int
  let a: Int
  let s: Int

  func ratingsTotal() -> Int {
    return x + m + a + s
  }
}

// MARK: - Parsing

fileprivate extension Y2023Day19 {
  private static let partValueXRef = Reference(Int.self)
  private static let partValueMRef = Reference(Int.self)
  private static let partValueARef = Reference(Int.self)
  private static let partValueSRef = Reference(Int.self)

  private static let workflowNameRef = Reference(String.self)
  private static let rulesLineRef = Reference(String.self)

  private static let propertyRef = Reference(Property.self)
  private static let conditionOperationRef = Reference(Operation.self)
  private static let conditionValueRef = Reference(Int.self)
  
  private static let ruleResultRef = Reference(RuleResult.self)

  /// E.g. To parse `"{x=787,m=2655,a=1222,s=2876}"`
  private static let partRegex = Regex {
    "{x="
    TryCapture(as: partValueXRef) {
      OneOrMore(.digit)
    } transform: { w in
      Int(w)
    }
    ",m="
    TryCapture(as: partValueMRef) {
      OneOrMore(.digit)
    } transform: { w in
      Int(w)
    }
    ",a="
    TryCapture(as: partValueARef) {
      OneOrMore(.digit)
    } transform: { w in
      Int(w)
    }
    ",s="
    TryCapture(as: partValueSRef) {
      OneOrMore(.digit)
    } transform: { w in
      Int(w)
    }
    "}"
  }


  /// E.g. To parse `"px{a<2006:qkq,m>2090:A,rfg}"`
  private static let workflowRegex = Regex {
    TryCapture(as: workflowNameRef) {
      OneOrMore(.word)
    } transform: { w in
      String(w)
    }
    "{"
    TryCapture(as: rulesLineRef) {
      OneOrMore(.any)
    } transform: { w in
      String(w)
    }
    "}"
  }



  /// E.g. To parse `"a<1231:aba"`
  private static let ruleRegex1 = Regex {
    TryCapture(as: propertyRef) {
      OneOrMore(.word)
    } transform: { w in
      Property.init(rawValue: String(w))
    }
    TryCapture(as: conditionOperationRef) {
      ChoiceOf {
        "<"
        ">"
      }
    } transform: { w in
      Operation.init(rawValue: String(w))
    }
    TryCapture(as: conditionValueRef) {
      OneOrMore(.digit)
    } transform: { w in
      Int(w)
    }
    ":"
    TryCapture(as: ruleResultRef) {
      OneOrMore(.any)
    } transform: { w in
      RuleResult.ruleResult(from: String(w))
    }
  }

  // E.g. To parse `"A"`
  private static let ruleRegex2 = Regex {
    TryCapture(as: ruleResultRef) {
      OneOrMore(.any)
    } transform: { w in
      RuleResult.ruleResult(from: String(w))
    }
  }


  // MARK: - Parsing functions

  func parseWorkflows(_ lines: [String]) -> [String: Workflow] {
    var workflowMap: [String: Workflow] = [:]

    let workflows = lines.compactMap { parseWorkflow($0) }
    workflows.forEach { workflowMap[$0.name] = $0 }
    return workflowMap
  }


  func parseWorkflow(_ line: String) -> Workflow? {
    guard let match = line.firstMatch(of: Self.workflowRegex) else {
      return nil
    }
    let name = match[Self.workflowNameRef]
    let rulesLine = match[Self.rulesLineRef]

    let rules = rulesLine
      .split(separator: ",")
      .compactMap {
        parseRule($0.trimmingCharacters(in: .whitespaces))
      }

    let workflow = Workflow(name: name, rules: rules)
    return workflow
  }

  func parseRule(_ line: String) -> Rule? {
    if let match = line.firstMatch(of: Self.ruleRegex1) {
      let condition = Condition(
        property: match[Self.propertyRef],
        operation: match[Self.conditionOperationRef],
        value: match[Self.conditionValueRef])
      let result = match[Self.ruleResultRef]
      let r = Rule(condition: condition, result: result)
      return r

    } else if let match = line.firstMatch(of: Self.ruleRegex2) {
      let result = match[Self.ruleResultRef]
      let r = Rule(result: result)
      return r
    }

    return nil
  }

  func parsePart(_ line: String) -> Part? {
    guard let match = line.firstMatch(of: Self.partRegex) else {
      return nil
    }
    let part = Part(
      x: match[Self.partValueXRef],
      m: match[Self.partValueMRef],
      a: match[Self.partValueARef],
      s: match[Self.partValueSRef])
    return part
  }
}
