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
package us.coffeecode.advent_of_code.y2018;

import java.nio.file.Files;
import java.util.List;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2018/day/2">Year 2018, day 2</a>. This problem asks us to evaluate some strings.
 * For part one, count how many strings have a character repeated exactly twice anywhere in the string, and how many
 * have a character repeated exactly three times. Then multiply these counts together. For part two, we need to find the
 * pair of strings that differ by a single character, and return the string that is those strings minus the different
 * character.
 * </p>
 * <p>
 * Part one is some simple iteration and counting. Part two is a variation on the
 * <a href="https://en.wikipedia.org/wiki/Hamming_distance">Hamming distance</a> algorithm. We need two strings with a
 * Hamming distance of one. The method to check two strings tracks the index of the character that is different. If
 * there is a second different character, return null to indicate this is not a solution. Otherwise, once done comparing
 * the strings, get two substrings on either side of that index, concatenate, and return them.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2018Day02 {

  public long calculatePart1() {
    int twos = 0;
    int threes = 0;
    for (final String line : getInput()) {
      final int[] counts = new int[26];
      for (int ch : line.chars().toArray()) {
        ++counts[ch - 'a'];
      }
      boolean countedTwo = false;
      boolean countedThree = false;
      for (int count : counts) {
        if (!countedTwo && count == 2) {
          ++twos;
          countedTwo = true;
        }
        if (!countedThree && count == 3) {
          ++threes;
          countedThree = true;
        }
      }
    }
    return twos * threes;
  }

  public String calculatePart2() {
    final List<String> strings = getInput();
    for (int i = 0; i < strings.size() - 1; ++i) {
      for (int j = i + 1; j < strings.size(); ++j) {
        final String solution = ham(strings.get(i), strings.get(j));
        if (solution != null) {
          return solution;
        }
      }
    }
    return "NO SOLUTION";
  }

  private String ham(final String str1, final String str2) {
    int difference = -1;
    for (int i = 0; i < str1.length(); ++i) {
      if (str1.codePointAt(i) != str2.codePointAt(i)) {
        if (difference >= 0) {
          // Already found a difference, this cannot be a solution.
          return null;
        }
        difference = i;
      }
    }
    return str1.substring(0, difference) + str1.substring(difference + 1);
  }

  /** Get the input data for this solution. */
  private List<String> getInput() {
    try {
      return Files.readAllLines(Utils.getInput(2018, 2));
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

}
