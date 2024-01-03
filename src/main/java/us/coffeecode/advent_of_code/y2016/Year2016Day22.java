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

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2016, day = 22, title = "Grid Computing")
@Component
public final class Year2016Day22 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final List<Node> input = getInput(pc);
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

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    // Move data from (max,0) to (0,0)
    final Iterable<Node> input = getInput(pc);
    int x_max = 0;
    int y_max = 0;
    for (final Node node : input) {
      x_max = Math.max(x_max, node.x);
      y_max = Math.max(y_max, node.y);
    }
    final Node[][] grid = new Node[x_max + 1][y_max + 1];
    // Starting coordinates of the empty node.
    int start_x = -1;
    int start_y = -1;
    // The first x coordinate that is clear of the wall of immovable data.
    int x_clear = y_max;
    for (final Node node : input) {
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
  private List<Node> getInput(final PuzzleContext pc) {
    return il.lines(pc).stream().filter(s -> s.startsWith("/")).map(Node::make).toList();
  }

  private static record Node(String id, int x, int y, int size, int used, int avail, int pct) {

    private static final int PREFIX = "/dev/grid/".length();

    private static final Pattern SEPARATOR = Pattern.compile("\\s+");

    private static final Pattern NODE_SEPARATOR = Pattern.compile("-");

    static Node make(final String input) {
      final String[] tokens = SEPARATOR.split(input.substring(PREFIX));
      final String[] nodeTokens = NODE_SEPARATOR.split(tokens[0]);
      return new Node(tokens[0], Integer.parseInt(nodeTokens[1].substring(1)), Integer.parseInt(nodeTokens[2].substring(1)),
        Integer.parseInt(tokens[1].substring(0, tokens[1].length() - 1)),
        Integer.parseInt(tokens[2].substring(0, tokens[2].length() - 1)),
        Integer.parseInt(tokens[3].substring(0, tokens[3].length() - 1)),
        Integer.parseInt(tokens[4].substring(0, tokens[4].length() - 1)));
    }

    boolean isViableWith(final Node other) {
      if ((this == other) || (used == 0)) {
        return false;
      }
      return used <= other.avail;
    }

    /** Determine if this node is basically a lead brick that we need to work around. */
    public boolean isOversize() {
      return (size >= 100) || (used >= 100);
    }

  }

}
