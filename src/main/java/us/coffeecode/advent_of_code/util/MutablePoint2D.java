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
 * Represents a point in 2D space that can be modified.
 */
public final class MutablePoint2D
implements Comparable<MutablePoint2D> {

  private int _x;

  private int _y;

  public MutablePoint2D(final int[] array) {
    this(array[0], array[1]);
  }

  public MutablePoint2D(final int x, final int y) {
    _x = x;
    _y = y;
  }

  public MutablePoint2D(final MutablePoint2D point, final int x, final int y) {
    this(point._x + x, point._y + y);
  }

  public MutablePoint2D(final MutablePoint3D point) {
    this(point.getX(), point.getY());
  }

  public MutablePoint2D(final Point2D point) {
    this(point.getX(), point.getY());
  }

  public MutablePoint2D(final Point3D point) {
    this(point.getX(), point.getY());
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
  public MutablePoint2D addX(final int x) {
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
  public MutablePoint2D addY(final int y) {
    _y += y;
    return this;
  }

  /** Add the values in the other point into this point. */
  public MutablePoint2D add(final MutablePoint2D other) {
    _x += other._x;
    _y += other._y;
    return this;
  }

  /** Get all non-diagonal neighbors one unit away from this point. */
  public List<MutablePoint2D> getNeighbors() {
    final List<MutablePoint2D> neighbors = new ArrayList<>(4);
    neighbors.add(new MutablePoint2D(_x + 1, _y));
    neighbors.add(new MutablePoint2D(_x - 1, _y));
    neighbors.add(new MutablePoint2D(_x, _y + 1));
    neighbors.add(new MutablePoint2D(_x, _y - 1));
    return neighbors;
  }

  /** Get the value in a map cell corresponding to this point. */
  public boolean get(final boolean[][] map) {
    return map[_y][_x];
  }

  /** Set the value in a map cell corresponding to this point. */
  public void set(final boolean[][] map, final boolean value) {
    map[_y][_x] = value;
  }

  /** Get the value in a map cell corresponding to this point. */
  public int get(final int[][] map) {
    return map[_y][_x];
  }

  /** Set the value in a map cell corresponding to this point. */
  public void set(final int[][] map, final int value) {
    map[_y][_x] = value;
  }

  /** Get the value in a map cell corresponding to this point. */
  public long get(final long[][] map) {
    return map[_y][_x];
  }

  /** Set the value in a map cell corresponding to this point. */
  public void set(final long[][] map, final long value) {
    map[_y][_x] = value;
  }

  /** Get the value in a map cell corresponding to this point. */
  public <T> T get(final T[][] map) {
    return map[_y][_x];
  }

  /** Set the value in a map cell corresponding to this point. */
  public <T> void set(final T[][] map, final T value) {
    map[_y][_x] = value;
  }

  /** Get the Manhattan distance to origin. */
  public int getManhattanDistance() {
    return Math.abs(_x) + Math.abs(_y);
  }

  /** Get the Manhattan distance to another point. */
  public int getManhattanDistance(final MutablePoint2D other) {
    return Math.abs(_x - other._x) + Math.abs(_y - other._y);
  }

  /** Get whether this point is at origin, the center of space. */
  public boolean isOrigin() {
    return (_x == 0) && (_y == 0);
  }

  /** Get whether this point exists inside the map. */
  public boolean isIn(final boolean[][] map) {
    if ((_x < 0) || (_y < 0)) {
      return false;
    }
    return (_y < map.length) && (_x < map[_y].length);
  }

  /** Get whether this point exists inside the map. */
  public boolean isIn(final int[][] map) {
    if ((_x < 0) || (_y < 0)) {
      return false;
    }
    return (_y < map.length) && (_x < map[_y].length);
  }

  /** Get whether this point exists inside the map. */
  public boolean isIn(final long[][] map) {
    if ((_x < 0) || (_y < 0)) {
      return false;
    }
    return (_y < map.length) && (_x < map[_y].length);
  }

  /** Get whether this point exists inside the map. */
  public <T> boolean isIn(final T[][] map) {
    if ((_x < 0) || (_y < 0)) {
      return false;
    }
    return (_y < map.length) && (_x < map[_y].length);
  }

  @Override
  public int compareTo(final MutablePoint2D o) {
    final int result = Integer.compare(_y, o._y);
    if (result != 0) {
      return result;
    }
    return Integer.compare(_x, o._x);
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    else if (obj instanceof MutablePoint2D o) {
      return (_x == o._x) && (_y == o._y);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Integer.reverse(_x) ^ _y;
  }

  @Override
  public String toString() {
    return "(" + _x + "," + _y + ")";
  }

}
