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
package us.coffeecode.advent_of_code.y2020;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point2D;

@AdventOfCodeSolution(year = 2020, day = 24)
@Component
public class Year2020Day24 {

  private static final int PART2_ROUNDS = 100;

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return countBlackTiles(init(pc));
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    Map<Point2D, Boolean> tiles = init(pc);
    for (int i = 0; i < PART2_ROUNDS; ++i) {
      tiles = playOneRound(tiles);
    }
    return countBlackTiles(tiles);
  }

  /** Play one round of the flipping game. */
  private Map<Point2D, Boolean> playOneRound(final Map<Point2D, Boolean> tiles) {
    final Map<Point2D, Boolean> result = new HashMap<>();
    for (final Map.Entry<Point2D, Boolean> entry : tiles.entrySet()) {
      final Point2D c = entry.getKey();
      // This tile is black
      if (Boolean.TRUE.equals(entry.getValue())) {
        final int blackAdjacent = countBlackTilesTouching(c, tiles);
        if ((blackAdjacent == 0) || (blackAdjacent > 2)) {
          result.put(c, Boolean.FALSE);
        }
        else {
          result.put(c, Boolean.TRUE);
        }
        // Look for tiles surrounding this one that don't exist. They are white, but not recorded yet.
        for (final Direction d : Direction.values()) {
          final Point2D c2 = new Point2D(c, d.dx, d.dy);
          if (!tiles.containsKey(c2) && !result.containsKey(c2)) {
            final int blackAdjacent2 = countBlackTilesTouching(c2, tiles);
            if (blackAdjacent2 == 2) {
              result.put(c2, Boolean.TRUE);
            }
          }
        }
      }
      // This tile is white and has not already been processed.
      else if (!result.containsKey(c)) {
        final int blackAdjacent = countBlackTilesTouching(c, tiles);
        if (blackAdjacent == 2) {
          result.put(c, Boolean.TRUE);
        }
      }
    }
    return result;
  }

  /** Count the number of black tiles touching the given coordinates. */
  private int countBlackTilesTouching(final Point2D c, final Map<Point2D, Boolean> blackTiles) {
    int tiles = 0;
    for (final Direction d : Direction.values()) {
      final Point2D adjacent = new Point2D(c, d.dx, d.dy);
      if (Boolean.TRUE.equals(blackTiles.get(adjacent))) {
        ++tiles;
      }
    }
    return tiles;
  }

  /** Count the number of black tiles in the provided coordinate space. */
  private long countBlackTiles(final Map<Point2D, Boolean> blackTiles) {
    return blackTiles.values()
                     .stream()
                     .filter(b -> b.booleanValue())
                     .count();
  }

  /** Initialize the coordinate space by flipping all of the grid elements in the input data. */
  private Map<Point2D, Boolean> init(final PuzzleContext pc) {
    final Map<Point2D, Boolean> blackTiles = new HashMap<>();
    for (final Iterable<Direction> steps : il.linesAsObjects(pc, Direction::parse)) {
      final Point2D c = traverse(steps);
      if (blackTiles.containsKey(c)) {
        final Boolean currentValue = blackTiles.get(c);
        final Boolean newValue = Boolean.valueOf(!currentValue.booleanValue());
        blackTiles.put(c, newValue);
      }
      else {
        blackTiles.put(c, Boolean.TRUE);
      }
    }
    return blackTiles;
  }

  /** Given a set of directions, traverse the grid and get the final coordinate. */
  private Point2D traverse(final Iterable<Direction> steps) {
    int dx = 0;
    int dy = 0;
    for (final Direction step : steps) {
      dx += step.dx;
      dy += step.dy;
    }
    return new Point2D(dx, dy);
  }

  private static enum Direction {

    NW(-1, 1),
    NE(1, 1),
    E(2, 0),
    SE(1, -1),
    SW(-1, -1),
    W(-2, 0);

    static List<Direction> parse(final String str) {
      final List<Direction> directions = new ArrayList<>(str.length());
      int previous = -1;
      for (int i = 0; i < str.length(); ++i) {
        int current = str.codePointAt(i);
        if (previous == 'n') {
          if (current == 'e') {
            directions.add(NE);
          }
          else if (current == 'w') {
            directions.add(NW);
          }
          else {
            throw new IllegalArgumentException(str);
          }
          previous = -1;
        }
        else if (previous == 's') {
          if (current == 'e') {
            directions.add(SE);
          }
          else if (current == 'w') {
            directions.add(SW);
          }
          else {
            throw new IllegalArgumentException(str);
          }
          previous = -1;
        }
        else if (previous < 0) {
          if (current == 'e') {
            directions.add(E);
          }
          else if (current == 'w') {
            directions.add(W);
          }
          else {
            previous = current;
          }
        }
      }
      return directions;
    }

    final int dx;

    final int dy;

    private Direction(final int _x, final int _y) {
      dx = _x;
      dy = _y;
    }
  }

}
