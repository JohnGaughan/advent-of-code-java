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
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.LongStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Range;

@AdventOfCodeSolution(year = 2024, day = 9)
@Component
public class Year2024Day09 {

  @Autowired
  private InputLoader il;

  @SuppressWarnings("boxing")
  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Input1 input = getInputPartOne(pc);
    // While there is still a gap...
    while ((input.filesystem.lastKey()
                            .intValue() >= input.filesystem.size())) {
      // ...get and remove the first gap.
      final var iter = input.gaps.iterator();
      final Integer nextKey = iter.next();
      iter.remove();
      // Move the last file block to the location of the gap.
      final Integer nextValue = input.filesystem.lastEntry()
                                                .getValue();
      input.filesystem.remove(input.filesystem.lastKey());
      input.filesystem.put(nextKey, nextValue);
    }
    // Multiply all pairs of file IDs and block numbers, then total them to get the checksum.
    return input.filesystem.entrySet()
                           .stream()
                           .mapToLong(e -> e.getKey() * e.getValue())
                           .sum();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final Input2 input = getInputPartTwo(pc);
    // As files are moved (or not), add them to the defragmented files. No need to remove files as they are processed in
    // reverse order and we only care about gaps, which are tracked separately.
    final Collection<File> defragmented = new ArrayList<>(input.files.size());
    for (final File file : input.files) {
      boolean moved = false;
      for (var iter = input.gaps.iterator(); iter.hasNext();) {
        final Range gap = iter.next();
        // Ran out of gaps to the left, none were big enough.
        if (gap.compareTo(file.location) > 0) {
          break;
        }
        // Gap is further to the left than the file and big enough: move the file.
        else if (gap.sizeInclusive() >= file.location.sizeInclusive()) {
          // Move the file to the start of the gap.
          final Range newLocation = new Range(gap.getX1(), gap.getX1() + file.location.sizeInclusive() - 1);
          defragmented.add(new File(newLocation, file.id));
          // Remove the gap. If necessary, add a new one with the leftover space.
          iter.remove();
          if (!newLocation.equals(gap)) {
            final int x1 = newLocation.getX2() + 1;
            final int x2 = gap.getX2();
            input.gaps.add(new Range(x1, x2));
          }
          moved = true;
          break;
        }
      }
      // File was not moved, so add it as-is to the defragmented files.
      if (!moved) {
        defragmented.add(file);
      }
    }
    return defragmented.stream()
                       .mapToLong(f -> f.id * LongStream.rangeClosed(f.location.getX1(), f.location.getX2())
                                                        .sum())
                       .sum();
  }

  /** Get the input for part one, which tracks individual blocks in the file system. */
  private Input1 getInputPartOne(final PuzzleContext pc) {
    final NavigableMap<Integer, Integer> filesystem = new TreeMap<>();
    final List<Integer> gaps = new LinkedList<>();
    boolean inFile = true;
    int location = 0;
    int id = 0;
    for (final int digit : il.fileAsIntsFromDigits(pc)) {
      if (inFile) {
        final Integer value = Integer.valueOf(id);
        for (int i = 0; i < digit; ++i) {
          filesystem.put(Integer.valueOf(location), value);
          ++location;
        }
        ++id;
      }
      else {
        for (int i = 0; i < digit; ++i) {
          gaps.add(Integer.valueOf(location));
          ++location;
        }
      }
      inFile = !inFile;
    }
    return new Input1(filesystem, gaps);
  }

  /** Get the input for part two, which tracks block ranges of entire files and the gaps between them. */
  private Input2 getInputPartTwo(final PuzzleContext pc) {
    final List<File> files = new LinkedList<>();
    final NavigableSet<Range> gaps = new TreeSet<>();
    boolean inFile = true;
    int id = 0;
    int location = 0;
    for (final int digit : il.fileAsIntsFromDigits(pc)) {
      if (inFile && (digit > 0)) {
        files.add(new File(new Range(location, location + digit - 1), id));
        ++id;
      }
      else if (!inFile && (digit > 0)) {
        gaps.add(new Range(location, location + digit - 1));
      }
      location += digit;
      inFile = !inFile;
    }
    // Sort the files in reverse order based on their location in the file system, so iteration is correct per the
    // requirements.
    Collections.sort(files, (a, b) -> b.location.compareTo(a.location));
    return new Input2(files, gaps);
  }

  private record Input1(NavigableMap<Integer, Integer> filesystem, List<Integer> gaps) {}

  private record Input2(List<File> files, NavigableSet<Range> gaps) {}

  private record File(Range location, int id) {}
}
