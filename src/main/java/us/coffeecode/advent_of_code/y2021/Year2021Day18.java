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
package us.coffeecode.advent_of_code.y2021;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2021, day = 18)
@Component
public final class Year2021Day18 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    Node root = null;
    for (final Node node : il.linesAsObjects(pc, this::parse)) {
      if (root == null) {
        root = node;
      }
      else {
        root = add(root, node);
        reduce(root);
      }
    }
    return (root == null) ? Long.MIN_VALUE : root.getMagnitude();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    long max = Long.MIN_VALUE;
    final List<Node> nodes = il.linesAsObjects(pc, this::parse);
    for (int i = 0; i < nodes.size() - 1; ++i) {
      for (int j = i + 1; j < nodes.size(); ++j) {
        final Node node1 = add(nodes.get(i)
                                    .copy(),
          nodes.get(j)
               .copy());
        final Node node2 = add(nodes.get(j)
                                    .copy(),
          nodes.get(i)
               .copy());
        reduce(node1);
        reduce(node2);
        max = Math.max(max, node1.getMagnitude());
        max = Math.max(max, node2.getMagnitude());
      }
    }
    return max;
  }

  /** Add two trees together into a larger tree. */
  private Node add(final Node left, final Node right) {
    final Node root = new Node();
    root.left = left;
    root.right = right;
    root.right.parent = root.left.parent = root;
    return root;
  }

  /** Reduce a tree completely until no more reduction operations are possible. */
  private void reduce(final Node root) {
    while (explode(root, 1) || split(root)) {
      // The work is in the control statement.
    }
  }

  /** Perform the explode operation, distributing too-deep nodes to other nodes. */
  private boolean explode(final Node root, final int depth) {
    // Need to do an in-order traversal of the leaf nodes, looking for the first (left-most) node at depth greater than
    // 4 with two leaf nodes.
    boolean updated = false;

    if (!root.isLeaf()) {
      // Explode this node.
      if ((depth > 4) && root.left.isLeaf() && root.right.isLeaf()) {
        final Node previous = getInOrderPreviousLeaf(root);
        if (previous != null) {
          previous.value += root.left.value;
        }
        final Node next = getInOrderNextLeaf(root);
        if (next != null) {
          next.value += root.right.value;
        }
        root.left = null;
        root.right = null;
        root.value = 0;
      }
      else {
        updated = explode(root.left, depth + 1);
        if (!updated) {
          updated = explode(root.right, depth + 1);
        }
      }
    }
    return updated;
  }

  /** Get the next leaf node in a reverse in-order traversal. */
  private Node getInOrderPreviousLeaf(final Node node) {
    Node previous = node;
    Node current = node.parent;

    // Go up until there is a left child.
    while (previous == current.left) {
      previous = previous.parent;
      current = current.parent;
      if (current == null) {
        return null;
      }
    }
    current = current.left;

    // Now find the right-most child.
    while ((current != null) && !current.isLeaf()) {
      current = current.right;
    }

    return current;
  }

  /** Get the next leaf node in an in-order traversal. */
  private Node getInOrderNextLeaf(final Node node) {
    Node previous = node;
    Node current = node.parent;

    // Go up until there is a right child.
    while (previous == current.right) {
      previous = previous.parent;
      current = current.parent;
      if (current == null) {
        return null;
      }
    }
    current = current.right;

    // Now find the left-most child.
    while ((current != null) && !current.isLeaf()) {
      current = current.left;
    }

    return current;
  }

  /** Perform an in-order traversal of leaf nodes until we find a value greater than ten. */
  private boolean split(final Node root) {
    boolean updated = false;
    if (!root.isLeaf()) {
      updated = split(root.left);
      if (!updated) {
        updated = split(root.right);
      }
    }
    else if (root.value > 9) {
      root.left = new Node();
      root.left.parent = root;
      root.right = new Node();
      root.right.parent = root;
      // Left is rounded down: integer division/shift does this.
      root.left.value = root.value >> 1;
      // Right is whatever is left over.
      root.right.value = root.value - root.left.value;
      root.value = 0;
      updated = true;
    }
    return updated;
  }

  private Node parse(final String line) {
    final int[] codePoints = line.codePoints()
                                 .toArray();
    return makeNode(codePoints, 0, codePoints.length);
  }

  private Node makeNode(final int[] codePoints, final int start, final int end) {
    // [1,2]
    final Node node = new Node();
    if (end == start + 1) {
      node.value = codePoints[start] - '0';
      return node;
    }
    // Find the comma that belongs to this node.
    int midPoint = start + 1;
    int level = 0;
    while (true) {
      if (codePoints[midPoint] == '[') {
        ++level;
      }
      else if (codePoints[midPoint] == ']') {
        --level;
      }
      else if ((codePoints[midPoint] == ',') && (level == 0)) {
        break;
      }
      ++midPoint;
    }
    node.left = makeNode(codePoints, start + 1, midPoint);
    node.left.parent = node;
    node.right = makeNode(codePoints, midPoint + 1, end - 1);
    node.right.parent = node;
    return node;
  }

  private static final class Node {

    Node parent;

    Node left;

    Node right;

    long value;

    Node copy() {
      // Cloneable is obsolete trash.
      Node copy = new Node();
      if (isLeaf()) {
        copy.value = value;
      }
      else {
        copy.left = left.copy();
        copy.right = right.copy();
        copy.left.parent = copy.right.parent = copy;
      }
      return copy;
    }

    boolean isLeaf() {
      return (left == null) && (right == null);
    }

    long getMagnitude() {
      if (isLeaf()) {
        return value;
      }
      return (3 * left.getMagnitude()) + (right.getMagnitude() << 1);
    }

    @Override
    public String toString() {
      if (isLeaf()) {
        return Long.toString(value);
      }
      return "[" + left + "," + right + "]";
    }

  }

}
