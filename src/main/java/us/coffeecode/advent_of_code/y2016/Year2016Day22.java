/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2020  John Gaughan
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
package us.coffeecode.advent_of_code.y2016;

import java.nio.file.Files;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2016/day/22">Year 2016, day 22</a>. This problem asks us to evaluate a grid of
 * nodes. These nodes represent file servers containing data. Part one is a sanity check on reading and interpreting the
 * data. Part two requires us to move data around in the grid following specific rules until the required data traverses
 * from one node to another. This is similar to a maze, except we may move data that we do not need in order to make
 * room: this is similar to being able to move maze walls following a set of rules.
 * </p>
 * <p>
 * Part one is trivial and there is not anything important to note about it. Part two requires some human analysis to
 * arrive at anything resembling an efficient solution. The grid is has three types of nodes. First is an empty node.
 * Next is a node with so much data it cannot possibly fit anywhere else. Last is a "regular" node whose data can fit on
 * any other node that is not oversize. Looking at it this way, the grid is similar to a larger version of a
 * <a href="https://en.wikipedia.org/wiki/15_puzzle">15 puzzle</a> with an immovable wall. bunch of squares with one
 * square missing. You need to shuffle the pieces around, one at a time, to get the pieces in the correct locations. In
 * this case, we only care about one piece of data being in the correct location.
 * </p>
 * <p>
 * Visualizing the grid, there is a horizontal wall of immovable data near the bottom. We need to move the empty node
 * left far enough to clear the wall, then all the way up to the top row. Next we need to move it all the way to the top
 * right corner, which also moves the target data one cell left. From here we perform a series of five-move shuffles to
 * move the data one cell to the left each time. We repeat this for as many cells as we need to move it left. The
 * algorithm simply calculates these moves from the input data without actually rearranging anything.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2016Day22 {

  public long calculatePart1() {
    final List<Node> input = getInput();
    long viablePairs = 0;
    for (int i = 0; i < input.size() - 1; ++i) {
      final Node a = input.get(i);
      for (int j = i + 1; j < input.size(); ++j) {
        final Node b = input.get(j);
        if (a.isViableWith(b)) {
          ++viablePairs;
        }
        if (b.isViableWith(a)) {
          ++viablePairs;
        }
      }
    }
    return viablePairs;
  }

  public long calculatePart2() {
    // Move data from (max,0) to (0,0)
    final List<Node> input = getInput();
    int x_max = 0;
    int y_max = 0;
    for (Node node : input) {
      x_max = Math.max(x_max, node.x);
      y_max = Math.max(y_max, node.y);
    }
    final Node[][] grid = new Node[x_max + 1][y_max + 1];
    // Starting coordinates of the empty node.
    int start_x = -1;
    int start_y = -1;
    // The first x coordinate that is clear of the wall of immovable data.
    int x_clear = y_max;
    for (Node node : input) {
      grid[node.x][node.y] = node;
      if (node.used == 0) {
        start_x = node.x;
        start_y = node.y;
      }
      else if (node.isOversize()) {
        x_clear = Math.min(x_clear, node.x);
      }
    }

    // Move left far enough to clear the wall.
    long moves = start_x - x_clear + 1;
    // Move to the top row
    moves += start_y;
    // Move to the right column
    moves += x_max - x_clear + 1;
    // Shuffle the data left, fives moves per node.
    moves += 5 * (x_max - 1);
    return moves;
  }

  /** Get the input data for this solution. */
  private List<Node> getInput() {
    try {
      return Files.readAllLines(Utils.getInput(2016, 22)).stream().filter(s -> s.startsWith("/")).map(
        Node::new).collect(Collectors.toList());
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private static final class Node {

    private static final int PREFIX = "/dev/grid/".length();

    private static final Pattern SEPARATOR = Pattern.compile("\\s+");

    private static final Pattern NODE_SEPARATOR = Pattern.compile("-");

    final String id;

    final int x;

    final int y;

    final int size;

    final int used;

    final int avail;

    final int pct;

    final String toString;

    Node(final String input) {
      final String[] tokens = SEPARATOR.split(input.substring(PREFIX));
      id = tokens[0];
      final String[] nodeTokens = NODE_SEPARATOR.split(id);
      x = Integer.parseInt(nodeTokens[1].substring(1));
      y = Integer.parseInt(nodeTokens[2].substring(1));
      size = Integer.parseInt(tokens[1].substring(0, tokens[1].length() - 1));
      used = Integer.parseInt(tokens[2].substring(0, tokens[2].length() - 1));
      avail = Integer.parseInt(tokens[3].substring(0, tokens[3].length() - 1));
      pct = Integer.parseInt(tokens[4].substring(0, tokens[4].length() - 1));
      toString =
        id + "(x=" + x + ",y=" + y + ",Size=" + size + ",Used=" + used + ",Avail=" + avail + ",Use%=" + pct + ")";
    }

    boolean isViableWith(final Node other) {
      if (this == other || used == 0) {
        return false;
      }
      return used <= other.avail;
    }

    /** Determine if this node is basically a lead brick that we need to work around. */
    public boolean isOversize() {
      return size >= 100 || used >= 100;
    }

    @Override
    public String toString() {
      return toString;
    }
  }

}
