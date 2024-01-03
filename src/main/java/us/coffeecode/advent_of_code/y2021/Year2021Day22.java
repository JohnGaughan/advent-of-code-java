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

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Range3D;

@AdventOfCodeSolution(year = 2021, day = 22, title = "Reactor Reboot")
@Component
public final class Year2021Day22 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Range3D area = new Range3D(-50, -50, -50, 50, 50, 50);
    return calculate(il.linesAsObjects(pc, this::parse).stream().filter(c -> area.overlaps(c.range)).toList());
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculate(il.linesAsObjects(pc, this::parse));
  }

  private long calculate(final Iterable<Volume> input) {
    final Collection<Volume> volumes = new ArrayList<>(28_000);
    for (final Volume nextVolume : input) {
      final Collection<Volume> newVolumes = new ArrayList<>(8_192);
      for (final Volume old : volumes) {
        if (nextVolume.range.overlaps(old.range)) {
          newVolumes.add(new Volume(nextVolume.range.intersection(old.range), !old.on));
        }
      }
      if (nextVolume.on) {
        newVolumes.add(nextVolume);
      }
      volumes.addAll(newVolumes);
    }
    return volumes.stream().mapToLong(Volume::size).sum();
  }

  private Volume parse(final String line) {
    final int[] ints = InputLoader.DIGITS.matcher(line).results().map(r -> r.group()).mapToInt(Integer::parseInt).toArray();
    return new Volume(new Range3D(ints[0], ints[2], ints[4], ints[1], ints[3], ints[5]), line.codePointAt(1) == 'n');
  }

  private record Volume(Range3D range, boolean on) {

    long size() {
      return on ? range.sizeInclusive() : -range.sizeInclusive();
    }

  }

}
