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
package net.johngaughan.advent.y2020.day7;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import net.johngaughan.advent.AdventProblem;

/**
 * <p>
 * Year 2020, Day 7, Part 2.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public final class Year2020Day7Part2
implements AdventProblem {

  private static final String NEEDLE = "shiny gold";

  /** {@inheritDoc} */
  @Override
  public long calculate(final Path path) {
    final Map<String, Map<String, Integer>> rules = new Parser().parse(path);
    // Don't count the shiny gold bag itself.
    return countBagAndChildren(NEEDLE, rules, new HashMap<>()) - 1;
  }

  /** Count the number of bags contained by the given color of bag, including the bag itself. */
  private long countBagAndChildren(final String color, final Map<String, Map<String, Integer>> rules, final Map<String, Long> memoizer) {
    if (memoizer.containsKey(color)) {
      return memoizer.get(color);
    }

    long count = 1;
    for (Map.Entry<String, Integer> child : rules.get(color).entrySet()) {
      final String childColor = child.getKey();
      final long childCount = child.getValue();
      count += (childCount * countBagAndChildren(childColor, rules, memoizer));
    }
    memoizer.put(color, count);
    return count;
  }

}
