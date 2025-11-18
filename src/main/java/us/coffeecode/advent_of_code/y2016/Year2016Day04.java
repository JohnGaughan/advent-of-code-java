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

import java.util.NavigableMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2016, day = 4)
@Component
public final class Year2016Day04 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    long sectorIdSum = 0;
    for (final Room room : getInput(pc)) {
      sectorIdSum += room.sectorId;
    }
    return sectorIdSum;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final String needle = "northpole object storage";
    for (final Room room : getInput(pc)) {
      if (needle.equals(room.decrypt())) {
        return room.sectorId;
      }
    }
    return -1;
  }

  /** Get the input data for this solution. */
  private Iterable<Room> getInput(final PuzzleContext pc) {
    return il.linesAsObjects(pc, Room::make)
             .stream()
             .filter(Room::real)
             .toList();
  }

  private static record Room(String name, int sectorId, boolean real) {

    static Room make(final String input) {
      final int[] codePointCounts = new int[128];
      int sectorIdStart = -1;

      // Find the start of the sector ID, and count up the letters along the way.
      for (int i = 0; i < input.length(); ++i) {
        final int ch = input.codePointAt(i);
        if (Character.isDigit(ch)) {
          sectorIdStart = i;
          break;
        }
        if (Character.isAlphabetic(ch)) {
          ++codePointCounts[ch];
        }
      }

      // Find the end of the sector ID.
      int sectorIdEnd = -1;
      for (int i = sectorIdStart + 1; i < input.length(); ++i) {
        int ch = input.codePointAt(i);
        if (!Character.isDigit(ch)) {
          sectorIdEnd = i;
          break;
        }
      }

      // Omit the brackets
      final String expectedChecksum = input.substring(sectorIdEnd + 1, input.length() - 1);

      // Given the count of each letter, reorganize into a map from the count of letters to a set of the letters with
      // that count.
      final NavigableMap<Integer, SortedSet<Integer>> countBuckets = new TreeMap<>((o1, o2) -> o2.compareTo(o1));
      for (int i = 0; i < codePointCounts.length; ++i) {
        if (codePointCounts[i] > 0) {
          final Integer key = Integer.valueOf(codePointCounts[i]);
          if (!countBuckets.containsKey(key)) {
            countBuckets.put(key, new TreeSet<>());
          }
          countBuckets.get(key)
                      .add(Integer.valueOf(i));
        }
      }

      // Calculate the checksum for the input string.
      final StringBuilder str = new StringBuilder(5);
      while (str.length() < 5 && !countBuckets.isEmpty()) {
        Integer key = countBuckets.firstKey();
        SortedSet<Integer> value = countBuckets.get(key);
        Integer next = value.first();
        str.appendCodePoint(next.intValue());
        value.remove(next);
        if (value.isEmpty()) {
          countBuckets.remove(key);
        }
      }
      final String actualChecksum = str.toString();

      return new Room(input.substring(0, sectorIdStart - 1), Integer.parseInt(input.substring(sectorIdStart, sectorIdEnd)),
        actualChecksum.equals(expectedChecksum));
    }

    private static final int ALPHABET_LENGTH = 'z' - 'a' + 1;

    String decrypt() {
      // No need to rotate multiple times. Sector IDs are all in the hundreds.
      final int rotation = sectorId % 26;
      final StringBuilder decrypted = new StringBuilder(name.length());
      for (int i = 0; i < name.length(); ++i) {
        int ch = name.codePointAt(i);
        if ('-' == ch) {
          decrypted.append(' ');
        }
        else {
          ch += rotation;
          if (ch > 'z') {
            ch = ch - ALPHABET_LENGTH;
          }
          decrypted.appendCodePoint(ch);
        }
      }
      return decrypted.toString();
    }

  }

}
