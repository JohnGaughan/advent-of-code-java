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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.MutablePoint2D;

@AdventOfCodeSolution(year = 2018, day = 13, title = "Mine Cart Madness")
@Component
public final class Year2018Day13 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public String calculatePart1(final PuzzleContext pc) {
    return calculate(pc);
  }

  @Solver(part = 2)
  public String calculatePart2(final PuzzleContext pc) {
    return calculate(pc);
  }

  private String calculate(final PuzzleContext pc) {
    final boolean removeCrashes = pc.getBoolean("removeCrashes");
    final State state = new State(il.linesAsCodePoints(pc));
    while (!state.carts.isEmpty()) {
      final Collection<Cart> processedCarts = new ArrayList<>(state.carts.size());
      while (!state.carts.isEmpty()) {
        final Cart cart = state.carts.remove();

        // Move the cart forward one location.
        cart.location.addX(cart.d.dx);
        cart.location.addY(cart.d.dy);

        // Check for collision
        boolean collision = false;
        Iterator<Cart> iter = processedCarts.iterator();
        while (iter.hasNext()) {
          if (iter.next().collidesWith(cart)) {
            if (removeCrashes) {
              collision = true;
              iter.remove();
            }
            else {
              return cart.toString();
            }
          }
        }
        iter = state.carts.iterator();
        while (iter.hasNext()) {
          if (iter.next().collidesWith(cart)) {
            if (removeCrashes) {
              collision = true;
              iter.remove();
            }
            else {
              return cart.toString();
            }
          }
        }

        // No collision: update the current cart.
        if (!collision) {
          final int ch = cart.location.get(state.map);
          if ((ch == '/') || (ch == '\\')) {
            cart.d = cart.d.corner(ch);
          }
          else if (ch == '+') {
            cart.d = cart.d.turn(cart.nextTurn);
            cart.nextTurn = cart.nextTurn.next();
          }
          processedCarts.add(cart);
        }
      }

      // Reset the carts back into the state and check for the exit condition of part two.
      state.carts.addAll(processedCarts);
      if (state.carts.size() == 1) {
        return state.carts.remove().toString();
      }
    }
    return "NO SOLUTION";
  }

  private static final class State {

    final int[][] map;

    final Queue<Cart> carts = new PriorityQueue<>(17);

    State(final int[][] _map) {
      map = _map;
      for (int y = 0; y < map.length; ++y) {
        for (int x = 0; x < map[y].length; ++x) {
          final Direction d = Direction.valueOf(map[y][x]);
          if (d != null) {
            map[y][x] = (d == Direction.NORTH) ? '|' : '-';
            carts.add(new Cart(x, y, d));
          }
        }
      }
    }

  }

  private static enum Direction {

    NORTH(0, -1, '^'),
    SOUTH(0, 1, 'v'),
    EAST(1, 0, '>'),
    WEST(-1, 0, '<');

    private static final Map<Direction, Map<Integer, Direction>> CORNERS = Map.of( //
      NORTH, Map.of(Integer.valueOf('\\'), WEST, Integer.valueOf('/'), EAST), //
      SOUTH, Map.of(Integer.valueOf('\\'), EAST, Integer.valueOf('/'), WEST), //
      EAST, Map.of(Integer.valueOf('\\'), SOUTH, Integer.valueOf('/'), NORTH), //
      WEST, Map.of(Integer.valueOf('\\'), NORTH, Integer.valueOf('/'), SOUTH));

    private static final Map<Direction, Map<Turn, Direction>> TURNS = Map.of( //
      NORTH, Map.of(Turn.LEFT, WEST, Turn.FORWARD, NORTH, Turn.RIGHT, EAST), //
      SOUTH, Map.of(Turn.LEFT, EAST, Turn.FORWARD, SOUTH, Turn.RIGHT, WEST), //
      EAST, Map.of(Turn.LEFT, NORTH, Turn.FORWARD, EAST, Turn.RIGHT, SOUTH), //
      WEST, Map.of(Turn.LEFT, SOUTH, Turn.FORWARD, WEST, Turn.RIGHT, NORTH));

    static Direction valueOf(final int codePoint) {
      for (final Direction d : values()) {
        if (d.ch == codePoint) {
          return d;
        }
      }
      return null;
    }

    final int dx;

    final int dy;

    final int ch;

    Direction(final int _dx, final int _dy, final int _ch) {
      dx = _dx;
      dy = _dy;
      ch = _ch;
    }

    Direction corner(final int c) {
      return CORNERS.get(this).get(Integer.valueOf(c));
    }

    Direction turn(final Turn turn) {
      return TURNS.get(this).get(turn);
    }
  }

  private static enum Turn {

    LEFT,
    FORWARD,
    RIGHT;

    private static final Map<Turn, Turn> TRANSITIONS = Map.of(LEFT, FORWARD, FORWARD, RIGHT, RIGHT, LEFT);

    Turn next() {
      return TRANSITIONS.get(this);
    }
  }

  private static final class Cart
  implements Comparable<Cart> {

    final MutablePoint2D location;

    Direction d;

    Turn nextTurn = Turn.LEFT;

    Cart(final int x, final int y, final Direction _d) {
      location = new MutablePoint2D(x, y);
      d = _d;
    }

    boolean collidesWith(final Cart other) {
      return location.equals(other.location);
    }

    @Override
    public int compareTo(final Cart o) {
      final int y_result = Integer.compare(location.getY(), o.location.getY());
      if (y_result != 0) {
        return y_result;
      }
      return Integer.compare(location.getX(), o.location.getX());
    }

    @Override
    public String toString() {
      // Cannot use the built-in toString because it is formatted incorrectly for the problem requirements.
      return location.getX() + "," + location.getY();
    }
  }

}
