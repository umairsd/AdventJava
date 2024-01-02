// Created on 1/1/24.

import Foundation

extension ClosedRange<Int> {

  /// Bisects the given range into two sub-ranges.
  func bisectingRange(at value: Int) -> (ClosedRange<Int>, ClosedRange<Int>) {
    assert(value > lowerBound && value < upperBound)
    return ((lowerBound...value - 1), (value...upperBound))
  }

}
