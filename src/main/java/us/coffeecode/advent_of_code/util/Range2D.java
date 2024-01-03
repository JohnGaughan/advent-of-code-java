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

import java.util.Collection;
import java.util.stream.IntStream;

/**
 * Represents a range of points or an interval in a two-dimensional space.
 */
public class Range2D {

  private final int _x1;

  private final int _y1;

  private final int _x2;

  private final int _y2;

  private final int hashCode;

  private String toString;

  public Range2D(final int x1, final int y1, final int x2, final int y2) {
    if (x1 > x2) {
      throw new IllegalArgumentException("x1 (" + x1 + ") > x2 (" + x2 + ")");
    }
    if (y1 > y2) {
      throw new IllegalArgumentException("y1 (" + y1 + ") > y2 (" + y2 + ")");
    }
    _x1 = x1;
    _y1 = y1;
    _x2 = x2;
    _y2 = y2;
    hashCode = Integer.reverse(_x1) ^ _y1 ^ Integer.reverse(_y2) ^ _x2;
  }

  public int getX1() {
    return _x1;
  }

  public int getY1() {
    return _y1;
  }

  public int getX2() {
    return _x2;
  }

  public int getY2() {
    return _y2;
  }

  public boolean containsExclusive(final Point2D point) {
    return (_x1 <= point.getX()) && (point.getX() < _x2) && (_y1 <= point.getY()) && (point.getY() < _y2);
  }

  public boolean containsInclusive(final Point2D point) {
    return (_x1 <= point.getX()) && (point.getX() <= _x2) && (_y1 <= point.getY()) && (point.getY() <= _y2);
  }

  public boolean containsExclusive(final MutablePoint2D point) {
    return (_x1 <= point.getX()) && (point.getX() < _x2) && (_y1 <= point.getY()) && (point.getY() < _y2);
  }

  public boolean containsInclusive(final MutablePoint2D point) {
    return (_x1 <= point.getX()) && (point.getX() <= _x2) && (_y1 <= point.getY()) && (point.getY() <= _y2);
  }

  public boolean containsAnyExclusive(final Collection<Point2D> points) {
    return points.stream().anyMatch(p -> containsExclusive(p));
  }

  public boolean containsAnyInclusive(final Collection<Point2D> points) {
    return points.stream().anyMatch(p -> containsInclusive(p));
  }

  public boolean overlaps(final Range2D r) {
    boolean x = (_x1 <= r._x2) && (_x2 >= r._x1);
    boolean y = (_y1 <= r._y2) && (_y2 >= r._y1);
    return x && y;
  }

  public Range2D intersection(final Range2D r) {
    int x1 = Math.max(_x1, r._x1);
    int x2 = Math.min(_x2, r._x2);
    int y1 = Math.max(_y1, r._y1);
    int y2 = Math.min(_y2, r._y2);
    if ((x2 < x1) || (y2 < y1)) {
      return null;
    }
    return new Range2D(x1, y1, x2, y2);
  }

  public int[] getRangeXExclusive() {
    return IntStream.range(_x1, _x2).toArray();
  }

  public int[] getRangeXInclusive() {
    return IntStream.rangeClosed(_x1, _x2).toArray();
  }

  public int[] getRangeYExclusive() {
    return IntStream.range(_y1, _y2).toArray();
  }

  public int[] getRangeYInclusive() {
    return IntStream.rangeClosed(_y1, _y2).toArray();
  }

  public long sizeExclusive() {
    return (long) (_x2 - _x1) * (_y2 - _y1);
  }

  public long sizeInclusive() {
    return (long) (_x2 - _x1 + 1) * (_y2 - _y1 + 1);
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    else if (obj instanceof Range2D o) {
      return (_x1 == o._x1) && (_y1 == o._y1) && (_x2 == o._x2) && (_y2 == o._y2);
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
      toString = "((" + _x1 + "," + _y1 + ")->(" + _x2 + "," + _y2 + "))";
    }
    return toString;
  }

}
