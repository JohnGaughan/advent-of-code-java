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
  public static int[] reverse(final int[] array) {
    final int[] reversed = new int[array.length];
    final int end = array.length - 1;
    for (int i = 0; i < array.length; ++i) {
      reversed[end - i] = array[i];
    }
    return reversed;
  }

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
   * Reverse elements in the provided array in-place, modifying the array parameter. Only reverse elements between the
   * two bounds, where the start element is inclusive and the end element is exclusive.
   *
   * @param array the array to reverse.
   * @param start index of the first element to swap.
   * @param end one past the final element's index.
   */
  public static void reverseInPlace(final int[] array, final int start, final int end) {
    final int[] reversed = new int[end - start];
    for (int i = 0; i < reversed.length; ++i) {
      reversed[reversed.length - i - 1] = array[start + i];
    }
    System.arraycopy(reversed, 0, array, start, reversed.length);
  }

  /**
   * Reverse elements in the provided array in-place, modifying the array parameter. Only reverse elements between the
   * two bounds, where the start element is inclusive and the end element is exclusive.
   *
   * @param array the array to reverse.
   * @param start index of the first element to swap.
   * @param end one past the final element's index.
   */
  public static void reverseInPlace(final long[] array, final int start, final int end) {
    final long[] reversed = new long[end - start];
    for (int i = 0; i < reversed.length; ++i) {
      reversed[reversed.length - i - 1] = array[start + i];
    }
    System.arraycopy(reversed, 0, array, start, reversed.length);
  }

  /**
   * Make a deep copy of the provided two-dimensional array.
   *
   * @param array the 2D array to copy.
   * @return a new deep copy of the array.
   */
  public static int[][] deepCopyOf(final int[][] array) {
    final int[][] result = new int[array.length][];
    for (int i = 0; i < result.length; ++i) {
      result[i] = Arrays.copyOf(array[i], array[i].length);
    }
    return result;
  }

  /**
   * Make a deep copy of the provided two-dimensional array.
   *
   * @param array the 2D array to copy.
   * @return a new deep copy of the array.
   */
  public static long[][] deepCopyOf(final long[][] array) {
    final long[][] result = new long[array.length][];
    for (int i = 0; i < result.length; ++i) {
      result[i] = Arrays.copyOf(array[i], array[i].length);
    }
    return result;
  }

  /**
   * Make a copy of an array, omitting a specific index.
   *
   * @param array the array to copy.
   * @param index the index of the element to omit.
   * @return a copy of the array, minus the specified element.
   */
  public static int[] copyOmitting(final int[] array, final int index) {
    if (array == null) {
      throw new IllegalArgumentException("Null array");
    }
    else if (array.length == 0) {
      throw new IllegalArgumentException("Array length zero: cannot reduce length");
    }
    else if ((index < 0) || (index >= array.length)) {
      throw new IllegalArgumentException("Index " + index + " not valid for array of length " + array.length);
    }
    final int[] newArray = new int[array.length - 1];
    System.arraycopy(array, 0, newArray, 0, index);
    System.arraycopy(array, index + 1, newArray, index, newArray.length - index);
    return newArray;
  }

  /**
   * Make a copy of an array, omitting a specific index.
   *
   * @param array the array to copy.
   * @param index the index of the element to omit.
   * @return a copy of the array, minus the specified element.
   */
  public static long[] copyOmitting(final long[] array, final int index) {
    if (array == null) {
      throw new IllegalArgumentException("Null array");
    }
    else if (array.length == 0) {
      throw new IllegalArgumentException("Array length zero: cannot reduce length");
    }
    else if ((index < 0) || (index >= array.length)) {
      throw new IllegalArgumentException("Index " + index + " not valid for array of length " + array.length);
    }
    final long[] newArray = new long[array.length - 1];
    System.arraycopy(array, 0, newArray, 0, index);
    System.arraycopy(array, index + 1, newArray, index, newArray.length - index);
    return newArray;
  }
}
