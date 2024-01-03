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
import java.util.NoSuchElementException;

import org.springframework.stereotype.Component;

/**
 * Queue of primitive longs, designed for use with IntCode programs.
 */
@Component
final class LongQueue
implements IntCodeIoQueue {

  private static final int DEFAULT_ARRAY_LENGTH = 0x10;

  private long[] array;

  private int head;

  private int size;

  public LongQueue() {
    array = new long[DEFAULT_ARRAY_LENGTH];
    head = 0;
    size = 0;
  }

  public LongQueue(final long value) {
    array = new long[DEFAULT_ARRAY_LENGTH];
    head = 0;
    size = 1;
  }

  public LongQueue(final long[] values) {
    int length = DEFAULT_ARRAY_LENGTH;
    while (length < values.length) {
      length <<= 1;
    }
    array = new long[length];
    head = 0;
    size = values.length;
    System.arraycopy(values, 0, array, 0, values.length);
  }

  @Override
  public void add(final long value) {
    ensureCapacity(size + 1);
    final int tail = (head + size) % array.length;
    array[tail] = value;
    ++size;
  }

  @Override
  public void add(final long[] values) {
    ensureCapacity(size + values.length);
    Arrays.stream(values).forEach(l -> add(l));
  }

  @Override
  public long remove() {
    if (size == 0) {
      throw new NoSuchElementException();
    }
    final long value = array[head];
    head = (head + 1) % array.length;
    --size;
    return value;
  }

  @Override
  public long[] remove(final int quantity) {
    if (size < quantity) {
      throw new NoSuchElementException();
    }
    final long[] removed = new long[quantity];
    final int length1 = Math.min(array.length - head, size);
    System.arraycopy(array, head, removed, 0, length1);
    if (length1 < size) {
      System.arraycopy(array, 0, removed, length1, size - length1);
    }
    size -= quantity;
    head = (head + quantity) % array.length;
    return removed;
  }

  @Override
  public long[] removeAll() {
    return remove(size);
  }

  @Override
  public void clear() {
    head = size = 0;
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public boolean isEmpty() {
    return size == 0;
  }

  private void ensureCapacity(final int capacity) {
    if (array.length < capacity) {
      int newCapacity = array.length;
      while (newCapacity < capacity) {
        newCapacity <<= 1;
      }
      final long[] newArray = new long[newCapacity];
      // The queue might wrap around the end of the array to the beginning, which would insert a ton of zeros into the
      // middle. To be safe, move the queue to the front of the array.
      final int length1 = Math.min(array.length - head, size);
      System.arraycopy(array, head, newArray, 0, length1);
      if (length1 < size) {
        System.arraycopy(array, 0, newArray, length1, size - length1);
      }
      array = newArray;
    }
  }

  @Override
  public String toString() {
    final StringBuilder str = new StringBuilder(size << 3);
    str.append("[");
    for (int i = head; i < head + size && i < array.length; ++i) {
      if (!str.isEmpty()) {
        str.append(", ");
      }
      str.append(array[i]);
    }
    for (int i = 0; i < head + size - array.length; ++i) {
      if (!str.isEmpty()) {
        str.append(", ");
      }
      str.append(array[i]);
    }
    str.append("]");
    return str.toString();
  }

}
