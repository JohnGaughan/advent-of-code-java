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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2024, day = 23)
@Component
public class Year2024Day23 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Map<String, Set<String>> connections = getConnections(pc);
    final Set<Set<String>> triplets = new HashSet<>();
    // O(n^3) algorithm does not scale, but does not need to with these inputs.
    for (final var entry : connections.entrySet()) {
      final String root = entry.getKey();
      for (final String target : entry.getValue()) {
        for (final String target2 : connections.get(target)) {
          // If these vertices are unique and fully-connected, create a triplet.
          if (!root.equals(target2) && connections.get(root)
                                                  .contains(target2)) {
            final var candidate = Set.of(root, target, target2);
            // Triplet is only valid if at least one vertex starts with "t"
            if (candidate.stream()
                         .anyMatch(s -> s.startsWith("t"))) {
              triplets.add(Set.of(root, target, target2));
            }
          }
        }
      }
    }
    return triplets.size();
  }

  @Solver(part = 2)
  public String calculatePart2(final PuzzleContext pc) {
    final Map<String, Set<String>> connections = getConnections(pc);
    Set<String> largestClique = Collections.emptySet();
    // Iterate through each vertex and check its connections. One will be fully-connected.
    for (var connection : connections.entrySet()) {
      // Copy the connections to a list to allow index-based access so we can perform fewer checks.
      final List<String> neighbors = new ArrayList<>(connection.getValue());
      final Set<String> clique = new HashSet<>();
      clique.add(connection.getKey());
      // Iterate through all neighbor vertices of the current vertex.
      for (int i = 0; i < neighbors.size() - 1; ++i) {
        final String v1 = neighbors.get(i);
        // Neighbor must be connected to everything in the clique so far.
        if (connections.get(v1)
                       .containsAll(clique)) {
          // Check first neighbor against other neighbors, but only in one direction.
          for (int j = i + 1; j < neighbors.size(); ++j) {
            final String v2 = neighbors.get(j);
            final Set<String> c2 = connections.get(v2);
            // Second neighbor must be connected to the first neighbor and the clique so far.
            if (c2.contains(v1) && c2.containsAll(clique)) {
              clique.add(v1);
              clique.add(v2);
            }
          }
        }
      }
      // If this clique is larger than the largest one found thus far, save it as the new largest.
      if (clique.size() > largestClique.size()) {
        largestClique = clique;
      }
    }
    // Return the largest clique formatted per the requirements.
    return largestClique.stream()
                        .sorted()
                        .collect(Collectors.joining(","));
  }

  /** Get a mapping of each vertex to its neighbors in the graph. */
  private Map<String, Set<String>> getConnections(final PuzzleContext pc) {
    final List<List<String>> pairs = il.linesAsStrings(pc, SPLIT);
    final Map<String, Set<String>> connections = new HashMap<>();
    for (final List<String> connection : pairs) {
      connections.computeIfAbsent(connection.get(0), k -> new HashSet<>());
      connections.computeIfAbsent(connection.get(1), k -> new HashSet<>());
      connections.get(connection.get(0))
                 .add(connection.get(1));
      connections.get(connection.get(1))
                 .add(connection.get(0));
    }
    return connections;
  }

  private static final Pattern SPLIT = Pattern.compile("-");
}
