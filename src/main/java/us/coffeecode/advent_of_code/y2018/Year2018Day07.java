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

import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2018/day/7">Year 2018, day 7</a>. This is a problem about directed graphs. There
 * are a number of steps to complete and they must be completed in a certain order. Part one asks for the completion
 * order assuming one person working on the tasks, while part two asks for the completion order with five workers and a
 * variable amount of time required for each task.
 * </p>
 * <p>
 * I chose to model this is a digraph where edges point toward the start of the graph. This makes it easy to see which
 * nodes have dependencies, since they contain their own dependencies instead of needing to search other nodes. At any
 * given stage I can simply search for nodes with empty dependencies then choose a node. I also used one algorithm for
 * both parts, even though part one is simpler. There is less duplication this way.
 * </p>
 * <p>
 * Keep track of what tasks each worker is performing, as well as the completion time. Each tick, see if any nodes are
 * completed. If so, add them to the output and idle its worker. Next, update the nodes that are available to work. This
 * would ideally use a priority set if such a thing existed in the Java libraries. Instead, I use a TreeSet which is
 * fine for the tiny amount of data and has both properties I need. Once that is done, start work if any workers are
 * available and there is anything to work on.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2018Day07 {

  public String calculatePart1() {
    return solve(1).order;
  }

  public long calculatePart2() {
    return solve(5).time;
  }

  private Solution solve(final int workerCount) {
    final Map<Integer, Node> nodes = getInput();
    final StringBuilder result = new StringBuilder();
    final SortedSet<Integer> available = new TreeSet<>();
    int time = 0;

    final int[] working = new int[workerCount];
    final int[] completionTime = new int[workerCount];

    // While there are nodes left to consume, consume them.
    for (; result.length() != 26; ++time) {
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
  private Map<Integer, Node> getInput() {
    try {
      // Construct a reverse digraph, directed toward where we need to start. Not all nodes are defined in the input, so
      // add them all first.
      final Map<Integer, Node> nodes = new HashMap<>();
      for (int i = 'A'; i <= 'Z'; ++i) {
        nodes.put(Integer.valueOf(i), new Node(i));
      }
      for (final int[] pair : Files.readAllLines(Utils.getInput(2018, 7)).stream().map(
        s -> Arrays.stream(new int[] { s.codePointAt(5), s.codePointAt(36) }).toArray()).toArray(int[][]::new)) {
        nodes.get(Integer.valueOf(pair[1])).targets.add(Integer.valueOf(pair[0]));
      }
      return nodes;
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private static final class Solution {

    final String order;

    final int time;

    Solution(final String _order, final int _time) {
      order = _order;
      time = _time;
    }
  }

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
