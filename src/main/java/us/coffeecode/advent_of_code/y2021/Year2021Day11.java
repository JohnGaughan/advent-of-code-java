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

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point2D;

@AdventOfCodeSolution(year = 2021, day = 11, title = "Dumbo Octopus")
@Component
public final class Year2021Day11 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    long flashes = 0;
    final int[][] board = il.linesAs2dIntArrayFromDigits(pc);
    for (int i = 0; i < 100; ++i) {
      flashes += iteration(board);
    }
    return flashes;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final int[][] board = il.linesAs2dIntArrayFromDigits(pc);
    final long boardSize = board.length * board[0].length;
    for (int i = 1; i < Integer.MAX_VALUE; ++i) {
      final long flashes = iteration(board);
      if (flashes == boardSize) {
        return i;
      }
    }
    return 0;
  }

  private long iteration(final int[][] board) {
    // Start by incrementing each position by one.
    for (int y = 0; y < board.length; ++y) {
      for (int x = 0; x < board[y].length; ++x) {
        ++board[y][x];
      }
    }
    // Now look for flashes and process each one. An octopus can flash only once per iteration, so track which ones
    // flashed already so we do not process them again.
    boolean flashed = true;
    final Set<Point2D> flashes = new HashSet<>(128);
    while (flashed) {
      flashed = false;
      for (int y = 0; y < board.length; ++y) {
        for (int x = 0; x < board[y].length; ++x) {
          // This position flashes: mark it as flashed, reset its energy, then update its neighbors.
          if (board[y][x] > 9) {
            final Point2D point = new Point2D(x, y);
            flashes.add(point);
            flashed = true;
            board[y][x] = 0;
            for (final Delta delta : DELTAS) {
              final int x1 = x + delta.dx;
              final int y1 = y + delta.dy;
              final Point2D neighbor = new Point2D(x1, y1);
              // Neighbor has not flashed yet, so it is safe to update it. If it already flashed then leave it alone.
              if (neighbor.isIn(board) && !flashes.contains(neighbor)) {
                ++board[y1][x1];
              }
            }
          }
        }
      }
    }
    return flashes.size();
  }

  private record Delta(int dx, int dy) {}

  private static final Collection<Delta> DELTAS = List.of(new Delta(-1, -1), new Delta(0, -1), new Delta(1, -1), new Delta(-1, 0),
    new Delta(1, 0), new Delta(-1, 1), new Delta(0, 1), new Delta(1, 1));
}
