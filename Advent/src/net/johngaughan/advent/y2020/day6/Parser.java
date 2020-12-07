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
package net.johngaughan.advent.y2020.day6;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.johngaughan.advent.Utils;

/**
 * <p>
 * File parser for day six.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public class Parser {

  public List<List<Set<Character>>> parse(final Path t) {
    try {
      return parse(Files.readAllLines(t));
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  /** Parse all of the passenger responses. */
  private List<List<Set<Character>>> parse(final List<String> lines) {
    final List<List<Set<Character>>> responses = new ArrayList<>();
    // Iterate over each group of lines in the input, where each group is separated by a blank line.
    for (List<String> lineGroup : Utils.getLineGroups(lines)) {
      final List<Set<Character>> lineGroupLetters = convertLinesToSets(lineGroup);
      // Given sets for each line in the group, allow a function to reduce them to a single set before returning.
      responses.add(lineGroupLetters);
    }
    return responses;
  }

  /**
   * Given a group of lines as strings, convert them to a second group of sets where each set contains the characters on
   * that line.
   */
  private List<Set<Character>> convertLinesToSets(final List<String> lines) {
    final List<Set<Character>> responses = new ArrayList<>(lines.size());
    for (String line : lines) {
      final Set<Character> response = new HashSet<>();
      for (char ch : line.toCharArray()) {
        if (('a' <= ch) && (ch <= 'z')) {
          response.add(ch);
        }
      }
      responses.add(response);
    }
    return responses;
  }

}
