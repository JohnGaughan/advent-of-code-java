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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2017, day = 7, title = "Recursive Circus")
@Component
public final class Year2017Day07 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public String calculatePart1(final PuzzleContext pc) {
    return getRootNode(il.linesAsMap(pc, Disc::new, d -> d.name, Function.identity()));
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final Map<String, Disc> discs = il.linesAsMap(pc, Disc::new, d -> d.name, Function.identity());
    final String rootName = getRootNode(discs);
    final Node root = makeTree(rootName, discs);
    Node parent = root;
    Node current = root;
    long weight = 0;
    while (true) {
      final Node unbalanced = current.getUnbalancedChild();
      if (unbalanced == null) {
        // This is the node that needs to change, but we need to access sibling nodes to know by how much.
        long correctWeight = 0;
        for (final Node sibling : parent.children) {
          if (sibling.weight != current.weight) {
            correctWeight = sibling.weight;
            break;
          }
        }
        weight = correctWeight - current.childrenWeight;
        break;
      }
      else {
        // There is an unbalanced node, so iterate into it.
        parent = current;
        current = unbalanced;
      }
    }
    return weight;
  }

  private Node makeTree(final String discName, final Map<String, Disc> discs) {
    final Disc disc = discs.get(discName);
    final List<Node> children = new ArrayList<>(disc.children.size());
    for (final String childName : disc.children) {
      children.add(makeTree(childName, discs));
    }
    return new Node(disc, children);
  }

  private String getRootNode(final Map<String, Disc> discs) {
    final Set<String> names = new HashSet<>(discs.keySet());
    for (final Disc disc : discs.values()) {
      for (final String name : disc.children) {
        names.remove(name);
      }
    }
    return names.iterator()
                .next();
  }

  private static final class Node {

    final Disc disc;

    final List<Node> children;

    final int weight;

    final int childrenWeight;

    Node(final Disc _disc, final List<Node> _children) {
      disc = _disc;
      children = _children;
      int _childrenWeight = 0;
      for (final Node child : children) {
        _childrenWeight += child.weight;
      }
      childrenWeight = _childrenWeight;
      weight = childrenWeight + disc.weight;
    }

    Node getUnbalancedChild() {
      // Map weights to how many children have that weight.
      final Map<Integer, Integer> weights = new HashMap<>();
      for (final Node child : children) {
        Integer key = Integer.valueOf(child.weight);
        if (weights.containsKey(key)) {
          int value = weights.get(key)
                             .intValue();
          weights.put(key, Integer.valueOf(value + 1));
        }
        else {
          weights.put(key, Integer.valueOf(1));
        }
      }
      // Get the weight where only one child has it.
      for (final Map.Entry<Integer, Integer> entry : weights.entrySet()) {
        if (entry.getValue()
                 .intValue() == 1) {
          int childWeight = entry.getKey()
                                 .intValue();
          for (final Node child : children) {
            if (child.weight == childWeight) {
              return child;
            }
          }
        }
      }
      // No unbalanced child
      return null;
    }

    @Override
    public String toString() {
      return disc.name + " = " + weight;
    }
  }

  private static final class Disc {

    private static final String CHILDREN_SEPARATOR = "-> ";

    private static final Pattern LIST_SEPARATOR = Pattern.compile(", ");

    final String name;

    final int weight;

    final List<String> children;

    private final String input;

    Disc(final String _input) {
      input = _input;
      final int openParen = _input.indexOf('(');
      final int closeParen = _input.indexOf(')', openParen);
      name = _input.substring(0, openParen - 1);
      weight = Integer.parseInt(_input.substring(openParen + 1, closeParen));
      final int childrenSeparator = _input.indexOf(CHILDREN_SEPARATOR, closeParen);
      if (childrenSeparator < 0) {
        children = Collections.emptyList();
      }
      else {
        String childrenRaw = _input.substring(childrenSeparator + CHILDREN_SEPARATOR.length());
        children = Arrays.asList(LIST_SEPARATOR.split(childrenRaw));
      }
    }

    @Override
    public boolean equals(final Object obj) {
      if (this == obj) {
        return true;
      }
      else if (obj instanceof Disc o) {
        return input.equals(o.input);
      }
      return false;
    }

    @Override
    public int hashCode() {
      return input.hashCode();
    }

    @Override
    public String toString() {
      return input;
    }
  }

}
