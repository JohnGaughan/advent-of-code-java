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
package net.johngaughan.advent_of_code.y2020;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import net.johngaughan.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2020/day/24">Year 2020, day 24</a>. This penultimate puzzle is a variation of
 * Conway's Game of Life using a hexagonal grid.
 * </p>
 * <p>
 * The only slightly tricky problem here is representing a hexagonal grid using a coordinate system. I opted for
 * treating the X axis as having a space of two between grid elements, since the diagonal movements move halfway between
 * them. Instead of using floats or decimals, this is a cleaner and less error-prone approach.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public class Year2020Day24 {

  private static final int PART2_ROUNDS = 100;

  public long calculatePart1() {
    return countBlackTiles(init());
  }

  public long calculatePart2() {
    Map<Coordinate, Boolean> tiles = init();
    for (int i = 0; i < PART2_ROUNDS; ++i) {
      tiles = playOneRound(tiles);
    }
    return countBlackTiles(tiles);
  }

  /** Play one round of the flipping game. */
  private Map<Coordinate, Boolean> playOneRound(final Map<Coordinate, Boolean> tiles) {
    final Map<Coordinate, Boolean> result = new HashMap<>();
    for (Map.Entry<Coordinate, Boolean> entry : tiles.entrySet()) {
      final Coordinate c = entry.getKey();
      // This tile is black
      if (Boolean.TRUE.equals(entry.getValue())) {
        final int blackAdjacent = countBlackTilesTouching(c, tiles);
        if (blackAdjacent == 0 || blackAdjacent > 2) {
          result.put(c, Boolean.FALSE);
        }
        else {
          result.put(c, Boolean.TRUE);
        }
        // Look for tiles surrounding this one that don't exist. They are white, but not recorded yet.
        for (Direction d : Direction.values()) {
          final Coordinate c2 = new Coordinate(c, d);
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
  private int countBlackTilesTouching(final Coordinate c, final Map<Coordinate, Boolean> blackTiles) {
    int tiles = 0;
    for (Direction d : Direction.values()) {
      Coordinate adjacent = new Coordinate(c, d);
      if (Boolean.TRUE.equals(blackTiles.get(adjacent))) {
        ++tiles;
      }
    }
    return tiles;
  }

  /** Count the number of black tiles in the provided coordinate space. */
  private long countBlackTiles(final Map<Coordinate, Boolean> blackTiles) {
    long countOfBlackTiles = 0;
    for (Boolean value : blackTiles.values()) {
      if (value.booleanValue()) {
        ++countOfBlackTiles;
      }
    }
    return countOfBlackTiles;
  }

  /** Initialize the coordinate space by flipping all of the grid elements in the input data. */
  private Map<Coordinate, Boolean> init() {
    final Map<Coordinate, Boolean> blackTiles = new HashMap<>();
    for (List<Direction> steps : getInput()) {
      final Coordinate c = traverse(steps);
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
  private Coordinate traverse(final List<Direction> steps) {
    final Coordinate c = new Coordinate();
    for (Direction step : steps) {
      c.add(step);
    }
    return c;
  }

  /** Get the input data for this solution. */
  private List<List<Direction>> getInput() {
    try {
      return Files.readAllLines(Utils.getInput(2020, 24)).stream().map(Direction::parse).collect(Collectors.toList());
    }
    catch (final RuntimeException ex) {
      throw ex;
    }
    catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private static final class Coordinate {

    int x;

    int y;

    Coordinate() {
      x = 0;
      y = 0;
    }

    Coordinate(final Coordinate c, final Direction d) {
      x = c.x + d.dx;
      y = c.y + d.dy;
    }

    void add(final Direction d) {
      x += d.dx;
      y += d.dy;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
      if (this == obj) {
        return true;
      }
      else if (!(obj instanceof Coordinate)) {
        return false;
      }
      Coordinate o = (Coordinate) obj;
      return x == o.x && y == o.y;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
      return Objects.hash(Integer.valueOf(y), Integer.valueOf(x));
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
      return "(" + x + "," + y + ")";
    }
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
