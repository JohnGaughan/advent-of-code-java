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
package net.johngaughan.advent.y2015.day3;

import java.util.Objects;

/**
 * <p>
 * Represents a single pair of coordinates.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
final class Coordinates {

  private final int _x;

  private final int _y;

  private final int hashCode;

  /** Constructs a <code>Coordinates</code>. */
  public Coordinates(final int x, final int y) {
    this._x = x;
    this._y = y;
    this.hashCode = Objects.hash(this._x, this._y);
  }

  /** {@inheritDoc} */
  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    else if (!(obj instanceof Coordinates)) {
      return false;
    }
    else {
      Coordinates o = (Coordinates) obj;
      return (this._x == o._x) && (this._y == o._y);
    }
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    return this.hashCode;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return "(" + this._x + "," + this._y + ")";
  }
}
