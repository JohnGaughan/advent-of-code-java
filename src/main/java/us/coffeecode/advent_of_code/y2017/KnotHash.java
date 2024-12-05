/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2020  John Gaughan
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
package us.coffeecode.advent_of_code.y2017;

import org.springframework.stereotype.Component;

/**
 * Implementation of the knot hash introduced in day 10 and used in days 10 and 14. There are two steps to the hash
 * function. First, we create a buffer containing integers from 0 to 255 and mix them up based on values in the input
 * string. Next, we combine them into groups using XOR.
 */
@Component
final class KnotHash {

  private static final int DENSE_SIZE = 16;

  private static final int LIST_SIZE = 256;

  private static final int[] SUFFIX = new int[] { 17, 31, 73, 47, 23 };

  public int[] hashOneRound(final int[] lengths) {
    final int[] array = createState();
    hash(array, lengths, 1);
    return array;
  }

  public int[] hash(final String input) {
    return hash(input.codePoints()
                     .toArray());
  }

  private int[] hash(final int[] input) {
    final int[] result = new int[input.length + SUFFIX.length];
    System.arraycopy(input, 0, result, 0, input.length);
    System.arraycopy(SUFFIX, 0, result, input.length, SUFFIX.length);
    final int[] array = createState();
    hash(array, result, 64);
    return condense(array);
  }

  private void hash(final int[] array, final int[] lengths, final int rounds) {
    int skip = 0;
    int position = 0;
    for (int i = 0; i < rounds; ++i) {
      for (int length : lengths) {
        twist(array, length, position, skip);
        position = position + length + skip;
        position %= array.length;
        ++skip;
      }
    }
  }

  /** Perform one twist operation, reversing a portion of the array. */
  private void twist(final int[] array, final int length, final int position, final int skip) {
    for (int i = 0; i < length / 2; ++i) {
      final int i1 = (position + i) % array.length;
      final int i2 = (position + length - 1 - i) % array.length;
      final int swap = array[i1];
      array[i1] = array[i2];
      array[i2] = swap;
    }
  }

  /** Get a new dense array from the sparse hash. */
  private int[] condense(final int[] sparseHash) {
    final int[] dense = new int[DENSE_SIZE];
    final int blockSize = sparseHash.length / dense.length;
    for (int i = 0; i < sparseHash.length; ++i) {
      dense[i / blockSize] ^= sparseHash[i];
    }
    return dense;
  }

  /** Create the initial state of the buffer used by the hash function. */
  private int[] createState() {
    final int[] array = new int[LIST_SIZE];
    for (int i = 0; i < array.length; ++i) {
      array[i] = i;
    }
    return array;
  }

}
