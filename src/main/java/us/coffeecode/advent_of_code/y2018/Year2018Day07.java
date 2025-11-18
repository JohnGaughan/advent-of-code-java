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
package us.coffeecode.advent_of_code.y2018;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2018, day = 7)
@Component
public final class Year2018Day07 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public String calculatePart1(final PuzzleContext pc) {
    return solve(pc, 1).order;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return solve(pc, 5).time;
  }

  private Solution solve(final PuzzleContext pc, final int workerCount) {
    final Map<Integer, Node> nodes = getInput(pc);
    final int size = nodes.size();
    final StringBuilder result = new StringBuilder();
    final SortedSet<Integer> available = new TreeSet<>();
    int time = 0;

    final int[] working = new int[workerCount];
    final int[] completionTime = new int[workerCount];

    // While there are nodes left to consume, consume them.
    for (; result.length() < size; ++time) {
      // Check for nodes completed.
      for (int i = 0; i < workerCount; ++i) {
        if (working[i] >= 'A' && completionTime[i] == time) {
          // This node is completed: consume it.
          result.appendCodePoint(working[i]);
          final Integer completed = Integer.valueOf(working[i]);
          working[i] = 0;
          completionTime[i] = 0;
          nodes.remove(completed);
          for (final Node node : nodes.values()) {
            node.targets.remove(completed);
          }
        }
      }

      // Update the available steps with whatever is completed.
      for (final Node node : nodes.values()) {
        if (node.targets.isEmpty()) {
          final Integer value = Integer.valueOf(node.id);
          boolean add = true;
          for (final int inProgress : working) {
            if (inProgress == value.intValue()) {
              add = false;
              break;
            }
          }
          if (add) {
            available.add(value);
          }
        }
      }

      // Start work on new nodes
      final Iterator<Integer> iter = available.iterator();
      for (int i = 0; i < workerCount; ++i) {
        if (working[i] < 'A' && iter.hasNext()) {
          final Integer next = iter.next();
          iter.remove();
          working[i] = next.intValue();
          completionTime[i] = time + working[i] - 'A' + 61;
        }
      }
    }

    // Increments at the end of the loop, but there are no new tasks to start. This means all workers are idle, and they
    // found the solution during the previous time unit.
    --time;

    return new Solution(result.toString(), time);
  }

  /** Get the input data for this solution. */
  private Map<Integer, Node> getInput(final PuzzleContext pc) {
    // Construct a reverse digraph, directed toward where we need to start. Not all nodes are defined in the input, so
    // add them all first.
    final Map<Integer, Node> nodes = new HashMap<>();
    for (final Integer[] pair : il.lines(pc)
                                  .stream()
                                  .map(s -> Arrays.stream(new int[] { s.codePointAt(5), s.codePointAt(36) })
                                                  .boxed()
                                                  .toArray(Integer[]::new))
                                  .toArray(Integer[][]::new)) {
      nodes.computeIfAbsent(pair[0], i -> new Node(i.intValue()));
      nodes.computeIfAbsent(pair[1], i -> new Node(i.intValue()));
      nodes.get(pair[1]).targets.add(pair[0]);
    }
    return nodes;
  }

  private static record Solution(String order, long time) {}

  private static final class Node {

    final int id;

    final Set<Integer> targets = new HashSet<>();

    Node(final int _id) {
      id = _id;
    }

    @Override
    public String toString() {
      return targets.toString();
    }
  }

}
