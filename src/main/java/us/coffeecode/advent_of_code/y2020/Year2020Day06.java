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
package us.coffeecode.advent_of_code.y2020;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2020, day = 6, title = "Custom Customs")
@Component
public final class Year2020Day06 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return getInput(pc).stream()
                       .map(l -> union(l))
                       .mapToInt(s -> s.size())
                       .sum();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return getInput(pc).stream()
                       .map(l -> intersection(l))
                       .mapToInt(s -> s.size())
                       .sum();
  }

  /** Reduces a single group of passenger answers by calculating the union. */
  private Set<Integer> union(final List<Set<Integer>> group) {
    final Set<Integer> result = new HashSet<>();
    group.forEach(s -> result.addAll(s));
    return result;
  }

  /** Reduces a single group of passenger answers by calculating the intersection. */
  private Set<Integer> intersection(final List<Set<Integer>> group) {
    final Set<Integer> result = "abcdefghijklmnopqrstuvwxyz".codePoints()
                                                            .boxed()
                                                            .collect(Collectors.toSet());
    group.forEach(s -> result.retainAll(s));
    return result;
  }

  /** Get the input data for this solution. */
  private List<List<Set<Integer>>> getInput(final PuzzleContext pc) {
    return il.groups(pc)
             .stream()
             .map(lines -> lines.stream()
                                .map(line -> line.codePoints()
                                                 .boxed()
                                                 .collect(Collectors.toSet()))
                                .toList())
             .toList();
  }

}
