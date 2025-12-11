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
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    return dfs(getInput(pc), START1, new HashMap<>());
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return dfs(getInput(pc), START2, false, false, new HashMap<>());
  }

  /** Perform a depth-first search with memoization for part one. */
  private long dfs(final Map<String, Collection<String>> routes, final String currentNode, final Map<String, Long> cache) {
    // Found the end: this either counts as one route if we passed through both intermediate nodes, or zero if not.
    if (END.equals(currentNode)) {
      return 1;
    }

    long routeCount = 0;

    // Memoization: we found this route already so use the cached answer.
    if (cache.containsKey(currentNode)) {
      return cache.get(currentNode)
                  .longValue();
    }

    // Have not visited this node before: visit its children.
    for (final String nextNode : routes.get(currentNode)) {
      routeCount += dfs(routes, nextNode, cache);
    }

    // Save the results of visiting this node in the memoization cache for later use.
    cache.put(currentNode, Long.valueOf(routeCount));
    return routeCount;
  }

  /** Perform a depth-first search with memoization for part two. */
  private long dfs(final Map<String, Collection<String>> routes, final String currentNode, final boolean visit1, final boolean visit2, final Map<CacheKey, Long> cache) {
    // Found the end: this either counts as one route if we passed through both intermediate nodes, or zero if not.
    if (END.equals(currentNode)) {
      return (visit1 && visit2) ? 1 : 0;
    }

    long routeCount = 0;
    final CacheKey cacheKey = new CacheKey(currentNode, visit1, visit2);

    // Memoization: we found this route already so use the cached answer.
    if (cache.containsKey(cacheKey)) {
      return cache.get(cacheKey)
                  .longValue();
    }

    // Have not visited this node before: visit its children.
    for (final String nextNode : routes.get(currentNode)) {
      if (VISIT1.equals(nextNode)) {
        routeCount += dfs(routes, nextNode, true, visit2, cache);
      }
      else if (VISIT2.equals(nextNode)) {
        routeCount += dfs(routes, nextNode, visit1, true, cache);
      }
      else {
        routeCount += dfs(routes, nextNode, visit1, visit2, cache);
      }
    }

    // Save the results of visiting this node in the memoization cache for later use.
    cache.put(cacheKey, Long.valueOf(routeCount));
    return routeCount;
  }

  /** Get the input as a node mapped to the downstream nodes to which it is linked. */
  private Map<String, Collection<String>> getInput(final PuzzleContext pc) {
    return il.linesAsStrings(pc, SPLIT)
             .stream()
             .collect(Collectors.toMap(t -> t.get(0), t -> t.subList(1, t.size())));
  }

  /** Combination of state that unique identifies a node visit. This is used as a key in the memoization cache. */
  private static record CacheKey(String node, boolean visit1, boolean visit2) {}

  /** Pattern that splits a line of input into tokens. */
  private static final Pattern SPLIT = Pattern.compile(":? ");

  /** Start node for part 1. */
  private static final String START1 = "you";

  /** Start node for part 2. */
  private static final String START2 = "svr";

  /** Required intermediate node 1. */
  private static final String VISIT1 = "dac";

  /** Required intermediate node 2. */
  private static final String VISIT2 = "fft";

  /** End node for both parts. */
  private static final String END = "out";

}
