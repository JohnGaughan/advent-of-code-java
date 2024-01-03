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

/**
 * Utility functions for arrays that are not included in core Java.
 */
public final class MyArrays {

  /**
   * Reverse the provided array, returning the reversed array while preserving the input array which is not modified.
   *
   * @param array the array to reverse.
   * @return a new array which is the input array, reversed.
   */
  public static long[] reverse(final long[] array) {
    final long[] reversed = new long[array.length];
    final int end = array.length - 1;
    for (int i = 0; i < array.length; ++i) {
      reversed[end - i] = array[i];
    }
    return reversed;
  }

  /**
   * Make a deep copy of the provided two-dimensional array.
   *
   * @param array the 2D array to copy.
   * @return a new deep copy of the array.
   */
  public static int[][] copy(final int[][] array) {
    final int[][] result = new int[array.length][];
    for (int i = 0; i < result.length; ++i) {
      result[i] = Arrays.copyOf(array[i], array[i].length);
    }
    return result;
  }
}
