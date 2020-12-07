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
package net.johngaughan.advent.y2020.day6;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.johngaughan.advent.AdventProblem;

/**
 * <p>
 * Day six, part one.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public final class Year2020Day6Part1
implements AdventProblem {

  /** {@inheritDoc} */
  @Override
  public long calculate(final Path path) {
    long sum = 0;
    for (final Collection<?> c : reduceGroups(new Parser().parse(path))) {
      sum += c.size();
    }
    return sum;
  }

  private List<Set<Character>> reduceGroups(final List<List<Set<Character>>> groups) {
    final List<Set<Character>> result = new ArrayList<>(groups.size());
    for (final List<Set<Character>> group : groups) {
      result.add(reduceGroup(group));
    }
    return result;
  }

  private Set<Character> reduceGroup(final List<Set<Character>> group) {
    final Set<Character> response = new HashSet<>();
    // Get the union of all of the sets/lines.
    for (final Set<Character> line : group) {
      response.addAll(line);
    }
    return response;
  }

}
