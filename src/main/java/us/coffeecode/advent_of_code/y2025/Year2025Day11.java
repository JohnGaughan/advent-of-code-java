/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2025  John Gaughan
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
package us.coffeecode.advent_of_code.y2025;

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

@AdventOfCodeSolution(year = 2025, day = 11)
@Component
public class Year2025Day11 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Map<String, Collection<String>> routes = getInput(pc);
    final Map<String, Long> routeCounts = new HashMap<>(routes.size());
    routeCounts.put(END, Long.valueOf(1));
    /*
     * Starting at the end, work backwards to see how many routes each node has to the end. This works because the input
     * has no loops. If this loops infinitely then the input is bad.
     */
    while (!routeCounts.containsKey(START1)) {
      for (final var entry : routes.entrySet()) {
        final String node = entry.getKey();
        if (!routeCounts.containsKey(node)) {
          final Collection<String> targets = entry.getValue();
          if (routeCounts.keySet()
                         .containsAll(targets)) {
            final long routeCount = targets.stream()
                                           .mapToLong(n -> routeCounts.get(n)
                                                                      .longValue())
                                           .sum();
            routeCounts.put(node, Long.valueOf(routeCount));
          }
        }
      }
    }
    return routeCounts.get(START1)
                      .longValue();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final Map<String, Collection<String>> routes = getInput(pc);
    return dfs(routes, START2, false, false, new HashMap<>());
  }

  /** Perform a depth-first search with memoization for part two. */
  private long dfs(final Map<String, Collection<String>> routes, final String current, final boolean int1, final boolean int2, final Map<CacheKey, Long> cache) {
    // Found the end: this either counts as one route if we passed through both intermediate nodes, or zero if not.
    if (END.equals(current)) {
      return (int1 && int2) ? 1 : 0;
    }

    long routeCount = 0;
    final CacheKey cacheKey = new CacheKey(current, int1, int2);

    // Memoization: we found this route already so use the cached answer.
    if (cache.containsKey(cacheKey)) {
      return cache.get(cacheKey)
                  .longValue();
    }

    // Have not visited this node before: visit its children.
    for (final String nextCurrent : routes.get(current)) {
      if (INT1.equals(nextCurrent)) {
        routeCount += dfs(routes, nextCurrent, true, int2, cache);
      }
      else if (INT2.equals(nextCurrent)) {
        routeCount += dfs(routes, nextCurrent, int1, true, cache);
      }
      else {
        routeCount += dfs(routes, nextCurrent, int1, int2, cache);
      }
    }

    // Save the results of visiting this node in the memoization cache for later use.
    cache.put(cacheKey, Long.valueOf(routeCount));
    return routeCount;
  }

  /** Get the input as a node mapped to the downstream nodes to which it is linked. */
  private Map<String, Collection<String>> getInput(final PuzzleContext pc) {
    final List<List<String>> lines = il.linesAsStrings(pc, SPLIT);
    final Map<String, Collection<String>> routes = new HashMap<>(lines.size());
    for (final List<String> line : lines) {
      routes.put(line.getFirst()
                     // Snip off the colon
                     .substring(0, 3),
        line.subList(1, line.size()));
    }
    return routes;
  }

  /** Combination of state that unique identifies a node visit. This is used as a key in the memoization cache. */
  private static record CacheKey(String node, boolean fft, boolean dac) {}

  /** Pattern that splits a line of input into tokens. */
  private static final Pattern SPLIT = Pattern.compile(" ");

  /** Start node for part 1. */
  private static final String START1 = "you";

  /** Start node for part 2. */
  private static final String START2 = "svr";

  /** Required intermediate node 1. */
  private static final String INT1 = "dac";

  /** Required intermediate node 2. */
  private static final String INT2 = "fft";

  /** End node for both parts. */
  private static final String END = "out";

}
