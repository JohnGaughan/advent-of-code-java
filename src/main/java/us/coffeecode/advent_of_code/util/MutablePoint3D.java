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
import java.util.List;

/**
 * Represents a point in 3D space that can be modified.
 */
public class MutablePoint3D
implements Comparable<MutablePoint3D> {

  private int _x;

  private int _y;

  private int _z;

  public MutablePoint3D(final int[] array) {
    this(array[0], array[1], array[2]);
  }

  public MutablePoint3D(final int x, final int y, final int z) {
    _x = x;
    _y = y;
    _z = z;
  }

  public MutablePoint3D(final MutablePoint3D point, final int x, final int y, final int z) {
    this(point._x + x, point._y + y, point._z + z);
  }

  public MutablePoint3D(final MutablePoint2D point) {
    this(point, 0);
  }

  public MutablePoint3D(final MutablePoint2D point, final int z) {
    this(point.getX(), point.getY(), z);
  }

  public MutablePoint3D(final Point2D point) {
    this(point.getX(), point.getY(), 0);
  }

  public MutablePoint3D(final Point2D point, final int z) {
    this(point.getX(), point.getY(), z);
  }

  public MutablePoint3D(final Point3D point) {
    this(point.getX(), point.getY(), point.getZ());
  }

  /** Get the X value. */
  public int getX() {
    return _x;
  }

  /** Set the X value. */
  public void setX(final int x) {
    _x = x;
  }

  /** Add the provided value into X. */
  public MutablePoint3D addX(final int x) {
    _x += x;
    return this;
  }

  /** Get the Y value. */
  public int getY() {
    return _y;
  }

  /** Set the Y value. */
  public void setY(final int y) {
    _y = y;
  }

  /** Add the provided value into Y. */
  public MutablePoint3D addY(final int y) {
    _y += y;
    return this;
  }

  /** Get the Z value. */
  public int getZ() {
    return _z;
  }

  /** Set the Z value. */
  public void setZ(final int z) {
    _z = z;
  }

  /** Add the provided value into Z. */
  public MutablePoint3D addZ(final int z) {
    _z += z;
    return this;
  }

  /** Add the values in the other point into this point. */
  public MutablePoint3D add(final MutablePoint3D other) {
    _x += other._x;
    _y += other._y;
    _z += other._z;
    return this;
  }

  /** Get all non-diagonal neighbors one unit away from this point. */
  public List<MutablePoint3D> getNeighbors() {
    final List<MutablePoint3D> neighbors = new ArrayList<>(6);
    neighbors.add(new MutablePoint3D(_x + 1, _y, _z));
    neighbors.add(new MutablePoint3D(_x - 1, _y, _z));
    neighbors.add(new MutablePoint3D(_x, _y + 1, _z));
    neighbors.add(new MutablePoint3D(_x, _y - 1, _z));
    neighbors.add(new MutablePoint3D(_x, _y, _z + 1));
    neighbors.add(new MutablePoint3D(_x, _y, _z - 1));
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
  public int getManhattanDistance(final MutablePoint3D other) {
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

  @Override
  public int compareTo(final MutablePoint3D o) {
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
    else if (obj instanceof MutablePoint3D o) {
      return (_x == o._x) && (_y == o._y) && (_z == o._z);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Integer.reverse(_x) ^ _y ^ Integer.reverseBytes(_z);
  }

  @Override
  public String toString() {
    return "(" + _x + "," + _y + "," + _z + ")";
  }

}
