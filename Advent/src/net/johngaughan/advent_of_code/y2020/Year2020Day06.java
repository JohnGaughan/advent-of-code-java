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
package net.johngaughan.advent_of_code.y2020;

import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import net.johngaughan.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2020/day/6">Year 2020, day 6</a>. This problem states that each line may contain a
 * bunch of characters. Lines are in groups in the input, separated by blank lines. Part one wants the union of the
 * characters in each group: essentially a set of all unique characters on its lines. Part two asks for the
 * intersection, that is, the set of characters common between the lines.
 * </p>
 * <p>
 * My solution is to convert each line to a set of integers (code points), then perform set operations on them.
 * Specifically, union and intersection. Once these are reduced, sum all of the groups of input (sets).
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public final class Year2020Day06 {

  public int calculatePart1() {
    return getInput().stream().map(l -> union(l)).mapToInt(s -> s.size()).sum();
  }

  public int calculatePart2() {
    return getInput().stream().map(l -> intersection(l)).mapToInt(s -> s.size()).sum();
  }

  /** Reduces a single group of passenger answers by calculating the union. */
  private Set<Integer> union(final List<Set<Integer>> group) {
    final Set<Integer> result = new HashSet<>();
    group.forEach(s -> result.addAll(s));
    return result;
  }

  /** Reduces a single group of passenger answers by calculating the intersection. */
  private Set<Integer> intersection(final List<Set<Integer>> group) {
    final Set<Integer> result = "abcdefghijklmnopqrstuvwxyz".chars().boxed().collect(Collectors.toSet());
    group.forEach(s -> result.retainAll(s));
    return result;
  }

  /** Get the input data for this solution. */
  private List<List<Set<Integer>>> getInput() {
    try {
      return Utils.getLineGroups(Files.readAllLines(Utils.getInput(2020, 6))).stream().map(
        lines -> lines.stream().map(line -> line.codePoints().boxed().collect(Collectors.toSet())).collect(
          Collectors.toList())).collect(Collectors.toList());
    }
    catch (final RuntimeException ex) {
      throw ex;
    }
    catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
  }

}
