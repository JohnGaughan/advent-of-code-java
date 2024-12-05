/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2023  John Gaughan
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
package us.coffeecode.advent_of_code.y2023;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Range;
import us.coffeecode.advent_of_code.util.Range4D;

@AdventOfCodeSolution(year = 2023, day = 19, title = "Aplenty")
@Component
public class Year2023Day19 {

  // All ranges here are inclusive. X = [4...6] means there are three values in range.

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Input in = getInput(pc);
    final Collection<Map<Category, Range>> ranges = makeRanges(in.workflows);
    long score = 0;
    for (final Map<Category, Integer> part : in.parts) {
      if (ranges.stream()
                .anyMatch(m -> Arrays.stream(Category.values())
                                     .allMatch(c -> m.get(c)
                                                     .containsInclusive(part.get(c)
                                                                            .intValue())))) {
        score += part.values()
                     .stream()
                     .mapToLong(Integer::longValue)
                     .sum();
      }
    }
    return score;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final Input in = getInput(pc);
    return size(makeRanges(in.workflows).stream()
                                        .map(this::toRange4D)
                                        .toList());
  }

  /** Get the total size of the given 4D ranges, excluding duplicated regions. */
  private long size(final List<Range4D> ranges) {
    long size = 0;
    for (int i = 0; i < ranges.size(); ++i) {
      final Range4D range = ranges.get(i);
      size += range.sizeInclusive();
    }
    // Loop over the intersections, then the intersections of those intersections, until nothing remains. Alternate
    // between subtracting and adding the volumes, which corrects for multiple (possibly many) ranges overlapping and
    // over-counting their volumes.
    List<Range4D> intersections = getIntersections(ranges);
    long signum = -1;
    while (!intersections.isEmpty()) {
      size += signum * intersections.stream()
                                    .mapToLong(Range4D::sizeInclusive)
                                    .sum();
      signum = -signum;
      intersections = getIntersections(intersections);
    }
    return size;
  }

  /** Get all intersections between ranges. */
  private List<Range4D> getIntersections(final List<Range4D> ranges) {
    final List<Range4D> intersections = new ArrayList<>(ranges.size());
    for (int i = 1; i < ranges.size(); ++i) {
      for (int j = 0; j < i; ++j) {
        final Range4D r1 = ranges.get(i);
        final Range4D r2 = ranges.get(j);
        if (r1.overlaps(r2)) {
          intersections.add(r1.intersection(r2));
        }
      }
    }
    return intersections;
  }

  /** Convert individual 1D ranges by category into combined 4D ranges. */
  private Range4D toRange4D(final Map<Category, Range> ranges) {
    final Range x = ranges.get(Category.X);
    final Range m = ranges.get(Category.M);
    final Range a = ranges.get(Category.A);
    final Range s = ranges.get(Category.S);
    return new Range4D(x.getX1(), m.getX1(), a.getX1(), s.getX1(), x.getX2(), m.getX2(), a.getX2(), s.getX2());
  }

  /** Reduce the workflow rules down such that we are left with the minimal range that satisfies them. */
  private final Collection<Map<Category, Range>> makeRanges(final Map<String, List<Rule>> workflows) {
    final Map<Category, Range> ranges = new HashMap<>();
    for (final Category c : Category.values()) {
      ranges.put(c, new Range(1, 4000));
    }
    return makeRanges(workflows, "in", ranges);
  }

  /** Recurse part of the workflow reduction algorithm. */
  private final Collection<Map<Category, Range>> makeRanges(final Map<String, List<Rule>> workflows, final String workflow, final Map<Category, Range> ranges) {
    final Set<Map<Category, Range>> accepted = new HashSet<>();
    final Map<Category, Range> myRanges = new HashMap<>(ranges);
    for (final Rule rule : workflows.get(workflow)) {
      // No test to perform. This will be a terminal operation.
      if (rule.test == null) {
        // Accept the results: add the current range to the accepted ranges.
        if ("A".equals(rule.then)) {
          accepted.add(new HashMap<>(myRanges));
        }
        // Need to recurse into another workflow.
        else if (!"R".equals(rule.then)) {
          accepted.addAll(makeRanges(workflows, rule.then, myRanges));
        }
        break;
      }
      // Perform the test.
      else {
        // If the test can only be true, run it and don't continue.
        if (rule.isCompletelyInRange(myRanges)) {
          // Accept the results: add the current range to the accepted ranges.
          if ("A".equals(rule.then)) {
            accepted.add(new HashMap<>(myRanges));
          }
          // Need to recurse into another workflow.
          else if (!"R".equals(rule.then)) {
            accepted.addAll(makeRanges(workflows, rule.then, myRanges));
          }
          break;
        }
        // If the test can never be true, skip it.
        else if (rule.isCompletelyOutOfRange(myRanges)) {
          continue;
        }
        // Range is split between parts that the rule matches and does not match.
        final Range[] split = rule.split(myRanges);
        myRanges.put(rule.category, split[0]);
        // Accept the results: add the current range to the accepted ranges.
        if ("A".equals(rule.then)) {
          accepted.add(new HashMap<>(myRanges));
        }
        // Need to recurse into another workflow.
        else if (!"R".equals(rule.then)) {
          accepted.addAll(makeRanges(workflows, rule.then, myRanges));
        }
        myRanges.put(rule.category, split[1]);
      }
    }
    return accepted;
  }

  /** Make the puzzle input from the configured input file. */
  private Input getInput(final PuzzleContext pc) {
    final List<List<String>> groups = il.groups(pc);
    return new Input(makeWorkflows(groups.getFirst()), makeParts(groups.getLast()));
  }

  /** Make workflows from the provided strings from the input file. */
  private Map<String, List<Rule>> makeWorkflows(final Collection<String> inputs) {
    // This method deliberately uses mutable maps and lists so they can be reduced by removing redundant rules.
    final Map<String, List<Rule>> workflows = new HashMap<>(inputs.size() << 1);
    for (final String line : inputs) {
      final int openBrace = line.indexOf('{');
      workflows.put(line.substring(0, openBrace),
        Arrays.stream(RULE_SPLIT.split(line.substring(openBrace + 1, line.length() - 1)))
              .map(this::makeRule)
              .collect(ArrayList::new, List::add, List::addAll));
    }
    return workflows;
  }

  /** Make a single rule within a workflow given the rule's text. */
  private Rule makeRule(final String text) {
    final int colon = text.indexOf(':');
    // There is no test: this rule always applies so pass along the text which is A, R, or a workflow ID.
    if (colon < 0) {
      return new Rule(null, null, 0, text);
    }
    // Comparison test. Construct a Predicate for the test, with the "then" being the part after the colon.
    return new Rule(Test.valueOf(text.codePointAt(1)), Category.valueOf(text.codePointAt(0)),
      Integer.parseInt(text.substring(2, colon)), text.substring(colon + 1));
  }

  /** Make parts from the provided strings from the input file. */
  private List<Map<Category, Integer>> makeParts(final Collection<String> inputs) {
    final List<Map<Category, Integer>> parts = new ArrayList<>(inputs.size());
    for (final String line : inputs) {
      parts.add(Arrays.stream(VALUES_SPLIT.split(line.substring(1, line.length() - 1)))
                      .collect(Collectors.toMap(s -> Category.valueOf(s.codePointAt(0)), s -> Integer.valueOf(s.substring(2)))));
    }
    return parts;
  }

  /** Greater than or less than test of a value. */
  private static enum Test {

    LESS_THAN('<'),
    GREATER_THAN('>');

    static Test valueOf(final int ch) {
      return Arrays.stream(values())
                   .filter(e -> e.ch == ch)
                   .findFirst()
                   .orElseGet(() -> null);
    }

    final int ch;

    private Test(final int _ch) {
      ch = _ch;
    }
  }

  /** One category is one value on a part, or matched in a rule. */
  private static enum Category {

    X,
    M,
    A,
    S;

    static Category valueOf(final int ch) {
      return valueOf(Character.toString(ch)
                              .toUpperCase());
    }
  }

  /**
   * Contains all of the puzzle input data. Parts are represented as a map of integer code points identifying the
   * category, mapped to the value for that category. Each workflow is stored in a map where the workflow name is the
   * key, and the rules in the workflow are the value.
   */
  private record Input(Map<String, List<Rule>> workflows, List<Map<Category, Integer>> parts) {}

  /** Represents one rule in a workflow, which tests a part and if true, resolves to a string result. */
  private record Rule(Test test, Category category, int value, String then) {

    /** Get whether this rule will match the entire range for the matching category. */
    boolean isCompletelyInRange(final Map<Category, Range> ranges) {
      final Range range = ranges.get(category);
      if ((Test.GREATER_THAN == test) && (range.getX1() > value)) {
        return true;
      }
      else if ((Test.LESS_THAN == test) && (range.getX2() < value)) {
        return true;
      }
      return false;
    }

    /** Get whether this rule will match none of the range for the matching category. */
    boolean isCompletelyOutOfRange(final Map<Category, Range> ranges) {
      final Range range = ranges.get(category);
      if ((Test.GREATER_THAN == test) && (range.getX2() <= value)) {
        return true;
      }
      else if ((Test.LESS_THAN == test) && (range.getX1() >= value)) {
        return true;
      }
      return false;
    }

    /** Split the range in two. First element matches the comparison of this rule, second element does not. */
    Range[] split(final Map<Category, Range> ranges) {
      final Range range = ranges.get(category);
      final Range[] split = new Range[2];
      if (Test.GREATER_THAN == test) {
        split[0] = new Range(value + 1, range.getX2());
        split[1] = new Range(range.getX1(), value);
      }
      else {
        split[0] = new Range(range.getX1(), value - 1);
        split[1] = new Range(value, range.getX2());
      }
      return split;
    }

  }

  /** Matches four sets of digits for parts. */
  private static final Pattern VALUES_SPLIT = Pattern.compile(",");

  /** Splits rule definitions. */
  private static final Pattern RULE_SPLIT = Pattern.compile(",");
}
