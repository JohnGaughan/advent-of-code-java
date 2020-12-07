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

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import net.johngaughan.advent.AdventProblem;

/**
 * <p>
 * Year 2015, day 3, part 1.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public final class Year2015Day3Part1
implements AdventProblem {

  /** {@inheritDoc} */
  @Override
  public long calculate(final Path path) {
    final Set<Coordinates> visited = new HashSet<>();
    int x = 0;
    int y = 0;

    // Visit the first house.
    visited.add(new Coordinates(x, y));

    // Now travel around and visit other houses.
    for (Direction d : new Parser().parse(path)) {
      if (Direction.UP == d) {
        ++x;
      }
      else if (Direction.DOWN == d) {
        --x;
      }
      else if (Direction.LEFT == d) {
        --y;
      }
      else if (Direction.RIGHT == d) {
        ++y;
      }
      else {
        throw new AssertionError("Unknown direction [" + d + "]");
      }
      visited.add(new Coordinates(x, y));
    }

    return visited.size();
  }

}
