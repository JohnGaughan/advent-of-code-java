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
import java.util.List;
import java.util.NavigableMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2016/day/4">Year 2016, day 4</a>. This has two different parts. The first is
 * figuring out which inputs are valid. This requires counting letter frequency and comparing two checksums to see if
 * they are equal. For part one, sum up the sector IDs of valid inputs. For part two, "decrypt" the input with a Caesar
 * cipher where the rotation is the sector ID.
 * </p>
 * <p>
 * This is mostly menial low-level input formatting and handling and there is not much interesting to say about it.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2016Day04 {

  public long calculatePart1() {
    long sectorIdSum = 0;
    for (Room room : getInput()) {
      sectorIdSum += room.sectorId;
    }
    return sectorIdSum;
  }

  public long calculatePart2() {
    final String needle = "northpole object storage";
    for (Room room : getInput()) {
      if (needle.equals(room.decrypt())) {
        return room.sectorId;
      }
    }
    return -1;
  }

  /** Get the input data for this solution. */
  private List<Room> getInput() {
    try {
      return Files.readAllLines(Utils.getInput(2016, 4)).stream().map(Room::new).filter(Room::isReal).collect(
        Collectors.toList());
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private static final class Room {

    public final String name;

    public final int sectorId;

    private final boolean real;

    Room(final String input) {
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

      // Set variables we know so far.
      name = input.substring(0, sectorIdStart - 1);
      sectorId = Integer.parseInt(input.substring(sectorIdStart, sectorIdEnd));
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
          countBuckets.get(key).add(Integer.valueOf(i));
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

      real = actualChecksum.equals(expectedChecksum);
    }

    private static final int ALPHABET_LENGTH = 'z' - 'a' + 1;

    String decrypt() {
      // No need to rotate multiple times. Sector IDs are all in the hundreds.
      final int rotation = sectorId % 26;
      StringBuilder decrypted = new StringBuilder(name.length());
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

    boolean isReal() {
      return real;
    }
  }

}
