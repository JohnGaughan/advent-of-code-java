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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.johngaughan.advent.AdventProblem;

/**
 * <p>
 * Year 2020, Day 7, Part 1.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public final class Year2020Day7Part1
implements AdventProblem {

  private static final String SEARCH_NEEDLE = "shiny gold";

  /** {@inheritDoc} */
  @Override
  public long calculate(final Path path) {
    final Map<String, Map<String, Integer>> rules = new Parser().parse(path);
    final Set<String> colors = getColors(rules);
    return colors.size();
  }

  /**
   * Get the colors that can contain the search needle given the provided rules about which bags contain which other
   * bags.
   */
  private Set<String> getColors(final Map<String, Map<String, Integer>> rules) {
    final Set<String> colors = new HashSet<>();
    final Set<String> negatives = new HashSet<>();
    for (String color : rules.keySet()) {
      checkForNeedle(color, rules, colors, negatives);
    }
    return colors;
  }

  /** Get whether the current color bag can contain the search needle, directly or transitively. */
  private boolean checkForNeedle(final String currentColor, final Map<String, Map<String, Integer>> rules, final Set<String> positives, final Set<String> negatives) {
    final Set<String> childColors = rules.get(currentColor).keySet();

    // See if the needle is a direct descendant of the current color.
    if (childColors.contains(SEARCH_NEEDLE)) {
      positives.add(currentColor);
    }

    // Check each child recursively.
    for (String childColor : childColors) {
      // If we already know the child can contain the needle, add the current color.
      if (positives.contains(childColor)) {
        positives.add(currentColor);
      }
      // If the child has not been memoized either positively or negatively, i.e. it is a "maybe," recurse and check.
      else if (!negatives.contains(childColor)) {
        if (checkForNeedle(childColor, rules, positives, negatives)) {
          positives.add(currentColor);
        }
        else {
          negatives.add(currentColor);
        }
      }
    }
    return positives.contains(currentColor);
  }

}
