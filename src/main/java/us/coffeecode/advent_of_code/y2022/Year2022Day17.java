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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2022, day = 17)
@Component
public class Year2022Day17 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(pc, 2_022);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculate(pc, 1_000_000_000_000L);
  }

  private static final int LOOKBACK = 10;

  public long calculate(final PuzzleContext pc, final long rounds) {
    final int[] input = il.fileAsString(pc)
                          .codePoints()
                          .toArray();
    int wind = 0;
    byte[] tower = new byte[3 << 20];
    int height = 0;
    final Map<CacheKey, CacheValue> cycleCache = new HashMap<>(1 << 21);
    long addHeight = 0;
    for (long i = 0; i < rounds; ++i) {
      final int rock = (int) (i % SHAPES.length);
      if (height > LOOKBACK) {
        final byte[] topRows = new byte[LOOKBACK >> 1];
        System.arraycopy(tower, height - topRows.length, topRows, 0, topRows.length);
        final CacheKey key = new CacheKey(rock, wind, topRows);
        if (cycleCache.containsKey(key)) {
          // Found a cycle: figure out how many rocks to skip to put us close to the number of rounds
          final CacheValue value = cycleCache.get(key);
          final long cycleLength = i - value.cycle;
          final long heightDifference = height - value.height;
          final long addCycles = (rounds - i) / cycleLength;
          addHeight = addCycles * heightDifference;
          i += addCycles * cycleLength;
        }
        else {
          cycleCache.put(key, new CacheValue(i, height));
        }
      }
      final byte[] shape = Arrays.copyOf(SHAPES[rock], SHAPES[rock].length);
      int y = height + 3;
      tower = ensureCapacity(tower, y + shape.length);
      while (true) {
        final int direction = input[wind];
        wind = (wind + 1) % input.length;

        if (canMove(tower, shape, y, direction)) {
          if (direction == '<') {
            for (int j = 0; j < shape.length; ++j) {
              shape[j] <<= 1;
            }
          }
          else {
            for (int j = 0; j < shape.length; ++j) {
              shape[j] >>= 1;
            }
          }
        }

        if (canDrop(tower, shape, y)) {
          --y;
        }
        else {
          // Imprint this shape at its current location.
          for (int y0 = 0; y0 < shape.length; ++y0) {
            tower[y + y0] |= shape[y0];
          }
          break;
        }
      }
      height = Math.max(height, y + shape.length);
    }
    return height + addHeight;
  }

  private record CacheKey(int rock, int wind, byte[] top) {

    @Override
    public boolean equals(final Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj instanceof CacheKey o) {
        return rock == o.rock && wind == o.wind && Arrays.equals(top, o.top);
      }
      return false;
    }
  }

  private record CacheValue(long cycle, long height) {}

  private boolean canMove(final byte[] tower, final byte[] shape, final int y, final int direction) {
    for (int i = 0; i < shape.length; ++i) {
      final int shifted;
      if (direction == '<') {
        shifted = (shape[i] << 1);
        // At the left edge
        if (shifted > MAX) {
          return false;
        }
      }
      else {
        shifted = (shape[i] >> 1);
        // At the right edge
        if (shape[i] != (shifted << 1)) {
          return false;
        }
      }
      if ((tower[y + i] & shifted) > 0) {
        return false;
      }
    }
    return true;
  }

  private boolean canDrop(final byte[] tower, final byte[] shape, final int y) {
    if (y == 0) {
      return false;
    }
    else if (tower[y - 1] == 0) {
      return true;
    }
    for (int i = 0; i < shape.length; ++i) {
      if ((shape[i] & tower[y - 1 + i]) > 0) {
        return false;
      }
    }
    return true;
  }

  private byte[] ensureCapacity(final byte[] tower, final int capacity) {
    if (capacity < tower.length) {
      return tower;
    }
    final byte[] newTower = new byte[capacity + 1];
    System.arraycopy(tower, 0, newTower, 0, tower.length);
    return newTower;
  }

  private static final int MAX = (1 << 7) - 1;

  private static final byte[][] SHAPES = new byte[][] {
    // @formatter:off
    { 0b0011110 },

    { 0b0001000,
      0b0011100,
      0b0001000 },

    { 0b0011100,
      0b0000100,
      0b0000100 },

    { 0b0010000,
      0b0010000,
      0b0010000,
      0b0010000 },

    { 0b0011000,
      0b0011000 }
    // @formatter:on
  };
}
