/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2024  John Gaughan
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
package us.coffeecode.advent_of_code.y2024;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2024, day = 25, title = "Code Chronicle")
@Component
public class Year2024Day25 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Input input = getInput(pc);
    return input.locks.stream()
                      .mapToLong(l -> input.keys.stream()
                                                .filter(k -> fits(l, k))
                                                .count())
                      .sum();
  }

  /** Get whether the key fits in the lock. */
  private boolean fits(final int[] lock, final int[] key) {
    for (int i = 0; i < lock.length; ++i) {
      if (lock[i] + key[i] > 5) {
        return false;
      }
    }
    return true;
  }

  /** Get the input as arrays of lock/key pin/teeth heights. */
  private Input getInput(final PuzzleContext pc) {
    final List<int[]> locks = new ArrayList<>(250);
    final List<int[]> keys = new ArrayList<>(250);
    for (final List<String> group : il.groups(pc)) {
      final String first = group.getFirst();
      final int rowLength = first.length();

      // Lock
      if (first.codePointAt(0) == '#') {
        final int[] pins = new int[rowLength];
        for (int y = 0; y < group.size(); ++y) {
          final String row = group.get(y);
          for (int x = 0; x < pins.length; ++x) {
            if (row.codePointAt(x) == '#') {
              pins[x] = y;
            }
          }
        }
        locks.add(pins);
      }

      // Key
      else {
        final int[] teeth = new int[rowLength];
        final int max = group.size() - 1;
        for (int y = max; y >= 0; --y) {
          final String row = group.get(y);
          for (int x = 0; x < teeth.length; ++x) {
            if (row.codePointAt(x) == '#') {
              teeth[x] = max - y;
            }
          }
        }
        keys.add(teeth);
      }
    }
    return new Input(locks, keys);
  }

  private record Input(List<int[]> locks, List<int[]> keys) {}
}
