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
package us.coffeecode.advent_of_code.y2019;

import java.util.Arrays;

/**
 * Array of longs that dynamically resizes itself. Built for IntCode: it accepts long indices since indices come from
 * the array of longs itself. Can return values outside of the array, which will be zero. Dynamically resizes itself as
 * needed.
 */
final class DynamicLongArray {

  private long[] array;

  public DynamicLongArray(final long[] input) {
    array = Arrays.copyOf(input, input.length);
  }

  public DynamicLongArray(final DynamicLongArray original) {
    array = new long[original.array.length];
    System.arraycopy(original.array, 0, array, 0, array.length);
  }

  public long getFirst() {
    return (array.length == 0) ? 0 : array[0];
  }

  public long get(final long location) {
    if (location >= array.length) {
      return 0;
    }
    return array[(int) location];
  }

  public void set(final long location, final long value) {
    if (location >= array.length) {
      resize(location);
    }
    array[(int) location] = value;
  }

  private void resize(final long minSize) {
    final long minimum = 1 + Math.max(minSize, array.length);
    assert minimum <= Integer.MAX_VALUE;
    int newSize = 1 << 8;
    while (newSize < minimum) {
      newSize <<= 1;
    }
    assert 0 < minimum;
    final long[] newArray = new long[newSize];
    System.arraycopy(array, 0, newArray, 0, array.length);
    array = newArray;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    else if (obj instanceof DynamicLongArray) {
      // Good enough
      return hashCode() == obj.hashCode();
    }
    return false;
  }

  @Override
  public int hashCode() {
    // Only hash up to the final non-zero value, since trailing zeros do not matter.
    int end = array.length - 1;
    while (array[end] == 0) {
      --end;
    }
    int hash = 27_259;
    for (int i = 0; i <= end; ++i) {
      hash = 4_053_691 * hash + Long.hashCode(array[i]);
    }
    return hash;
  }

  @Override
  public String toString() {
    return Arrays.toString(array);
  }

}
