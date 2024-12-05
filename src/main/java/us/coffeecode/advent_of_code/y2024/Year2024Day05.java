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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2024, day = 5, title = "Print Queue")
@Component
public class Year2024Day05 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Input input = getInput(pc);
    return input.manuals.stream()
                        .filter(l -> isCorrect(input, l))
                        .mapToLong(this::score)
                        .sum();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final Input input = getInput(pc);
    return input.manuals.stream()
                        .filter(l -> !isCorrect(input, l))
                        .map(l -> sorted(input, l))
                        .mapToLong(this::score)
                        .sum();
  }

  /** Determine whether the given manual is in the correct order. */
  private boolean isCorrect(final Input input, final Collection<Integer> manual) {
    return manual.equals(sorted(input, manual));
  }

  /** Return a new manual that is a copy of the provided manual, but sorted. */
  private List<Integer> sorted(final Input input, final Collection<Integer> manual) {
    return manual.stream()
                 .sorted((a, b) -> input.ordering.get(a)
                                                 .contains(b) ? 1 : -1)
                 .toList();
  }

  /** Calculate the score for the manual, which is the number of its center page. */
  private long score(final List<Integer> manual) {
    return manual.get(manual.size() >> 1)
                 .longValue();
  }

  /** Get the program input. */
  private Input getInput(final PuzzleContext pc) {
    final List<List<String>> groups = il.groups(pc);
    return new Input(getOrdering(groups.get(0)), getManuals(groups.get(1)));
  }

  /** Given the rules for page ordering, get a mapping of every page number to the pages that must appear before it. */
  private Map<Integer, Set<Integer>> getOrdering(final List<String> rules) {
    final Map<Integer, Set<Integer>> pre = new HashMap<>();
    final Set<Integer> pages = new HashSet<>();
    for (final String rule : rules) {
      final String parts[] = RULE_SPLIT.split(rule);
      final Integer left = Integer.valueOf(parts[0]);
      final Integer right = Integer.valueOf(parts[1]);
      // This is never used in this method, but the first page needs an empty entry.
      pre.computeIfAbsent(left, HashSet::new);
      pre.computeIfAbsent(right, HashSet::new);

      pre.get(right)
         .add(left);
      pages.add(left);
      pages.add(right);
    }
    final Map<Integer, Set<Integer>> ordering = new HashMap<>();
    for (final Integer page : pages) {
      ordering.put(page, pre.get(page));
    }
    return ordering;
  }

  /** Parse comma-separated lines into a list of list of page numbers representing manuals. */
  private List<List<Integer>> getManuals(final List<String> lines) {
    return lines.stream()
                .map(l -> Arrays.stream(MANUAL_SPLIT.split(l))
                                .map(Integer::valueOf)
                                .toList())
                .toList();
  }

  private static final Pattern RULE_SPLIT = Pattern.compile("\\|");

  private static final Pattern MANUAL_SPLIT = Pattern.compile(",");

  /**
   * Input is a combination of ordering rules and manuals. Ordering rules are mappings from page numbers to other page
   * numbers that must appear before it in a list, if present. Manuals are simple lists of page numbers that may or may
   * not be in a valid order.
   */
  private static record Input(Map<Integer, Set<Integer>> ordering, List<List<Integer>> manuals) {}
}
