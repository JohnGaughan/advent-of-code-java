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
package net.johngaughan.advent.y2015.day1;

import java.nio.file.Path;

import net.johngaughan.advent.AdventProblem;

/**
 * <p>
 * Day one, part two.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public class Year2015Day1Part2
implements AdventProblem {

  /** {@inheritDoc} */
  @Override
  public long calculate(final Path path) {
    String input = new Parser().parse(path);
    for (int i = 0, floor = 0; i < input.length(); ++i) {
      char ch = input.charAt(i);
      if (ch == '(') {
        ++floor;
      }
      else if (ch == ')') {
        --floor;
      }
      if (floor < 0) {
        // Answer expects one-based index.
        return i + 1;
      }
    }
    return Long.MIN_VALUE;
  }

}
