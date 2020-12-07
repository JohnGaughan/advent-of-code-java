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
 * Year 2015, Day 6, Part 2.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public final class Year2015Day6Part2
implements AdventProblem {

  /** {@inheritDoc} */
  @Override
  public long calculate(final Path path) {
    final int[][] grid = new int[1000][1000];
    for (final Action action : new Parser().parse(path)) {
      for (int i = action.x1; i <= action.x2; ++i) {
        for (int j = action.y1; j <= action.y2; ++j) {
          if (action.command == Command.ON) {
            ++grid[i][j];
          }
          else if (action.command == Command.OFF) {
            --grid[i][j];
            if (grid[i][j] < 0) {
              grid[i][j] = 0;
            }
          }
          else if (action.command == Command.TOGGLE) {
            grid[i][j] += 2;
          }
        }
      }
    }
    long brightness = 0;
    for (int[] row : grid) {
      for (int i : row) {
        brightness += i;
      }
    }
    return brightness;
  }

}
