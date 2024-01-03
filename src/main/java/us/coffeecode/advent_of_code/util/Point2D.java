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
 * Represents a point in 2D space that is immutable.
 */
public final class Point2D
implements Comparable<Point2D> {

  /** The origin point, at (0, 0). */
  public static final Point2D ORIGIN = new Point2D(0, 0);

  private final int _x;

  private final int _y;

  private final int hashCode;

  private String toString;

  /** Construct a Point2D from an array with two elements where element zero is X and element one is Y. */
  public Point2D(final int[] array) {
    this(array[0], array[1]);
  }

  /** Construct a Point2D from an array where the index at offset is X and offset plus one is Y. */
  public Point2D(final int[] array, final int offset) {
    this(array[offset], array[offset + 1]);
  }

  /** Construct a Point2D by adding deltas to the dimensions of another Point2D. */
  public Point2D(final Point2D other, final int dx, final int dy) {
    this(other._x + dx, other._y + dy);
  }

  /** Construct a Point2D by flattening a Point3D into two dimensions, ignoring its Z coordinate. */
  public Point2D(final Point3D other) {
    this(other.getX(), other.getY());
  }

  /** Construct a Point2D from raw X and Y coordinates. */
  public Point2D(final int x, final int y) {
    _x = x;
    _y = y;
    hashCode = Integer.reverse(_x) ^ _y;
  }

  /** Get the X value. */
  public int getX() {
    return _x;
  }

  /** Get the Y value. */
  public int getY() {
    return _y;
  }

  /** Get all non-diagonal neighbors one unit away from this point. */
  public List<Point2D> getCardinalNeighbors() {
    final List<Point2D> neighbors = new ArrayList<>(4);
    neighbors.add(new Point2D(_x + 1, _y));
    neighbors.add(new Point2D(_x - 1, _y));
    neighbors.add(new Point2D(_x, _y + 1));
    neighbors.add(new Point2D(_x, _y - 1));
    return neighbors;
  }

  /** Get all diagonal neighbors one unit away from this point. */
  public List<Point2D> getDiagonalNeighbors() {
    final List<Point2D> neighbors = new ArrayList<>(4);
    neighbors.add(new Point2D(_x - 1, _y - 1));
    neighbors.add(new Point2D(_x - 1, _y + 1));
    neighbors.add(new Point2D(_x + 1, _y - 1));
    neighbors.add(new Point2D(_x + 1, _y + 1));
    return neighbors;
  }

  /** Get the neighbors of this point in the X dimension. */
  public List<Point2D> getXNeighbors() {
    return List.of(new Point2D(_x - 1, _y), new Point2D(_x + 1, _y));
  }

  /** Get the neighbors of this point in the Y dimension. */
  public List<Point2D> getYNeighbors() {
    return List.of(new Point2D(_x, _y - 1), new Point2D(_x, _y + 1));
  }

  /** Get all eight neighbors, both cardinal and diagonal. */
  public List<Point2D> getAllNeighbors() {
    final List<Point2D> neighbors = new ArrayList<>(8);
    for (int y = -1; y <= 1; ++y) {
      for (int x = -1; x <= 1; ++x) {
        if ((x != 0) || (y != 0)) {
          neighbors.add(new Point2D(x + _x, y + _y));
        }
      }
    }
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
  public int getManhattanDistance(final Point2D other) {
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

  /** Get whether this point is adjacent to another in any direction. */
  public boolean isAdjacent(final Point2D other) {
    return isAdjacentCardinally(other) || isAdjacentDiagonally(other);
  }

  /** Get whether this point is adjacent to another but only in cardinal directions. */
  public boolean isAdjacentCardinally(final Point2D other) {
    return getManhattanDistance(other) == 1;
  }

  /** Get whether this point is adjacent to another but only in diagonal directions. */
  public boolean isAdjacentDiagonally(final Point2D other) {
    int dx = Math.abs(_x - other._x);
    int dy = Math.abs(_y - other._y);
    return (dx == 1) && (dy == 1);
  }

  public List<Point2D> range(final Point2D to) {
    if (_x == to._x) {
      final List<Point2D> result = new ArrayList<>(Math.abs(_y - to._y) + 1);
      for (int y = Math.min(_y, to._y); y <= Math.max(_y, to._y); ++y) {
        result.add(new Point2D(_x, y));
      }
      return result;
    }
    else if (_y == to._y) {
      final List<Point2D> result = new ArrayList<>(Math.abs(_x - to._x) + 1);
      for (int x = Math.min(_x, to._x); x <= Math.max(_x, to._x); ++x) {
        result.add(new Point2D(x, _y));
      }
      return result;
    }
    throw new IllegalArgumentException(toString() + " and " + to + " are not horizontal or vertical with each other");
  }

  /** Add the coordinates of this point with the given point. */
  public Point2D add(final Point2D p) {
    return new Point2D(_x + p._x, _y + p._y);
  }

  /** Add the provided deltas to this point's coordinates, returning a new point. */
  public Point2D add(final int dx, final int dy) {
    return new Point2D(_x + dx, _y + dy);
  }

  /** Subtract the coordinates of the given point from this point. */
  public Point2D subtract(final Point2D p) {
    return new Point2D(_x - p._x, _y - p._y);
  }

  /** Create a new point whose coordinates are the opposite of the current point. */
  public Point2D invert() {
    return new Point2D(-_x, -_y);
  }

  @Override
  public int compareTo(final Point2D o) {
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
    else if (obj instanceof Point2D o) {
      return (_x == o._x) && (_y == o._y);
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
      toString = "(" + _x + "," + _y + ")";
    }
    return toString;
  }

}
