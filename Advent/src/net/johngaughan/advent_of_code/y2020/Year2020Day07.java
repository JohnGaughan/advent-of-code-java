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
package net.johngaughan.advent_of_code.y2020;

import java.nio.file.Files;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import net.johngaughan.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2020/day/7">Year 2020, day 7</a>. This problem states that there are different
 * colors of bags, and each one must contain certain other bags in various quantities. Part one asks which color bags
 * can contain shiny gold bags directly or indirectly. Part two asks how many bags a shiny gold bag must contain.
 * </p>
 * <p>
 * Similar to year 2015 day 7, this is a test in recursion. I use memoization for both parts due to redundancy in the
 * input. One gotcha in the problem is not to count the shiny gold bag itself: an easy off-by-one error.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public final class Year2020Day07 {

  private static final String SEARCH_NEEDLE = "shiny gold";

  public int calculatePart1() {
    final Map<String, Map<String, Integer>> rules = getInput();
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
    final Set<String> childColors = rules.get(currentColor).keySet();

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

  public int calculatePart2() {
    final Map<String, Map<String, Integer>> rules = getInput();
    // Don't count the shiny gold bag itself.
    return countBagAndChildren(SEARCH_NEEDLE, rules, new HashMap<>()) - 1;
  }

  /** Count the number of bags contained by the given color of bag, including the bag itself. */
  private int countBagAndChildren(final String color, final Map<String, Map<String, Integer>> rules, final Map<String, Integer> memoizer) {
    if (memoizer.containsKey(color)) {
      return memoizer.get(color).intValue();
    }

    int count = 1;
    for (final Map.Entry<String, Integer> child : rules.get(color).entrySet()) {
      final String childColor = child.getKey();
      final int childCount = child.getValue().intValue();
      count += childCount * countBagAndChildren(childColor, rules, memoizer);
    }
    memoizer.put(color, Integer.valueOf(count));
    return count;
  }

  /** Matches the delimiter between the bag and the bags it contains. */
  private static final Pattern LINE_SPLIT = Pattern.compile(" bags contain ");

  /** Matches the delimiter between bag types. */
  private static final Pattern LIST_SPLIT = Pattern.compile(", ");

  /** Get the input data for this solution. */
  public Map<String, Map<String, Integer>> getInput() {
    try {
      return Files.readAllLines(Utils.getInput(2020, 7)).stream().map(s -> LINE_SPLIT.split(s)).collect(
        Collectors.toMap(s -> s[0], s -> getInput(s[1].substring(0, s[1].length() - 1))));
    }
    catch (final RuntimeException ex) {
      throw ex;
    }
    catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
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
