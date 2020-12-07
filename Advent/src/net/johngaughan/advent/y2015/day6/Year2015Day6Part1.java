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
package net.johngaughan.advent.y2015.day6;

import java.nio.file.Path;

import net.johngaughan.advent.AdventProblem;

/**
 * <p>
 * Year 2015, Day 6, Part 1.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public final class Year2015Day6Part1
implements AdventProblem {

  /** {@inheritDoc} */
  @Override
  public long calculate(final Path path) {
    final boolean[][] grid = new boolean[1000][1000];
    for (final Action action : new Parser().parse(path)) {
      for (int i = action.x1; i <= action.x2; ++i) {
        for (int j = action.y1; j <= action.y2; ++j) {
          if (action.command == Command.ON) {
            grid[i][j] = true;
          }
          else if (action.command == Command.OFF) {
            grid[i][j] = false;
          }
          else if (action.command == Command.TOGGLE) {
            grid[i][j] = !grid[i][j];
          }
        }
      }
    }
    long lit = 0;
    for (boolean[] row : grid) {
      for (boolean b : row) {
        if (b) {
          ++lit;
        }
      }
    }
    return lit;
  }

}
