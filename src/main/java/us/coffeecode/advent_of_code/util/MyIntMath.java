/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2023  John Gaughan
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package us.coffeecode.advent_of_code.util;

import java.util.Arrays;
import java.util.Collection;

/**
 * Utility class that contains reused math functions that do not exist in core Java or in any of the libraries used by
 * this project.
 */
public final class MyIntMath {

  /** Constant for the value 0. */
  public static final Integer ZERO = Integer.valueOf(0);

  /** Constant for the value 1. */
  public static final Integer ONE = Integer.valueOf(1);

  /**
   * Get the signum of a value, indicating its sign. If negative, return -1. If positive, return 1. If zero, return 0.
   *
   * @param value the value to test.
   * @return its signum value.
   */
  public static int signum(final int value) {
    if (value < 0) {
      return -1;
    }
    else if (value > 0) {
      return 1;
    }
    return 0;
  }

  /**
   * Calculate the greatest common divisor of two non-negative integers using the binary GCD algorithm. This requires
   * non-negative because negative numbers can cause problems and generally aren't needed by AOC anyway.
   *
   * @param a first integer
   * @param b second integer
   * @return the GCD of the two integers.
   */
  public static int gcd(final int a, final int b) {
    if (a < 0) {
      throw new IllegalArgumentException("a < 0: " + a);
    }
    if (b < 0) {
      throw new IllegalArgumentException("b < 0: " + b);
    }
    if (a == 0) {
      return b;
    }
    if (b == 0) {
      return a;
    }

    final int i = Integer.numberOfTrailingZeros(a);
    final int j = Integer.numberOfTrailingZeros(b);
    final int k = Math.min(i, j);

    int _a = (a >> i);
    int _b = (b >> j);

    while (true) {
      if ((_a & 1) != 1) {
        throw new IllegalStateException();
      }
      if ((_b & 1) != 1) {
        throw new IllegalStateException();
      }
      if (_a > _b) {
        final int temp = _a;
        _a = _b;
        _b = temp;
      }

      _b -= _a;

      if (_b == 0) {
        return _a << k;
      }

      _b >>= Integer.numberOfTrailingZeros(_b);
    }
  }

  /**
   * Computes the least common multiple of two numbers. These numbers must not be negative, and at most one may be zero.
   *
   * @param a the first number.
   * @param b the second number.
   * @return the least common multiple of the two numbers.
   */
  public static int lcm(final int a, final int b) {
    // Division first to avoid overflow. Guaranteed not to lose precision because GCD.
    return a / gcd(a, b) * b;
  }

  /**
   * Computes the least common multiple of several numbers. These numbers must not be negative, and at most one may be
   * zero.
   *
   * @param values the numbers.
   * @return the least common multiple of the numbers.
   */
  public static int lcm(final int[] values) {
    return Arrays.stream(values)
                 .reduce(1, MyIntMath::lcm);
  }

  /**
   * Computes the least common multiple of several numbers. These numbers must not be negative, and at most one may be
   * zero.
   *
   * @param values the numbers.
   * @return the least common multiple of the numbers.
   */
  public static int lcm(final Collection<Integer> values) {
    return values.stream()
                 .mapToInt(Integer::intValue)
                 .reduce(1, MyIntMath::lcm);
  }

  /** The maximum input for the factorial function before it overflows an int. */
  private static final int MAX_FACTORIAL_INPUT = 12;

  /**
   * Calculate the factorial of the provided integer.
   *
   * @param value the input to the factorial.
   * @return the calculated factorial.
   * @throws ArithmeticException if the value is out of range.
   */
  public static int factorial(final int value) {
    if ((value > MAX_FACTORIAL_INPUT) || (value < 0)) {
      throw new ArithmeticException("Integer out of range");
    }
    int result = 1;
    for (int i = 1; i <= value; ++i) {
      result *= i;
    }
    return result;
  }
}
