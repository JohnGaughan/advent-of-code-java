/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2021  John Gaughan
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
package us.coffeecode.advent_of_code.y2017;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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

@AdventOfCodeSolution(year = 2017, day = 12, title = "Digital Plumber")
@Component
public final class Year2017Day12 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Map<Integer, Integer[]> nodeMapping = getInput(pc);
    final Set<Integer> groupZero = getGroup(nodeMapping, Integer.valueOf(0));
    return groupZero.size();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final Map<Integer, Integer[]> nodeMapping = getInput(pc);
    final Collection<Set<Integer>> groups = new ArrayList<>();
    while (!nodeMapping.isEmpty()) {
      final Integer start = nodeMapping.keySet().iterator().next();
      final Set<Integer> group = getGroup(nodeMapping, start);
      groups.add(group);
      for (final Integer key : group) {
        nodeMapping.remove(key);
      }
    }
    return groups.size();
  }

  /** Get the set of all nodes that are connected - directly or indirectly - to the start node. */
  private Set<Integer> getGroup(final Map<Integer, Integer[]> nodeMapping, final Integer start) {
    final Set<Integer> visited = new HashSet<>();
    // Nodes visiting during the current search level.
    Set<Integer> visiting = new HashSet<>();
    visiting.add(start);
    while (!visiting.isEmpty()) {
      // Nodes to visit during the next search level.
      final Set<Integer> nextVisiting = new HashSet<>();
      for (final Integer key : visiting) {
        if (!visited.contains(key)) {
          // Add the current node to the set of visited nodes, then add all of its connections to the nodes to visit
          // next.
          visited.add(key);
          for (final Integer value : nodeMapping.get(key)) {
            nextVisiting.add(value);
          }
        }
      }
      visiting = nextVisiting;
    }
    return visited;
  }

  /** Get the input data for this solution. */
  private Map<Integer, Integer[]> getInput(final PuzzleContext pc) {
    final Map<Integer, Integer[]> result = new HashMap<>(2700);
    for (final String line : il.lines(pc)) {
      final String[] tokens = LINE_SPLIT.split(line);
      final String[] targets = SEPARATOR.split(tokens[1]);
      final Integer key = Integer.valueOf(tokens[0]);
      final Integer[] value = Arrays.stream(targets).map(Integer::valueOf).toArray(Integer[]::new);
      result.put(key, value);
    }
    return result;
  }

  private static final Pattern LINE_SPLIT = Pattern.compile(" <-> ");

  private static final Pattern SEPARATOR = Pattern.compile(", ");
}
