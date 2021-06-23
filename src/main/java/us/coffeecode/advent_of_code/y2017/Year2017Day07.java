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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2017/day/7">Year 2017, day 7</a>. This is a problem about balancing trees. Part one
 * asks for the root node, which verifies we construct the tree correctly. Part two asks for us to find a node in the
 * tree that is unbalanced, and find its correct weight to balance it.
 * </p>
 * <p>
 * Building the tree is simple. Figure out the root node, then recursively build its children and add them. For finding
 * the unbalanced node, we assume that a node must have three children to be unbalanced because one has to be different
 * from the others and this must be unambiguous. This works for the program input and its assumptions. From here we move
 * around the tree looking for the node that is unbalanced, but its children are balanced. That means that node itself
 * must have the wrong weight. Then we compare to its siblings and do a little math to figure out its correct weight. We
 * get the different which is the correct weight based on its siblings, minus the weight of the current node. We add
 * this to the weight of the current node only, which is the current node's total weight minus the weight of its
 * children. The current node's weight cancels out, giving a simplified expression.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2017Day07 {

  public String calculatePart1() {
    return getRootNode(getInput());
  }

  public long calculatePart2() {
    final Map<String, Disc> discs = getInput();
    final String rootName = getRootNode(discs);
    final Node root = makeTree(rootName, discs);
    Node parent = root;
    Node current = root;
    int weight = -1;
    while (true) {
      final Node unbalanced = current.getUnbalancedChild();
      if (unbalanced == null) {
        // This is the node that needs to change, but we need to access sibling nodes to know by how much.
        int correctWeight = 0;
        for (final Node sibling : parent.children) {
          if (sibling.weight != current.weight) {
            correctWeight = sibling.weight;
            break;
          }
        }
        // (correctWeight - current.weight) + (current.weight - current.childrenWeight)
        weight = correctWeight - current.childrenWeight;
        break;
      }
      else {
        // There is an unbalanced node, so iterate into it.
        parent = current;
        current = unbalanced;
      }
    }
    // NOT 1470
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
    return names.iterator().next();
  }

  /** Get the input data for this solution. */
  private Map<String, Disc> getInput() {
    try {
      return Files.readAllLines(Utils.getInput(2017, 7)).stream().map(Disc::new).collect(
        Collectors.toMap(d -> d.name, d -> d));
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
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
          int value = weights.get(key).intValue();
          weights.put(key, Integer.valueOf(value + 1));
        }
        else {
          weights.put(key, Integer.valueOf(1));
        }
      }
      // Get the weight where only one child has it.
      for (final Map.Entry<Integer, Integer> entry : weights.entrySet()) {
        if (entry.getValue().intValue() == 1) {
          int childWeight = entry.getKey().intValue();
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
      else if (!(obj instanceof Disc)) {
        return false;
      }
      return input.equals(((Disc) obj).input);
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
