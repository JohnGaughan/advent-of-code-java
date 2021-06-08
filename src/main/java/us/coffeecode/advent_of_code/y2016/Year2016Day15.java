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

import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2016/day/15">Year 2016, day 15</a>. This is a Chinese remainder theorem problem
 * similar to year 2020, day 13. Find a number that lines up with coprime repeating cycles.
 * </p>
 * <p>
 * The key here is that the number of positions on each disc is coprime to all the other discs. The algorithm would be
 * similar without that, but this way is simpler. For each disc, figure out the remainder (time offset). Then add a time
 * increment repeatedly until the remainder matches. The interval starts out at one. After processing each disc,
 * multiply the increment by the positions for the disc we just finished. This guarantees that when we calculate future
 * discs, we don't misalign previous ones.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2016Day15 {

  public long calculatePart1() {
    return calculate(getInput(false));
  }

  public long calculatePart2() {
    return calculate(getInput(true));
  }

  private long calculate(final List<Disc> discs) {
    int dropTime = 0;
    int increment = 1;
    for (Disc disc : discs) {
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
  private List<Disc> getInput(final boolean bonusDisc) {
    try {
      // new Disc("Disc #7 has 11 positions; at time=0, it is at position 0.");
      final List<Disc> discs =
        Files.readAllLines(Utils.getInput(2016, 15)).stream().map(Disc::new).collect(Collectors.toList());
      if (bonusDisc) {
        discs.add(new Disc("Disc #7 has 11 positions; at time=0, it is at position 0."));
      }
      Collections.sort(discs);
      return discs;
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private static final class Disc
  implements Comparable<Disc> {

    private static final Pattern NUMBERS = Pattern.compile("\\d+");

    final int id;

    final int positions;

    final int start;

    Disc(final String input) {
      final Matcher match = NUMBERS.matcher(input);
      match.find();
      id = Integer.parseInt(match.group());
      match.find();
      positions = Integer.parseInt(match.group());
      match.find();
      match.find();
      start = Integer.parseInt(match.group());
    }

    @Override
    public int compareTo(final Disc o) {
      return Integer.compare(id, o.id);
    }

  }

}
