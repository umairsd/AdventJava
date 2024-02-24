package com.umair.aoc.y2022;

import com.umair.aoc.common.DayTestBase;
import org.junit.jupiter.api.Test;

import static com.umair.aoc.y2022.Day25.decimalToSnafu;
import static  com.umair.aoc.y2022.Day25.snafuToDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Day25Test extends DayTestBase {

  @Test
  void testPart1_Example() {
    runExampleForDay(new Day25(), 1, 1, "2=-1=0");
  }

  @Test
  void testPart1_Full() {
    runForDay(new Day25(), 1, "20=2-02-0---02=22=21");
  }

  @Test
  void testPart2_Example() {
  }

  @Test
  void testPart2_Full() {
  }

  @Test
  void testSnafuToDecimal() {
    assertEquals(1, snafuToDecimal("1"));
    assertEquals(2, snafuToDecimal("2"));
    assertEquals(0, snafuToDecimal("0"));

    assertEquals(1747, snafuToDecimal("1=-0-2"));
    assertEquals(906, snafuToDecimal("12111"));
    assertEquals(198, snafuToDecimal("2=0="));
    assertEquals(11, snafuToDecimal("21"));
    assertEquals(201, snafuToDecimal("2=01"));
    assertEquals(31, snafuToDecimal("111"));
    assertEquals(1257, snafuToDecimal("20012"));
    assertEquals(32, snafuToDecimal("112"));
    assertEquals(353, snafuToDecimal("1=-1="));
    assertEquals(107, snafuToDecimal("1-12"));
    assertEquals(7, snafuToDecimal("12"));
    assertEquals(3, snafuToDecimal("1="));
    assertEquals(37, snafuToDecimal("122"));

    assertEquals(2022, snafuToDecimal("1=11-2"));
    assertEquals(12345, snafuToDecimal("1-0---0"));
    assertEquals(314159265, snafuToDecimal("1121-1110-1=0"));
  }

  @Test
  void testDecimalToDecimal() {
    assertEquals("0", decimalToSnafu(0));

    assertEquals("1", decimalToSnafu(1));
    assertEquals("2", decimalToSnafu(2));
    assertEquals("1=", decimalToSnafu(3));
    assertEquals("1-", decimalToSnafu(4));
    assertEquals("10", decimalToSnafu(5));
    assertEquals("11", decimalToSnafu(6));
    assertEquals("12", decimalToSnafu(7));
    assertEquals("2=", decimalToSnafu(8));
    assertEquals("2-", decimalToSnafu(9));
    assertEquals("20", decimalToSnafu(10));
    assertEquals("21", decimalToSnafu(11));

    assertEquals("1=-0-2", decimalToSnafu(1747));
    assertEquals("12111", decimalToSnafu(906));
    assertEquals("2=0=", decimalToSnafu(198));
    assertEquals("21", decimalToSnafu(11));
    assertEquals("2=01", decimalToSnafu(201));
    assertEquals("111", decimalToSnafu(31));
    assertEquals("20012", decimalToSnafu(1257));
    assertEquals("112", decimalToSnafu(32));
    assertEquals("1=-1=", decimalToSnafu(353));
    assertEquals("1-12", decimalToSnafu(107));
    assertEquals("12", decimalToSnafu(7));
    assertEquals("1=", decimalToSnafu(3));
    assertEquals("122", decimalToSnafu(37));

    assertEquals("1=11-2", decimalToSnafu(2022));
    assertEquals("1-0---0", decimalToSnafu(12345));
    assertEquals("1121-1110-1=0", decimalToSnafu(314159265));
  }
}
