/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2022  John Gaughan
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
import java.util.Objects;

/**
 * This is a circular buffer over integers. Users can get the next element, which will always return: once the array is
 * fully returned, this class will start over at the beginning of the array.<br>
 * <br>
 * This is similar to {@link ArrayInfinitelyIterable} except it uses primitives, meaning it cannot use the
 * {@link java.util.Iterator} interface and cannot be used in enhanced {@code for} loops.
 */
public final class CircularIntBuffer {

  private final int[] buffer;

  int pointer = 0;

  /**
   * Constructs a {@code CircularIntBuffer} from an integer array. This array cannot be null and must contain a minimum
   * of one element.
   */
  public CircularIntBuffer(final int[] array) {
    if (array == null) {
      throw new NullPointerException("Array is null");
    }
    if (array.length == 0) {
      throw new IllegalArgumentException("Zero length array");
    }
    buffer = new int[array.length];
    System.arraycopy(array, 0, buffer, 0, array.length);
  }

  /**
   * Constructs a {@code CircularIntBuffer} from the code points contained in a string. This string cannot be null and
   * must contain at least one code point.
   */
  public CircularIntBuffer(final String str) {
    if (str == null) {
      throw new NullPointerException("String is null");
    }
    if (str.length() == 0) {
      throw new IllegalArgumentException("Zero length string");
    }
    buffer = str.codePoints().toArray();
  }

  /**
   * Get the next element in the circular buffer. This is guaranteed to return an element without throwing an exception.
   */
  public int next() {
    final int element = buffer[pointer];
    ++pointer;
    if (pointer == buffer.length) {
      pointer = 0;
    }
    return element;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    else if (obj instanceof CircularIntBuffer o) {
      return (pointer == o.pointer) && Arrays.equals(buffer, o.buffer);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(Integer.valueOf(pointer), buffer);
  }

  @Override
  public String toString() {
    final StringBuilder str = new StringBuilder(buffer.length * 2 + 10);
    str.append("[").append(pointer).append(", ").append(Arrays.toString(buffer)).append("]");
    return str.toString();
  }
}
