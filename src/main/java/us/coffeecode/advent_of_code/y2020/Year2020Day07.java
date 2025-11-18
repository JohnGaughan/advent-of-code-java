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
package us.coffeecode.advent_of_code.y2020;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2020, day = 7)
@Component
public final class Year2020Day07 {

  private static final String SEARCH_NEEDLE = "shiny gold";

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Map<String, Map<String, Integer>> rules = getInput(pc);
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
    for (final String color : rules.keySet()) {
      checkForNeedle(color, rules, colors, negatives);
    }
    return colors;
  }

  /** Get whether the current color bag can contain the search needle, directly or transitively. */
  private boolean checkForNeedle(final String currentColor, final Map<String, Map<String, Integer>> rules, final Set<String> positives, final Set<String> negatives) {
    final Set<String> childColors = rules.get(currentColor)
                                         .keySet();

    // See if the needle is a direct descendant of the current color.
    if (childColors.contains(SEARCH_NEEDLE)) {
      positives.add(currentColor);
    }

    // Check each child recursively.
    for (final String childColor : childColors) {
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

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final Map<String, Map<String, Integer>> rules = getInput(pc);
    // Don't count the shiny gold bag itself.
    return countBagAndChildren(SEARCH_NEEDLE, rules, new HashMap<>()) - 1;
  }

  /** Count the number of bags contained by the given color of bag, including the bag itself. */
  private long countBagAndChildren(final String color, final Map<String, Map<String, Integer>> rules, final Map<String, Long> memoizer) {
    if (memoizer.containsKey(color)) {
      return memoizer.get(color)
                     .longValue();
    }

    long count = 1;
    for (final Map.Entry<String, Integer> child : rules.get(color)
                                                       .entrySet()) {
      final String childColor = child.getKey();
      final int childCount = child.getValue()
                                  .intValue();
      count += childCount * countBagAndChildren(childColor, rules, memoizer);
    }
    memoizer.put(color, Long.valueOf(count));
    return count;
  }

  /** Matches the delimiter between the bag and the bags it contains. */
  private static final Pattern LINE_SPLIT = Pattern.compile(" bags contain ");

  /** Matches the delimiter between bag types. */
  private static final Pattern LIST_SPLIT = Pattern.compile(", ");

  /** Get the input data for this solution. */
  public Map<String, Map<String, Integer>> getInput(final PuzzleContext pc) {
    return il.linesAsMap(pc, s -> LINE_SPLIT.split(s), s -> s[0], s -> getInput(s[1].substring(0, s[1].length() - 1)));
  }

  /** Parse a single input line. */
  private Map<String, Integer> getInput(final String line) {
    final Map<String, Integer> rules = new HashMap<>();

    // Only populate if there are children: otherwise keep it empty, to avoid null checks when using it.
    if (!"no other bags".equals(line)) {
      for (final String element : LIST_SPLIT.split(line)) {
        final int space = element.indexOf(' ');

        // Number is everything up to the first space
        final Integer value = Integer.valueOf(element.substring(0, space));

        // Color is everything after the first space up until "bag" or "bags"
        final int bag = element.indexOf(" bag");
        final String key = element.substring(space + 1, bag);

        rules.put(key, value);
      }
    }
    return rules;
  }

}
