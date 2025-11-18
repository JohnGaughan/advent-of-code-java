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

import static us.coffeecode.advent_of_code.y2019.ExecutionOption.BLOCK_UNTIL_INPUT_AVAILABLE;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point2D;

@AdventOfCodeSolution(year = 2019, day = 15)
@Component
public final class Year2019Day15 {

  @Autowired
  private IntCodeFactory icf;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(pc);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculate(pc);
  }

  private long calculate(final PuzzleContext pc) {
    final boolean returnFewestMovementCommands = pc.getBoolean("ReturnFewestMovementCommands");
    final Map<Point2D, Tile> map = new HashMap<>();
    final Queue<QueueEntry> states = new LinkedList<>();
    {
      final IntCode state = icf.make(pc, BLOCK_UNTIL_INPUT_AVAILABLE);
      states.add(new QueueEntry(state, new Point2D(0, 0), 1));
    }

    long distance = Long.MAX_VALUE;

    while (!states.isEmpty()) {
      final QueueEntry qe = states.poll();
      // Get all unmapped neighbors and try them out.
      for (int dir = 1; dir < 5; ++dir) {
        final Point2D coord = forDirection(dir, qe.point);
        if (!map.containsKey(coord)) {
          final IntCode state = icf.make(qe.state);
          state.getInput()
               .add(dir);
          state.exec();
          final long output = state.getOutput()
                                   .remove();

          if (output == 0) {
            map.put(coord, Tile.WALL);
          }
          else {
            if (output == 1) {
              map.put(coord, Tile.OPEN);
            }
            else {
              map.put(coord, Tile.GOAL);
              distance = Math.min(distance, qe.distance);
            }
            states.add(new QueueEntry(state, coord, qe.distance + 1));
          }
        }
      }
    }
    if (returnFewestMovementCommands) {
      return distance;
    }

    // Replace the goal with oxygen. This is the base case at T=0.
    for (final Point2D c : map.keySet()) {
      if (map.get(c) == Tile.GOAL) {
        map.put(c, Tile.OXYGEN);
      }
    }

    // While the maze is not filled with oxygen, expand the reach of oxygen.
    long time = 0;
    Collection<Point2D> edges = new HashSet<>();
    edges.addAll(map.entrySet()
                    .stream()
                    .filter(e -> e.getValue() == Tile.OXYGEN)
                    .map(e -> e.getKey())
                    .toList());
    while (map.values()
              .stream()
              .anyMatch(t -> t == Tile.OPEN)) {
      ++time;
      final Collection<Point2D> newEdges = new HashSet<>(edges.size());
      for (final var iter = edges.iterator(); iter.hasNext();) {
        for (final Point2D p : iter.next()
                                   .getCardinalNeighbors()) {
          if (map.get(p) == Tile.OPEN) {
            map.put(p, Tile.OXYGEN);
            newEdges.add(p);
          }
        }
        iter.remove();
      }
      edges = newEdges;
    }
    return time;
  }

  private Point2D forDirection(final long dir, final Point2D point) {
    if (dir == 1) {
      // North
      return new Point2D(point, 0, -1);
    }
    else if (dir == 2) {
      // South
      return new Point2D(point, 0, 1);
    }
    else if (dir == 3) {
      // West
      return new Point2D(point, -1, 0);
    }
    else if (dir == 4) {
      // East
      return new Point2D(point, 1, 0);
    }
    throw new IllegalArgumentException(Long.toString(dir));
  }

  private static record QueueEntry(IntCode state, Point2D point, long distance) {}

  private static enum Tile {

    OPEN(" "),
    WALL("#"),
    GOAL("G"),
    OXYGEN("O");

    private final String str;

    private Tile(final String s) {
      str = s;
    }

    @Override
    public String toString() {
      return str;
    }
  }

}
