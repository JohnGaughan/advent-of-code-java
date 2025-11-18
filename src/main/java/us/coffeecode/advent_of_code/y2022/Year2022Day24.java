/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2022  John Gaughan
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
package us.coffeecode.advent_of_code.y2022;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.MyIntMath;
import us.coffeecode.advent_of_code.util.Point2D;

@AdventOfCodeSolution(year = 2022, day = 24)
@Component
public class Year2022Day24 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Input input = getInput(pc);
    final State state = calculate(input, new State(START, 0, 0, START.getManhattanDistance(input.target)), input.target);
    return state.score;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final Input input = getInput(pc);
    final State first = calculate(input, new State(START, 0, 0, START.getManhattanDistance(input.target)), input.target);
    final State second =
      calculate(input, new State(input.target, first.board, 0, START.getManhattanDistance(input.target)), START);
    final State third =
      calculate(input, new State(START, second.board, 0, START.getManhattanDistance(input.target)), input.target);
    return first.score + second.score + third.score;
  }

  private static final Point2D START = new Point2D(0, -1);

  private State calculate(final Input input, final State start, final Point2D target) {
    final Queue<State> queue = new LinkedList<>();
    final Set<State> seen = new HashSet<>(1 << 18);
    queue.add(start);
    State bestState = null;
    while (!queue.isEmpty()) {
      final State current = queue.remove();
      if ((bestState != null) && (bestState.score <= current.score)) {
        continue;
      }
      final int nextBoardId = (current.board + 1) % input.boards.length;
      final boolean[][] nextBoard = input.boards[nextBoardId];

      // If we are one unit away from the target, then the only move worth considering is to move to it.
      if (target.getManhattanDistance(current.location) == 1) {
        final State next = new State(current.location, nextBoardId, current.score + 1, current.distanceToTarget);
        if ((bestState == null) || (next.score < bestState.score)) {
          bestState = next;
        }
        continue;
      }

      // Can we wait in place?
      if (start.location.equals(current.location) || current.location.get(nextBoard)) {
        final State next = new State(current.location, nextBoardId, current.score + 1, current.distanceToTarget);
        if (((bestState == null) || (next.score + next.distanceToTarget < bestState.score)) && !seen.contains(next)) {
          seen.add(next);
          queue.add(next);
        }
      }

      // Try moving to each neighbor.
      for (final Point2D neighbor : current.location.getCardinalNeighbors()) {
        if (neighbor.isIn(nextBoard) && neighbor.get(nextBoard)) {
          final State next = new State(neighbor, nextBoardId, current.score + 1, neighbor.getManhattanDistance(target));
          if (((bestState == null) || (next.score + next.distanceToTarget < bestState.score)) && !seen.contains(next)) {
            seen.add(next);
            queue.add(next);
          }
        }
      }
    }
    return bestState;
  }

  private record State(Point2D location, int board, long score, int distanceToTarget) {}

  private record Input(Point2D target, boolean[][][] boards) {}

  private Input getInput(final PuzzleContext pc) {
    final int[][] input = loadInput(pc);

    // Get the least common multiple. This is the number of unique states, where the wind pattern repeats.
    final int height = input.length;
    final int width = input[0].length;
    final int lcm = MyIntMath.lcm(width, height);

    // Initialize stuff needed to create the input object.
    final boolean[][][] boards = new boolean[lcm][height][width];

    // Initialize each board.
    for (int i = 0; i < lcm; ++i) {
      for (int y = 0; y < height; ++y) {
        final int y_up = (y + i) % height;
        final int y_down = (y - i + lcm) % height;
        for (int x = 0; x < width; ++x) {
          final int x_left = (x + i) % width;
          final int x_right = (x - i + lcm) % width;
          boards[i][y][x] =
            (input[y_up][x] != '^') && (input[y_down][x] != 'v') && (input[y][x_left] != '<') && (input[y][x_right] != '>');
        }
      }
    }

    return new Input(new Point2D(width - 1, height), boards);
  }

  private int[][] loadInput(final PuzzleContext pc) {
    final List<String> lines = il.lines(pc);
    final int[][] array = new int[lines.size() - 2][];
    for (int y = 1; y < lines.size() - 1; ++y) {
      final String line = lines.get(y);
      array[y - 1] = line.substring(1, line.length() - 1)
                         .codePoints()
                         .toArray();
    }
    return array;
  }
}
