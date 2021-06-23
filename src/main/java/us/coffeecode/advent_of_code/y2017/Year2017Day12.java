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

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2017/day/12">Year 2017, day 12</a>. This is an exercise in graph traversal. Given a
 * set of nodes and mapping between the nodes, part one asks us how many nodes are reachable from node 0: that is, how
 * many nodes are in its group. Part two asks us how many discrete groups of nodes there are.
 * </p>
 * <p>
 * The first task is isolating a group. Rather than building an actual graph, we can simply look up each node, one at a
 * time, and see where it leads, starting with an arbitrary starting node. This is a breadth-first traversal where we
 * omit paths to nodes already visited. As we visit nodes, add them to a set. Then this group is isolated. For part one
 * we can simply get the group for node zero and return its size. For part two, get an arbitrary node from the mapping
 * and find its group. Save the group into a collection of groups used solely to be able to get the number of groups.
 * Then remove everything in the group's set from the node mapping. Repeat until all mappings are exhausted. We are left
 * with a collection of discrete groups, which we can get its size for the puzzle answer.
 * </p>
 * <p>
 * This algorithm only works reliably if the input describes a bidirectional graph. However, this appears to be the case
 * for the provided input.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2017Day12 {

  public long calculatePart1() {
    final Map<Integer, Integer[]> nodeMapping = getInput();
    final Set<Integer> groupZero = getGroup(nodeMapping, Integer.valueOf(0));
    return groupZero.size();
  }

  public long calculatePart2() {
    final Map<Integer, Integer[]> nodeMapping = getInput();
    final Collection<Set<Integer>> groups = new ArrayList<>();
    while (!nodeMapping.isEmpty()) {
      final Integer start = nodeMapping.keySet().iterator().next();
      final Set<Integer> group = getGroup(nodeMapping, start);
      groups.add(group);
      for (Integer key : group) {
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
  private Map<Integer, Integer[]> getInput() {
    try {
      final Map<Integer, Integer[]> result = new HashMap<>(2700);
      for (final String line : Files.readAllLines(Utils.getInput(2017, 12))) {
        final String[] tokens = LINE_SPLIT.split(line);
        final String[] targets = SEPARATOR.split(tokens[1]);
        final Integer key = Integer.valueOf(tokens[0]);
        final Integer[] value = Arrays.stream(targets).map(Integer::valueOf).toArray(Integer[]::new);
        result.put(key, value);
      }
      return result;
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private static final Pattern LINE_SPLIT = Pattern.compile(" <-> ");

  private static final Pattern SEPARATOR = Pattern.compile(", ");

}
