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
package net.johngaughan.advent.y2020.day1;

import java.nio.file.Path;
import java.util.List;

import net.johngaughan.advent.y2020.AdventProblem;

/**
 * <p>
 * Day One, Part Two.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public final class Day1Part2
implements AdventProblem {

  /** {@inheritDoc} */
  @Override
  public long calculate(final Path path) {
    final List<Integer> values = new Parser().parse(path);
    for (int i = 0; i < values.size(); ++i) {
      final int i1 = values.get(i).intValue();
      for (int j = i + 1; j < values.size(); ++j) {
        final int j1 = values.get(j).intValue();
        for (int k = j + 1; k < values.size(); ++k) {
          final int k1 = values.get(k).intValue();
          if ((i1 + j1 + k1) == 2020) {
            return Integer.valueOf(i1 * j1 * k1);
          }
          else if ((i1 + j1 + k1) > 2020) {
            continue;
          }
        }
        if ((i1 + j1) > 2020) {
          continue;
        }
      }
    }
    return -1;
  }

}
