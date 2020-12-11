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
package net.johngaughan.advent_of_code.y2015;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import net.johngaughan.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2015/day/19">Year 2015, day 19</a>. This problem relates to formal language
 * grammar. Part one is an exercise in string replacement using grammar rules: get the total number of unique strings
 * after one replacement. Part two asks us to perform many string replacements and to get the fewest steps required to
 * find the molecule string.
 * </p>
 * <p>
 * Part one is a simple exercise in iterating over each rule and applying it everywhere in the molecule string it can
 * be, then counting how many unique results there are. This is slightly complicated by the fact that while Java has a
 * way to get the next substring starting at an index, it cannot do the same with replacements.
 * </p>
 * <p>
 * Part two looks really daunting at first, and it is, for the general case. However, the puzzle creator gave a hint
 * that greatly simplifies things: there is exactly one solution. This means there is no need to look for multiple and
 * find the best, and there is no possibility of applying rules incorrectly and getting into a dead end (when reducing
 * the string to <i>e</i> rather than the fool's errand of generating it).
 * </p>
 * <p>
 * My solution is to work backwards, reducing the string by applying the grammar rules in reverse over and over again.
 * Iterate through the rules. Apply each one to the string from left to right, then move on to the next rule. If the
 * string is ever equal to <i>e</i> then stop. There are some more elegant ways to do this, given that the grammar rules
 * have interesting properties such as the elements Rn and Ar always appearing in pairs and always in the same order,
 * like parenthesis. Those elements along with C and Y are terminals in this grammar. It is never possible to apply a
 * rule in a way that violates the chemistry naming convention, e.g. both C and Ca are symbols, but only Ca appears on
 * the left-hand side. I think it would be interesting to do more intelligent analysis and processing of this string,
 * but the solution runs in 4ms so there is no real need to do so.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public final class Year2015Day19 {

  public int calculatePart1(final Path path) {
    final Input input = parse(path);
    final Set<String> molecules = new HashSet<>();
    for (Rule rule : input.rules) {
      for (int i = 0; i < input.molecule.length(); ++i) {
        int j = input.molecule.indexOf(rule.left, i);
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

  public int calculatePart2(final Path path) {
    final Input input = parse(path);
    String str = input.molecule;
    int replacements = 0;
    while (!"e".equals(str)) {
      for (Rule rule : input.rules) {
        int found;
        while ((found = str.indexOf(rule.right)) >= 0) {
          final StringBuilder temp = new StringBuilder(input.molecule.length());
          temp.append(str.substring(0, found));
          temp.append(rule.left);
          temp.append(str.substring(found + rule.right.length()));
          str = temp.toString();
          ++replacements;
        }
      }
    }
    return replacements;
  }

  /** Parse the file located at the provided path location. */
  private Input parse(final Path path) {
    try {
      final List<List<String>> groups = Utils.getLineGroups(Files.readAllLines(path));
      final Collection<Rule> rules = groups.get(0).stream().map(Rule::new).collect(Collectors.toSet());
      return new Input(rules, groups.get(1).get(0));
    }
    catch (

    final RuntimeException ex) {
      throw ex;
    }
    catch (

    final Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private static final class Input {

    final Collection<Rule> rules;

    final String molecule;

    /** Constructs a <code>Input</code>. */
    Input(final Collection<Rule> r, final String m) {
      rules = r;
      molecule = m;
    }
  }

  /** Represents one production rule in the input. */
  private static final class Rule {

    private static final Pattern SPLIT = Pattern.compile(" => ");

    final String left;

    final String right;

    final int hash;

    Rule(final String line) {
      final String[] tokens = SPLIT.split(line);
      left = tokens[0];
      right = tokens[1];
      hash = Objects.hash(left, right);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
      if (this == obj) {
        return true;
      }
      else if (!(obj instanceof Rule)) {
        return false;
      }
      else {
        Rule other = (Rule) obj;
        return Objects.equals(left, other.left) && Objects.equals(right, other.right);
      }
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
      return hash;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
      return "[" + left + " => " + right + "]";
    }
  }
}
