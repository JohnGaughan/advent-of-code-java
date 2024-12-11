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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2024, day = 11, title = "Plutonian Pebbles")
@Component
public class Year2024Day11 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(pc, 25);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculate(pc, 75);
  }

  public long calculate(final PuzzleContext pc, final int iterations) {
    final Collection<Long> input = il.fileAsObjectsFromSplit(pc, SPLIT, Long::valueOf);
    return input.stream()
                .mapToLong(i -> countDescendants(i, iterations, new HashMap<>(2 << 12)))
                .sum();
  }

  /** Count the number of child numbers this parent number will split into after the given number of iterations. */
  private long countDescendants(final Long value, final int iterations, final Map<CacheKey, Long> cache) {
    // Tail case: previous level has one solution.
    if (iterations == 0) {
      return 1;
    }
    // Memoization step: this state was seen before, so short-circuit and return its answer.
    final CacheKey key = new CacheKey(value, iterations);
    if (cache.containsKey(key)) {
      return cache.get(key)
                  .longValue();
    }
    final int nextIterations = iterations - 1;
    // Rule 1.
    if (ZERO.equals(value)) {
      final long result = countDescendants(ONE, nextIterations, cache);
      cache.put(key, Long.valueOf(result));
      return result;
    }
    // Rule 2.
    final String str = value.toString();
    if ((str.length() % 2) == 0) {
      final int midpoint = (str.length() >> 1);
      final Long left = Long.valueOf(str.substring(0, midpoint));
      final Long right = Long.valueOf(str.substring(midpoint));
      final long result = countDescendants(left, nextIterations, cache) + countDescendants(right, nextIterations, cache);
      cache.put(key, Long.valueOf(result));
      return result;
    }
    // Rule 3.
    final long result = countDescendants(Long.valueOf(value.longValue() * 2024), nextIterations, cache);
    cache.put(key, Long.valueOf(result));
    return result;
  }

  /** Contains the unique state of a single recursive step to identify it in a cache. */
  private record CacheKey(Long value, int iterations) {}

  private static final Long ZERO = Long.valueOf(0);

  private static final Long ONE = Long.valueOf(1);

  private static final Pattern SPLIT = Pattern.compile(" ");
}
