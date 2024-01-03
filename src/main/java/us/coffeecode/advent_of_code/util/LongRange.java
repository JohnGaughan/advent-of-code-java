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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.LongStream;

/**
 * Represents a range of points or an interval in a one-dimensional space. Values are represented as long integers, for
 * when range values are not used to index into arrays or work with points that also may reference arrays.
 */
public class LongRange
implements Comparable<LongRange> {

  /**
   * Merge the ranges together such that they are all disjoint from each other. This method creates a new list and will
   * not modify its parameter. The resultant list will be sorted per Range's natural ordering. Merging ignores any null
   * elements in the list.
   *
   * @param the ranges to merge. This list will not be modified.
   * @return a new list of disjoint ranges representing the same overall ranges as the passed-in list. This will be a
   * new list.
   */
  public static List<LongRange> merge(final Collection<LongRange> ranges) {
    final List<LongRange> result = new ArrayList<>(ranges.size());
    ranges.stream().filter(r -> r != null).forEach(result::add);
    Collections.sort(result);
    boolean merged = true;
    while (merged) {
      merged = false;
      for (int i = 0; i < result.size() - 1; ++i) {
        final LongRange r1 = result.get(i);
        final LongRange r2 = result.get(i + 1);
        final LongRange r3 = r1.union(r2);
        if (r3 != null) {
          result.remove(i);
          result.remove(i);
          result.add(i, r3);
          merged = true;
          break;
        }
      }
    }
    return result;
  }

  private final long _x1;

  private final long _x2;

  private final int hashCode;

  private String toString;

  public LongRange(final String x1, final String x2) {
    this(Long.parseLong(x1.trim()), Long.parseLong(x2.trim()));
  }

  public LongRange(final long x1, final long x2) {
    if (x1 > x2) {
      throw new IllegalArgumentException("x2 (" + x2 + ") > x1 (" + x1 + ")");
    }
    _x1 = x1;
    _x2 = x2;
    hashCode = Objects.hash(Long.valueOf(Long.reverse(_x1)), Long.valueOf(_x2));
  }

  public long getX1() {
    return _x1;
  }

  public long getX2() {
    return _x2;
  }

  public boolean containsExclusive(final LongRange r) {
    return (_x1 <= r._x1) && (r._x2 < _x2);
  }

  public boolean containsInclusive(final LongRange r) {
    return (_x1 <= r._x1) && (r._x2 <= _x2);
  }

  public boolean containsExclusive(final long point) {
    return (_x1 <= point) && (point < _x2);
  }

  public boolean containsInclusive(final long point) {
    return (_x1 <= point) && (point <= _x2);
  }

  public boolean overlaps(final LongRange r) {
    return (_x1 <= r._x2) && (_x2 >= r._x1);
  }

  public LongRange union(final LongRange r) {
    if (overlaps(r) || (_x2 == r._x1 - 1) || (_x1 == r._x2 + 1)) {
      return new LongRange(Math.min(_x1, r._x1), Math.max(_x2, r._x2));
    }
    return null;
  }

  public LongRange intersection(final LongRange r) {
    long x1 = Math.max(_x1, r._x1);
    long x2 = Math.min(_x2, r._x2);
    if (x2 < x1) {
      return null;
    }
    return new LongRange(x1, x2);
  }

  public long[] getRangeXExclusive() {
    return LongStream.range(_x1, _x2).toArray();
  }

  public long[] getRangeXInclusive() {
    return LongStream.rangeClosed(_x1, _x2).toArray();
  }

  public long sizeExclusive() {
    return _x2 - _x1;
  }

  public long sizeInclusive() {
    return _x2 - _x1 + 1;
  }

  /** Shift this range up or down by the specified amount. */
  public LongRange shift(final long shift) {
    return new LongRange(_x1 + shift, _x2 + shift);
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    else if (obj instanceof LongRange o) {
      return (_x1 == o._x1) && (_x2 == o._x2);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return hashCode;
  }

  @Override
  public String toString() {
    if (toString == null) {
      toString = "(" + _x1 + " -> " + _x2 + ")";
    }
    return toString;
  }

  @Override
  public int compareTo(final LongRange o) {
    int comp = Long.compare(_x1, o._x1);
    if (comp == 0) {
      comp = Long.compare(_x2, o._x2);
    }
    return comp;
  }

}
