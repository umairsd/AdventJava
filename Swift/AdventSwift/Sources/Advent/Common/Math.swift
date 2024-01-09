// Created on 1/8/24.

import Foundation


func gcd(_ a: Int, _ b: Int) -> Int {
  if b == 0 {
    return a
  }
  return gcd(b, a % b)
}


func lcm(_ a: Int, _ b: Int) -> Int {
  return a * b / gcd(a, b)
}
