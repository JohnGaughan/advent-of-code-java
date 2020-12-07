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
package net.johngaughan.advent.y2015.day5;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.johngaughan.advent.AdventProblem;

/**
 * <p>
 * Year 2015, Day 5, Part 2.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public final class Year2015Day5Part2
implements AdventProblem {

  /** {@inheritDoc} */
  @Override
  public long calculate(final Path path) {
    long nice = 0;
    for (String line : new Parser().parse(path)) {
      if (isNice(line)) {
        ++nice;
      }
    }
    return nice;
  }

  private boolean isNice(final String str) {
    boolean pairRepeat = false;
    boolean singleRepeat = false;
    final Map<String, Set<Integer>> pairs = new HashMap<>();
    for (int i = 0; (i < str.length()) && !(singleRepeat && pairRepeat); ++i) {
      // Check for a single repeat two indices apart, e.g. "aba"
      if ((i > 1) && !singleRepeat) {
        singleRepeat = str.charAt(i) == str.charAt(i - 2);
      }
      // Check for a pair of characters already existing.
      if ((i < (str.length() - 1)) && !pairRepeat) {
        String thisPair = str.substring(i, i + 2);
        if (pairs.containsKey(thisPair)) {
          for (int previousIndex : pairs.get(thisPair)) {
            // No overlap
            if ((previousIndex + 2) <= i) {
              pairRepeat = true;
              break;
            }
          }
        }
        // First time this pair has been seen
        else {
          pairs.put(thisPair, new HashSet<>());
        }
        pairs.get(thisPair).add(i);
      }
    }
    return pairRepeat && singleRepeat;
  }

}
