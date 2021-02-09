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
package us.coffeecode.advent_of_code;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * This utility class contains general-purpose code used by multiple problems.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Utils {

  /** Gets the path containing input data for the given year and day. Used by unit tests. */
  public static Path getInput(final int year, final int day) {
    return Path.of("input", Integer.toString(year), "day" + (day < 10 ? "0" + day : Integer.toString(day)) + ".txt");
  }

  /**
   * <p>
   * Given a list of strings, group them up based on spacing. A blank string marks the boundary between string groups.
   * </p>
   * <p>
   * The blank strings are not returned.
   * </p>
   *
   * @param lines the lines to group.
   * @return the lines grouped together.
   */
  public static List<List<String>> getLineGroups(final List<String> lines) {
    // 1. Split lines list into segments based on blank lines separating passports.
    final List<List<String>> lineGroups = new ArrayList<>();
    int start = -1;
    for (int i = 0; i < lines.size(); ++i) {
      final boolean currentLineHasData = !lines.get(i).isBlank();
      // Not currently inside data
      if (start < 0) {
        // Don't combine these if statements!
        if (currentLineHasData) {
          start = i;
        }
      }
      // Current inside passport data
      else if (!currentLineHasData) {
        lineGroups.add(lines.subList(start, i));
        start = -1;
      }
    }
    // See if there is dangling data: if so, add it
    if (start > 0) {
      lineGroups.add(lines.subList(start, lines.size()));
    }

    return lineGroups;
  }

  /**
   * Calculate all of the permutations of the provided items. Each permutation will be stored in a list that conveys the
   * ordering of the items, with all permutations in a set that enforces uniqueness among orderings.
   *
   * @param <T> the type of item being processed.
   * @param items the items for which to find all permutations.
   * @return a set containing all unique permutations.
   */
  public static <T> Set<List<T>> permutations(final Collection<T> items) {
    if (items instanceof List) {
      return permutations((List<T>) items, new ArrayList<>());
    }
    return permutations(new ArrayList<>(items), new ArrayList<>());
  }

  private static <T> Set<List<T>> permutations(final List<T> items, final List<T> permutations) {
    final Set<List<T>> results = new HashSet<>();
    // Tail case
    if (items.isEmpty()) {
      results.add(new ArrayList<>(permutations));
    }
    // Recursive case
    else {
      for (int i = 0; i < items.size(); ++i) {
        final List<T> newLocations = new ArrayList<>();
        newLocations.addAll(items.subList(0, i));
        newLocations.addAll(items.subList(i + 1, items.size()));
        final List<T> newPermutations = new ArrayList<>(permutations);
        newPermutations.add(items.get(i));
        results.addAll(permutations(newLocations, newPermutations));
      }
    }
    return results;
  }

}
