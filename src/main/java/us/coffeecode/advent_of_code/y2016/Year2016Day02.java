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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2016/day/2">Year 2016, day 2</a>. This problem is about finding a sequence of keys
 * to press on a keypad based on starting at "5" and moving your finger up, down, left or right before pressing another
 * key. However, some keys may be at the edge of the keypad, and a particular move might not be allowed so it should be
 * ignored. The two parts have keypads of different sizes and layouts, with part two having non-numeric keys.
 * </p>
 * <p>
 * The two parts use the exact same solution, except with different data. The input data is the same, but they use a
 * different keypad mapping. This works by feeding in an array of strings that consist of a starting key, direction, and
 * destination key. Then go through the program input and use the mapping to move around the keypad, saving each key
 * press after iterating each input line.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2016Day02 {

  public String calculatePart1() {
    return calculate(getMapping(PART1));
  }

  public String calculatePart2() {
    return calculate(getMapping(PART2));
  }

  private String calculate(final Map<Integer, Map<Integer, Integer>> mapping) {
    final StringBuilder code = new StringBuilder(5);
    Integer key = Integer.valueOf('5');
    for (final List<Integer> sequence : getInput()) {
      for (final Integer d : sequence) {
        key = mapping.get(key).get(d);
      }
      code.appendCodePoint(key.intValue());
    }
    return code.toString();
  }

  /** Get the input data for this solution. */
  private List<List<Integer>> getInput() {
    try {
      return Files.readAllLines(Utils.getInput(2016, 2)).stream().map(
        s -> s.chars().mapToObj(Integer::valueOf).collect(Collectors.toList())).collect(Collectors.toList());
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private static final String[] PART1 = new String[] { "1U1", "1D4", "1L1", "1R2", "2U2", "2D5", "2L1", "2R3", "3U3",
      "3D6", "3L2", "3R3", "4U1", "4D7", "4L4", "4R5", "5U2", "5D8", "5L4", "5R6", "6U3", "6D9", "6L5", "6R6", "7U4",
      "7D7", "7L7", "7R8", "8U5", "8D8", "8L7", "8R9", "9U6", "9D9", "9L8", "9R9" };

  private static final String[] PART2 = new String[] { "1U1", "1D3", "1L1", "1R1", "2U2", "2D6", "2L2", "2R3", "3U1",
      "3D7", "3L2", "3R4", "4U4", "4D8", "4L3", "4R4", "5U5", "5D5", "5L5", "5R6", "6U2", "6DA", "6L5", "6R7", "7U3",
      "7DB", "7L6", "7R8", "8U4", "8DC", "8L7", "8R9", "9U9", "9D9", "9L8", "9R9", "AU6", "ADA", "ALA", "ARB", "BU7",
      "BDD", "BLA", "BRC", "CU8", "CDC", "CLB", "CRC", "DUB", "DDD", "DLD", "DRD" };

  /**
   * Get a mapping of keys on a keypad to other keys based on direction. The first map key is the starting keypad key.
   * The inner map uses the direction code point as a key, with its value being the code point of the keypad key that
   * results from attempting to move in that direction.
   */
  private final Map<Integer, Map<Integer, Integer>> getMapping(final String[] raw) {
    final Map<Integer, Map<Integer, Integer>> result = new HashMap<>();
    for (final String line : raw) {
      final Integer key1 = Integer.valueOf(line.codePointAt(0));
      final Integer dir = Integer.valueOf(line.codePointAt(1));
      final Integer key2 = Integer.valueOf(line.codePointAt(2));
      if (!result.containsKey(key1)) {
        result.put(key1, new HashMap<>());
      }
      result.get(key1).put(dir, key2);
    }
    return result;
  }

}
