/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2021  John Gaughan
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

import java.util.stream.IntStream;

/**
 * Represents a range of points or an interval in a three-dimensional space.
 */
public class Range3D {

  private final int _x1;

  private final int _y1;

  private final int _z1;

  private final int _x2;

  private final int _y2;

  private final int _z2;

  private final int hashCode;

  private String toString;

  public Range3D(final int[] xyz1_xyz2) {
    this(xyz1_xyz2[0], xyz1_xyz2[1], xyz1_xyz2[2], xyz1_xyz2[3], xyz1_xyz2[4], xyz1_xyz2[5]);
  }

  public Range3D(final int x1, final int y1, final int z1, final int x2, final int y2, final int z2) {
    if (x1 > x2) {
      throw new IllegalArgumentException("x1 (" + x1 + ") > x2 (" + x2 + ")");
    }
    if (y1 > y2) {
      throw new IllegalArgumentException("y1 (" + y1 + ") > y2 (" + y2 + ")");
    }
    if (z1 > z2) {
      throw new IllegalArgumentException("z1 (" + z1 + ") > z2 (" + z2 + ")");
    }
    _x1 = x1;
    _y1 = y1;
    _z1 = z1;
    _x2 = x2;
    _y2 = y2;
    _z2 = z2;
    hashCode = Integer.reverse(_x1) ^ _y1 ^ Integer.reverse(_y2) ^ _x2 ^ Integer.reverse(_z2) ^ _z1;
  }

  public int getX1() {
    return _x1;
  }

  public int getY1() {
    return _y1;
  }

  public int getZ1() {
    return _z1;
  }

  public int getX2() {
    return _x2;
  }

  public int getY2() {
    return _y2;
  }

  public int getZ2() {
    return _z2;
  }

  public boolean containsExclusive(final Point3D point) {
    return (_x1 <= point.getX()) && (point.getX() < _x2) && (_y1 <= point.getY()) && (point.getY() < _y2) && (_z1 <= point.getZ())
      && (point.getZ() < _z2);
  }

  public boolean containsInclusive(final Point3D point) {
    return (_x1 <= point.getX()) && (point.getX() <= _x2) && (_y1 <= point.getY()) && (point.getY() <= _y2)
      && (_z1 <= point.getZ()) && (point.getZ() <= _z2);
  }

  public boolean containsExclusive(final MutablePoint3D point) {
    return (_x1 <= point.getX()) && (point.getX() < _x2) && (_y1 <= point.getY()) && (point.getY() < _y2) && (_z1 <= point.getZ())
      && (point.getZ() < _z2);
  }

  public boolean containsInclusive(final MutablePoint3D point) {
    return (_x1 <= point.getX()) && (point.getX() <= _x2) && (_y1 <= point.getY()) && (point.getY() <= _y2)
      && (_z1 <= point.getZ()) && (point.getZ() <= _z2);
  }

  public boolean overlaps(final Range3D r) {
    boolean x = (_x1 <= r._x2) && (_x2 >= r._x1);
    boolean y = (_y1 <= r._y2) && (_y2 >= r._y1);
    boolean z = (_z1 <= r._z2) && (_z2 >= r._z1);
    return x && y && z;
  }

  public boolean overlapsX(final Range3D r) {
    // TODO: write unit test
    return (_x1 <= r._x2) && (_x2 >= r._x1);
  }

  public boolean overlapsY(final Range3D r) {
    // TODO: write unit test
    return (_y1 <= r._y2) && (_y2 >= r._y1);
  }

  public boolean overlapsZ(final Range3D r) {
    // TODO: write unit test
    return (_z1 <= r._z2) && (_z2 >= r._z1);
  }

  public Range3D intersection(final Range3D r) {
    int x1 = Math.max(_x1, r._x1);
    int x2 = Math.min(_x2, r._x2);
    int y1 = Math.max(_y1, r._y1);
    int y2 = Math.min(_y2, r._y2);
    int z1 = Math.max(_z1, r._z1);
    int z2 = Math.min(_z2, r._z2);
    if ((x2 < x1) || (y2 < y1) || (z2 < z1)) {
      return null;
    }
    return new Range3D(x1, y1, z1, x2, y2, z2);
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

  public int[] getRangeZExclusive() {
    return IntStream.range(_z1, _z2).toArray();
  }

  public int[] getRangeZInclusive() {
    return IntStream.rangeClosed(_z1, _z2).toArray();
  }

  public long sizeExclusive() {
    return (long) (_x2 - _x1) * (_y2 - _y1) * (_z2 - _z1);
  }

  public long sizeInclusive() {
    return (long) (_x2 - _x1 + 1) * (_y2 - _y1 + 1) * (_z2 - _z1 + 1);
  }

  public Range3D shiftX(final int dx) {
    // TODO: write unit test
    return new Range3D(_x1 + dx, _y1, _z1, _x2 + dx, _y2, _z2);
  }

  public Range3D shiftY(final int dy) {
    // TODO: write unit test
    return new Range3D(_x1, _y1 + dy, _z1, _x2, _y2 + dy, _z2);
  }

  public Range3D shiftZ(final int dz) {
    // TODO: write unit test
    return new Range3D(_x1, _y1, _z1 + dz, _x2, _y2, _z2 + dz);
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    else if (obj instanceof Range3D o) {
      return (_x1 == o._x1) && (_y1 == o._y1) && (_z1 == o._z1) && (_x2 == o._x2) && (_y2 == o._y2) && (_z2 == o._z2);
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
      toString = "((" + _x1 + "," + _y1 + "," + _z1 + ")->(" + _x2 + "," + _y2 + "," + _z2 + "))";
    }
    return toString;
  }

}
