/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2024  John Gaughan
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
package us.coffeecode.advent_of_code.y2024;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2024, day = 19)
@Component
public class Year2024Day19 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Input input = getInput(pc);
    final Map<String, Boolean> cache = new HashMap<>(1 << 14);
    return input.towels.stream()
                       .filter(s -> isPossible(s, input.stripes, cache))
                       .count();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final Input input = getInput(pc);
    final Map<String, Long> cache = new HashMap<>(1 << 15);
    return input.towels.stream()
                       .mapToLong(s -> permutations(s, input.stripes, cache))
                       .sum();
  }

  /** Recursive function that determines if this towel segment is possible to construct. */
  private boolean isPossible(final String towel, final Iterable<String> stripes, final Map<String, Boolean> cache) {
    if (cache.containsKey(towel)) {
      return cache.get(towel)
                  .booleanValue();
    }
    for (final String stripe : stripes) {
      // This towel is compatible with the stripe.
      if (towel.startsWith(stripe)) {
        // Complete match.
        if (towel.length() == stripe.length()) {
          cache.put(towel, Boolean.TRUE);
          return true;
        }
        // Recursive case: try to match the remainder of the towel.
        else if (isPossible(towel.substring(stripe.length()), stripes, cache)) {
          cache.put(towel, Boolean.TRUE);
          return true;
        }
      }
    }
    cache.put(towel, Boolean.FALSE);
    return false;
  }

  /** Recursive function that determines how many ways this towel segment can be constructed. */
  private long permutations(final String towel, final Iterable<String> stripes, final Map<String, Long> cache) {
    if (cache.containsKey(towel)) {
      return cache.get(towel)
                  .longValue();
    }
    long permutations = 0;
    for (final String stripe : stripes) {
      if (towel.startsWith(stripe)) {
        // This towel is compatible with the stripe.
        if (towel.startsWith(stripe)) {
          // Complete match: one permutation.
          if (towel.length() == stripe.length()) {
            ++permutations;
          }
          // Recursive case: count permutations for the remainder of the towel.
          else {
            permutations += permutations(towel.substring(stripe.length()), stripes, cache);
          }
        }
      }
    }
    cache.put(towel, Long.valueOf(permutations));
    return permutations;
  }

  private Input getInput(final PuzzleContext pc) {
    final List<List<String>> groups = il.groups(pc);
    final String rawStripes = groups.get(0)
                                    .get(0);
    final Collection<String> stripes = Arrays.asList(SPLIT_STRIPES.split(rawStripes));
    return new Input(stripes, groups.get(1));
  }

  /** Program input consisting of stripes to use to attempt to construct towels. */
  private record Input(Collection<String> stripes, Collection<String> towels) {}

  /** Splits the first line of the input file into stripes. */
  private static final Pattern SPLIT_STRIPES = Pattern.compile(", ");
}
