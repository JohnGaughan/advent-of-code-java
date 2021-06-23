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
package us.coffeecode.advent_of_code.y2017;

import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2017/day/4">Year 2017, day 4</a>. This problem asks us to perform string
 * comparisons among each line of words. Part one asks us to count how many lines have all unique words. Part two asks
 * us to count lines where no word is an anagram of another word.
 * </p>
 * <p>
 * Part one is trivial with Java collections. Put each list of strings (words) into a set, then see if its size matches
 * the list used to construct it. Part two requires us to dig into the letters in the words. The simplest way I found to
 * solve this was to sort the characters in each string, since two anagrams will produce the same sorted string.
 * However, Java does not make this easy with its stream API. There is no built-in character stream, and no trivial way
 * to convert an IntStream into a string, interpreting each integer as a code point. I went with a tiny bit more verbose
 * solution.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2017Day04 {

  public long calculatePart1() {
    long valid = 0;
    for (final List<String> line : getInput()) {
      final Set<String> uniques = new HashSet<>(line);
      if (line.size() == uniques.size()) {
        ++valid;
      }
    }
    return valid;
  }

  public long calculatePart2() {
    long valid = 0;
    for (final List<String> line : getInput()) {
      final Set<String> uniques = new HashSet<>();
      for (final String word : line) {
        final int[] codePoints = word.codePoints().sorted().toArray();
        uniques.add(new String(codePoints, 0, codePoints.length));
      }
      if (line.size() == uniques.size()) {
        ++valid;
      }
    }
    return valid;
  }

  /** Get the input data for this solution. */
  private List<List<String>> getInput() {
    try {
      return Files.readAllLines(Utils.getInput(2017, 4)).stream().map(s -> Arrays.asList(SEPARATOR.split(s))).collect(
        Collectors.toList());
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private static final Pattern SEPARATOR = Pattern.compile(" ");
}
