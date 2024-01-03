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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Represents a range of points or an interval in a one-dimensional space.
 */
public class Range
implements Comparable<Range> {

  /**
   * Merge the ranges together such that they are all disjoint from each other. This method creates a new list and will
   * not modify its parameter. The resultant list will be sorted per Range's natural ordering. Merging ignores any null
   * elements in the list.
   *
   * @param the ranges to merge. This list will not be modified.
   * @return a new list of disjoint ranges representing the same overall ranges as the passed-in list. This will be a
   * new list.
   */
  public static List<Range> merge(final List<Range> ranges) {
    final List<Range> result = new ArrayList<>(ranges.size());
    ranges.stream().filter(r -> r != null).forEach(result::add);
    Collections.sort(result);
    boolean merged = true;
    while (merged) {
      merged = false;
      for (int i = 0; i < result.size() - 1; ++i) {
        final Range r1 = result.get(i);
        final Range r2 = result.get(i + 1);
        final Range r3 = r1.union(r2);
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

  private final int _x1;

  private final int _x2;

  private final int hashCode;

  private String toString;

  public Range(final String x1, final String x2) {
    this(Integer.parseInt(x1.trim()), Integer.parseInt(x2.trim()));
  }

  public Range(final int x1, final int x2) {
    if (x1 > x2) {
      throw new IllegalArgumentException("x1 (" + x1 + ") > x2 (" + x2 + ")");
    }
    _x1 = x1;
    _x2 = x2;
    hashCode = Integer.reverse(_x1) ^ _x2;
  }

  public int getX1() {
    return _x1;
  }

  public int getX2() {
    return _x2;
  }

  public boolean containsExclusive(final Range r) {
    return (_x1 <= r._x1) && (r._x2 < _x2);
  }

  public boolean containsInclusive(final Range r) {
    return (_x1 <= r._x1) && (r._x2 <= _x2);
  }

  public boolean containsExclusive(final int point) {
    return (_x1 <= point) && (point < _x2);
  }

  public boolean containsInclusive(final int point) {
    return (_x1 <= point) && (point <= _x2);
  }

  public boolean overlaps(final Range r) {
    return (_x1 <= r._x2) && (_x2 >= r._x1);
  }

  public Range union(final Range r) {
    if (overlaps(r) || (_x2 == r._x1 - 1) || (_x1 == r._x2 + 1)) {
      return new Range(Math.min(_x1, r._x1), Math.max(_x2, r._x2));
    }
    return null;
  }

  public Range intersection(final Range r) {
    int x1 = Math.max(_x1, r._x1);
    int x2 = Math.min(_x2, r._x2);
    if (x2 < x1) {
      return null;
    }
    return new Range(x1, x2);
  }

  public int[] getRangeXExclusive() {
    return IntStream.range(_x1, _x2).toArray();
  }

  public int[] getRangeXInclusive() {
    return IntStream.rangeClosed(_x1, _x2).toArray();
  }

  public int sizeExclusive() {
    return _x2 - _x1;
  }

  public int sizeInclusive() {
    return _x2 - _x1 + 1;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    else if (obj instanceof Range o) {
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
  public int compareTo(final Range o) {
    int comp = Integer.compare(_x1, o._x1);
    if (comp == 0) {
      comp = Integer.compare(_x2, o._x2);
    }
    return comp;
  }

}
