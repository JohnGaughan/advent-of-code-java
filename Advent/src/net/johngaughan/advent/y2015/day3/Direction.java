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

/**
 * <p>
 * Enumeration of movement directions.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
enum Direction {

                UP('^'),
                DOWN('v'),
                LEFT('<'),
                RIGHT('>');

  /** Get whether the provided direction character correlates with an instance of this enum. */
  public static boolean isValid(final char ch) {
    for (Direction d : values()) {
      if (d.character == ch) {
        return true;
      }
    }
    return false;
  }

  public static Direction valueOf(final char ch) {
    for (Direction d : values()) {
      if (d.character == ch) {
        return d;
      }
    }
    throw new IllegalArgumentException(
      "Input character [" + ch + "] does not correlate with any instance of this enum");
  }

  private final char character;

  Direction(final char ch) {
    this.character = ch;
  }

}
