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
package us.coffeecode.advent_of_code.y2015;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2015, day = 19, title = "Medicine for Rudolph")
@Component
public final class Year2015Day19 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Input input = getInput(pc);
    final Set<String> molecules = new HashSet<>();
    for (final Rule rule : input.rules) {
      for (int i = 0; i < input.molecule.length(); ++i) {
        final int j = input.molecule.indexOf(rule.left, i);
        if (j >= 0) {
          final StringBuilder str = new StringBuilder();
          str.append(input.molecule.substring(0, j));
          str.append(rule.right);
          str.append(input.molecule.substring(j + rule.left.length()));
          molecules.add(str.toString());
          i = j + 1;
        }
      }
    }
    return molecules.size();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final Input input = getInput(pc);
    String str = input.molecule;
    long replacements = 0;
    while (!"e".equals(str)) {
      outer: for (final Rule rule : input.rules) {
        int found;
        while ((found = str.indexOf(rule.right)) >= 0) {
          final StringBuilder temp = new StringBuilder(input.molecule.length());
          temp.append(str.substring(0, found));
          temp.append(rule.left);
          temp.append(str.substring(found + rule.right.length()));
          // Examples are simple enough that they can put a terminal character in the middle of the string, which can
          // then never be reduced and results in an infinite loop. If that condition occurs, ignore it and try whatever
          // rule comes next.
          if ("e".equals(rule.left) && temp.length() > 1) {
            continue outer;
          }
          str = temp.toString();
          ++replacements;
        }
      }
    }
    return replacements;
  }

  /** Get the input data for this solution. */
  private Input getInput(final PuzzleContext pc) {
    final List<List<String>> groups = il.groups(pc);
    final Collection<Rule> rules = groups.getFirst()
                                         .stream()
                                         .map(Rule::make)
                                         .collect(Collectors.toSet());
    return new Input(rules, groups.get(1)
                                  .getFirst());
  }

  private static record Input(Collection<Rule> rules, String molecule) {}

  /** Represents one production rule in the input. */
  private static record Rule(String left, String right) {

    static Rule make(final String line) {
      final String[] tokens = SPLIT.split(line);
      return new Rule(tokens[0], tokens[1]);
    }

    private static final Pattern SPLIT = Pattern.compile(" => ");
  }
}
