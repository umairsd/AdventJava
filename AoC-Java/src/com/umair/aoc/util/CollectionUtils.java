package com.umair.aoc.util;

import java.util.stream.Collector;
import java.util.stream.Collectors;

public class CollectionUtils {

  /**
   * Collects the elements of a stream into a list, and returns the single element in the list.
   * If the list has more than one element, throws IllegalStateException.
   *
   * @return A collector.
   * @param <T> The type of the list.
   */
  public static <T> Collector<T, ?, T> singleElement() {
    return Collectors.collectingAndThen(
        Collectors.toList(),
        list -> {
          if (list.size() != 1) {
            throw new IllegalStateException("The collection must have only one element.");
          }
          return list.get(0);
        }
    );
  }

}
