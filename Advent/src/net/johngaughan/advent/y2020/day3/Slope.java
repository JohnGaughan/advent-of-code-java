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
package net.johngaughan.advent.y2020.day3;

import java.util.List;

/**
 * <p>
 * This class represents a slope that can be traversed. A slope repeats in the horizontal direction, but not vertically.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
final class Slope {

  /**
   * This represents the slope, sort of like Tux Racer. If a particular array element is true, there is a tree at that
   * location: otherwise, it is clear.
   */
  private final boolean[][] slope;

  /** Constructs a <code>Slope</code>. */
  public Slope(final List<String> lines) {
    this.slope = new boolean[lines.size()][];
    for (int i = 0; i < this.slope.length; ++i) {
      String line = lines.get(i);
      this.slope[i] = new boolean[line.length()];
      for (int j = 0; j < this.slope[i].length; ++j) {
        char c = line.charAt(j);
        this.slope[i][j] = (c == '#');
      }
    }
  }

  /** Count the number of trees encountered for the provided row and column changes. */
  public long countTrees(final long rowChange, final long colChange) {
    long trees = 0;
    int row = 0;
    int col = 0;
    while (row < this.slope.length) {
      if (isTree(row, col)) {
        ++trees;
      }
      row += rowChange;
      col += colChange;
    }
    return trees;
  }

  /** Get whether there is a tree at the given coordinate. */
  private boolean isTree(final int row, final int col) {
    final int colMod = col % this.slope[row].length;
    return this.slope[row][colMod];
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    StringBuilder str = new StringBuilder();
    boolean first = true;
    for (boolean[] line : this.slope) {
      if (first) {
        first = false;
      }
      else {
        str.append("\n");
      }
      for (boolean b : line) {
        str.append(b ? "X" : ".");
      }
    }
    return str.toString();
  }

}
