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

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2016, day = 15, title = "Timing is Everything")
@Component
public final class Year2016Day15 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(getInput(pc));
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculate(getInput(pc));
  }

  private long calculate(final Iterable<Disc> discs) {
    int dropTime = 0;
    int increment = 1;
    for (final Disc disc : discs) {
      int t = disc.positions - disc.id - disc.start;
      while (t < 0) {
        t += disc.positions;
      }
      while (dropTime % disc.positions != t) {
        dropTime += increment;
      }
      increment *= disc.positions;
    }
    return dropTime;
  }

  /** Get the input data for this solution. */
  private List<Disc> getInput(final PuzzleContext pc) {
    final List<Disc> discs = il.linesAsObjectsMutable(pc, Disc::make);
    if (pc.getBoolean("BonusDiscEnabled")) {
      discs.add(Disc.make(pc.getString("BonusDisc")));
    }
    Collections.sort(discs);
    return discs;
  }

  private static record Disc(int id, int positions, int start)
  implements Comparable<Disc> {

    private static final Pattern NUMBERS = Pattern.compile("\\d+");

    static Disc make(final String input) {
      final int[] i = NUMBERS.matcher(input).results().map(r -> r.group()).mapToInt(Integer::parseInt).toArray();
      return new Disc(i[0], i[1], i[3]);
    }

    @Override
    public int compareTo(final Disc o) {
      return Integer.compare(id, o.id);
    }

  }

}
