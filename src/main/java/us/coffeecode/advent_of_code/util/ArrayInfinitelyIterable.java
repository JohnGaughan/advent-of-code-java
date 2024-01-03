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
package us.coffeecode.advent_of_code.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

/**
 * Iterable implementation that wraps an array and circles around, infinitely. As long as the iterated array has at
 * least one element, there will always be a next element to get. This functionality is useful when a problem says to
 * use values as input and to wrap around back to the start when exhausted.<br>
 * <br>
 * If one needs to process primitive integers, {@link CircularIntBuffer} is the alternative.
 */
public class ArrayInfinitelyIterable<T>
implements Iterable<T> {

  final T[] array;

  int next = 0;

  /**
   * Constructs a {@code ArrayInfinitelyIterable}. The array must not be null although it can be empty.
   */
  public ArrayInfinitelyIterable(final T[] _array) {
    if (_array == null) {
      throw new NullPointerException("Array is null");
    }
    array = _array;
  }

  @Override
  public Iterator<T> iterator() {
    return new InfiniteIterator();
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    else if (obj instanceof ArrayInfinitelyIterable<?> o) {
      return (next == o.next) && Arrays.deepEquals(array, o.array);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(Integer.valueOf(next), Integer.valueOf(Arrays.deepHashCode(array)));
  }

  @Override
  public String toString() {
    return next + Arrays.toString(array);
  }

  /** Iterator implementation that works with this class. */
  private class InfiniteIterator
  implements Iterator<T> {

    @Override
    public boolean hasNext() {
      return array.length > 0;
    }

    @Override
    public T next() {
      if (next == array.length) {
        next = 0;
      }
      return array[next++];
    }
  }
}
