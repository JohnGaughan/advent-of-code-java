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

import com.google.common.math.LongMath;

/**
 * Utility class that contains reused math functions that do not exist in core Java or in any of the libraries used by
 * this project.
 */
public final class MyLongMath {

  /**
   * Computes the least common multiple of two numbers. These numbers must not be negative, and at most one may be zero.
   *
   * @param a the first number.
   * @param b the second number.
   * @return the least common multiple of the two numbers.
   */
  public static long lcm(final long a, final long b) {
    // Division first to avoid overflow. Guaranteed not to lose precision because GCD.
    return a / LongMath.gcd(a, b) * b;
  }

  /**
   * Computes the least common multiple of several numbers. These numbers must not be negative, and at most one may be
   * zero.
   *
   * @param values the numbers.
   * @return the least common multiple of the numbers.
   */
  public static long lcm(final long[] values) {
    return Arrays.stream(values).reduce(1, MyLongMath::lcm);
  }

  /**
   * Computes the least common multiple of several numbers. These numbers must not be negative, and at most one may be
   * zero.
   *
   * @param values the numbers.
   * @return the least common multiple of the numbers.
   */
  public static long lcm(final Collection<Long> values) {
    return values.stream().mapToLong(Long::longValue).reduce(1, MyLongMath::lcm);
  }

}
