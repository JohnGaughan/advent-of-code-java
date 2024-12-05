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
package us.coffeecode.advent_of_code.y2019;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2019, day = 6, title = "Universal Orbit Map")
@Component
public final class Year2019Day06 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Node root = getInput(pc);
    return root.getTotalDepth();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final Node root = getInput(pc);
    final Node you = root.get("YOU");
    final Node santa = root.get("SAN");
    Node parent = you;
    while (!parent.contains("SAN")) {
      parent = parent.parent;
    }
    int myDepth = you.depth - parent.depth - 1;
    int santaDepth = santa.depth - parent.depth - 1;
    return myDepth + santaDepth;
  }

  /** Get the input data for this solution. */
  private Node getInput(final PuzzleContext pc) {
    return buildTree(il.lines(pc)
                       .stream()
                       .map(s -> LINE_SPLIT.split(s))
                       .toArray(String[][]::new));
  }

  private static final Pattern LINE_SPLIT = Pattern.compile("\\)");

  private Node buildTree(final String[][] pairs) {
    final Map<String, Set<String>> parentChild = new HashMap<>();
    final Map<String, String> childParent = new HashMap<>();
    for (final String[] pair : pairs) {
      if (!parentChild.containsKey(pair[0])) {
        parentChild.put(pair[0], new HashSet<>());
      }
      parentChild.get(pair[0])
                 .add(pair[1]);
      childParent.put(pair[1], pair[0]);
    }

    final Node root = new Node("COM", null, 0);
    final Queue<Node> processing = new LinkedList<>();
    processing.add(root);

    while (!processing.isEmpty()) {
      final Node next = processing.remove();
      if (parentChild.containsKey(next.id)) {
        final int depth = next.depth + 1;
        for (final String childId : parentChild.get(next.id)) {
          final Node child = new Node(childId, next, depth);
          next.children.add(child);
          processing.add(child);
        }
      }
    }

    return root;
  }

  private static final class Node {

    final String id;

    final Node parent;

    final int depth;

    final Set<Node> children = new HashSet<>();

    Node(final String _id, final Node _parent, final int _depth) {
      id = _id;
      parent = _parent;
      depth = _depth;
    }

    Node get(final String _id) {
      if (id.equals(_id)) {
        return this;
      }
      for (final Node child : children) {
        final Node found = child.get(_id);
        if (found != null) {
          return found;
        }
      }
      return null;
    }

    boolean contains(final String _id) {
      if (id.equals(_id)) {
        return true;
      }
      for (final Node child : children) {
        if (child.contains(_id)) {
          return true;
        }
      }
      return false;
    }

    int getTotalDepth() {
      int result = depth;
      for (final Node child : children) {
        result += child.getTotalDepth();
      }
      return result;
    }

    @Override
    public boolean equals(final Object obj) {
      if (obj == this) {
        return true;
      }
      else if (obj instanceof Node o) {
        return Objects.equals(id, o.id);
      }
      return false;
    }

    @Override
    public int hashCode() {
      return id.hashCode();
    }

    @Override
    public String toString() {
      return id + "=" + children;
    }

  }

}
