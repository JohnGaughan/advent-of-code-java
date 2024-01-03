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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Represents a point in 3D space that is immutable.
 */
public final class Point3D
implements Comparable<Point3D> {

  /**
   * Construct a Point3D from a string containing a comma-delimited set of coordinates.
   */
  public static Point3D valueOf(final String s) {
    final int[] parts = Arrays.stream(SPLIT.split(s.trim())).mapToInt(Integer::parseInt).toArray();
    return new Point3D(parts[0], parts[1], parts[2]);
  }

  private static final Pattern SPLIT = Pattern.compile(",");

  /** The origin point, at (0, 0, 0). */
  public static final Point3D ORIGIN = new Point3D(0, 0, 0);

  private final int _x;

  private final int _y;

  private final int _z;

  private final int hashCode;

  private String toString;

  /**
   * Construct a Point3D from an array with three elements where element zero is X, element one is Y, and element two is
   * Z.
   */
  public Point3D(final int[] array) {
    this(array[0], array[1], array[2]);
  }

  /** Construct a Point3D by adding deltas to the dimensions of another Point3D. */
  public Point3D(final Point3D other, final int dx, final int dy, final int dz) {
    this(other._x + dx, other._y + dy, other._z + dz);
  }

  /** Construct a Point3D from a Point2D, with a Z coordinate of zero. */
  public Point3D(final Point2D other) {
    this(other.getX(), other.getY(), 0);
  }

  /** Construct a Point3D from a Point2D, using the specified Z coordinate. */
  public Point3D(final Point2D other, final int z) {
    this(other.getX(), other.getY(), z);
  }

  /** Construct a Point3D from raw X, Y, and Z coordinates. */
  public Point3D(final int x, final int y, final int z) {
    _x = x;
    _y = y;
    _z = z;
    hashCode = Integer.reverse(_x) ^ _y ^ Integer.reverseBytes(_z);
  }

  /** Get the X value. */
  public int getX() {
    return _x;
  }

  /** Get the Y value. */
  public int getY() {
    return _y;
  }

  /** Get the Z value. */
  public int getZ() {
    return _z;
  }

  /** Get all non-diagonal neighbors one unit away from this point. */
  public List<Point3D> getNeighbors() {
    final List<Point3D> neighbors = new ArrayList<>(6);
    neighbors.add(new Point3D(_x + 1, _y, _z));
    neighbors.add(new Point3D(_x - 1, _y, _z));
    neighbors.add(new Point3D(_x, _y + 1, _z));
    neighbors.add(new Point3D(_x, _y - 1, _z));
    neighbors.add(new Point3D(_x, _y, _z + 1));
    neighbors.add(new Point3D(_x, _y, _z - 1));
    return neighbors;
  }

  /** Get the value in a map cell corresponding to this point. */
  public boolean get(final boolean[][][] map) {
    return map[_z][_y][_x];
  }

  /** Set the value in a map cell corresponding to this point. */
  public void set(final boolean[][][] map, final boolean value) {
    map[_z][_y][_x] = value;
  }

  /** Get the value in a map cell corresponding to this point. */
  public int get(final int[][][] map) {
    return map[_z][_y][_x];
  }

  /** Set the value in a map cell corresponding to this point. */
  public void set(final int[][][] map, final int value) {
    map[_z][_y][_x] = value;
  }

  /** Get the value in a map cell corresponding to this point. */
  public long get(final long[][][] map) {
    return map[_z][_y][_x];
  }

  /** Set the value in a map cell corresponding to this point. */
  public void set(final long[][][] map, final long value) {
    map[_z][_y][_x] = value;
  }

  /** Get the value in a map cell corresponding to this point. */
  public <T> T get(final T[][][] map) {
    return map[_z][_y][_x];
  }

  /** Set the value in a map cell corresponding to this point. */
  public <T> void set(final T[][][] map, final T value) {
    map[_z][_y][_x] = value;
  }

  /** Get the Manhattan distance to origin. */
  public int getManhattanDistance() {
    return Math.abs(_x) + Math.abs(_y) + Math.abs(_z);
  }

  /** Get the Manhattan distance to another point. */
  public int getManhattanDistance(final Point3D other) {
    return Math.abs(_x - other._x) + Math.abs(_y - other._y) + Math.abs(_z - other._z);
  }

  /** Get whether this point is at origin, the center of space. */
  public boolean isOrigin() {
    return (_x == 0) && (_y == 0) && (_z == 0);
  }

  /** Get whether this point exists inside the map. */
  public boolean isIn(final boolean[][][] map) {
    if ((_x < 0) || (_y < 0) || (_z < 0)) {
      return false;
    }
    return (_z < map.length) && (_y < map[_z].length) && (_x < map[_z][_y].length);
  }

  /** Get whether this point exists inside the map. */
  public boolean isIn(final int[][][] map) {
    if ((_x < 0) || (_y < 0) || (_z < 0)) {
      return false;
    }
    return (_z < map.length) && (_y < map[_z].length) && (_x < map[_z][_y].length);
  }

  /** Get whether this point exists inside the map. */
  public boolean isIn(final long[][][] map) {
    if ((_x < 0) || (_y < 0) || (_z < 0)) {
      return false;
    }
    return (_z < map.length) && (_y < map[_z].length) && (_x < map[_z][_y].length);
  }

  /** Get whether this point exists inside the map. */
  public <T> boolean isIn(final T[][][] map) {
    if ((_x < 0) || (_y < 0) || (_z < 0)) {
      return false;
    }
    return (_z < map.length) && (_y < map[_z].length) && (_x < map[_z][_y].length);
  }

  /** Add the coordinates of this point with the given point. */
  public Point3D add(final Point3D p) {
    return new Point3D(_x + p._x, _y + p._y, _z + p._z);
  }

  /** Subtract the coordinates of the given point from this point. */
  public Point3D subtract(final Point3D p) {
    return new Point3D(_x - p._x, _y - p._y, _z - p._z);
  }

  /** Create a new point whose coordinates are the opposite of the current point. */
  public Point3D invert() {
    return new Point3D(-_x, -_y, -_z);
  }

  @Override
  public int compareTo(final Point3D o) {
    final int resultZ = Integer.compare(_z, o._z);
    if (resultZ != 0) {
      return resultZ;
    }
    final int resultY = Integer.compare(_y, o._y);
    if (resultY != 0) {
      return resultY;
    }
    return Integer.compare(_x, o._x);
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    else if (obj instanceof Point3D o) {
      return (_x == o._x) && (_y == o._y) && (_z == o._z);
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
      toString = "(" + _x + "," + _y + "," + _z + ")";
    }
    return toString;
  }

}
