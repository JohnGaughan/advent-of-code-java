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
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point2D;

@AdventOfCodeSolution(year = 2019, day = 13)
@Component
public final class Year2019Day13 {

  @Autowired
  private IntCodeFactory icf;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final IntCode state = icf.make(pc);
    state.exec();
    final long[] output = state.getOutput()
                               .removeAll();
    final Map<Point2D, Tile> tiles = new HashMap<>();
    for (int i = 0; i < output.length; i += 3) {
      tiles.put(new Point2D((int) output[i], (int) output[i + 1]), Tile.valueOf(output[i + 2]));
    }
    return tiles.values()
                .stream()
                .filter(t -> t == Tile.BLOCK)
                .count();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final Controller input = new Controller();
    final IntCode state = icf.make(pc, input, ExecutionOption.BLOCK_AFTER_THREE_OUTPUTS);
    state.getMemory()
         .set(0, 2);
    final Map<Point2D, Tile> tiles = new HashMap<>();
    long answer = 0;
    while (true) {
      // Run the simulation until it produces output, then decide how to proceed.
      state.exec();
      long[] results = state.getOutput()
                            .removeAll();

      // Invalid output.
      if ((results == null) || (results.length != 3)) {
        break;
      }

      // IntCode returned a score, and there are no more blocks.
      if ((results[0] == -1) && (results[1] == 0) && tiles.values()
                                                          .stream()
                                                          .filter(t -> t == Tile.BLOCK)
                                                          .count() == 0) {
        answer = results[2];
        break;
      }

      final Tile updating = Tile.valueOf(results[2]);
      if (updating != null) {
        tiles.put(new Point2D((int) results[0], (int) results[1]), updating);
      }

      // Update the paddle's position.
      if (Tile.HORIZONTAL_PADDLE == updating) {
        input.paddleX = results[0];
      }

      // Update the ball's position.
      if (Tile.BALL == updating) {
        input.ballX = results[0];
      }

    }
    return answer;
  }

  private static enum Tile {

    EMPTY(0, " "),
    WALL(1, "X"),
    BLOCK(2, "w"),
    HORIZONTAL_PADDLE(3, "-"),
    BALL(4, "o");

    public static final Tile valueOf(final long _id) {
      for (final Tile tile : values()) {
        if (tile.id == _id) {
          return tile;
        }
      }
      return null;
    }

    final long id;

    final String str;

    private Tile(final long _id, final String _str) {
      id = _id;
      str = _str;
    }

    @Override
    public String toString() {
      return str;
    }
  }

  /** Game controller that always moves the paddle toward the ball. */
  private static final class Controller
  implements IntCodeIoQueue {

    long paddleX = -1;

    long ballX = -1;

    @Override
    public void add(final long value) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void add(final long[] values) {
      throw new UnsupportedOperationException();
    }

    @Override
    public long remove() {
      if ((paddleX < 0) || (ballX < 0)) {
        return 0;
      }
      else if (ballX > paddleX) {
        return 1;
      }
      else if (ballX < paddleX) {
        return -1;
      }
      return 0;
    }

    @Override
    public long[] remove(final int quantity) {
      return new long[0];
    }

    @Override
    public long[] removeAll() {
      return new long[0];
    }

    @Override
    public void clear() {
      // Do nothing.
    }

    @Override
    public int size() {
      return 1;
    }

    @Override
    public boolean isEmpty() {
      return false;
    }

  }

}
