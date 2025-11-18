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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2015, day = 15)
@Component
public final class Year2015Day15 {

  /** The number of teaspoons worth of ingredients in the recipe. */
  private static final int QUANTITY = 100;

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return scores(il.linesAsObjects(pc, Rule::make), QUANTITY, -1).stream()
                                                                  .mapToLong(Long::longValue)
                                                                  .max()
                                                                  .getAsLong();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return scores(il.linesAsObjects(pc, Rule::make), QUANTITY, 500).stream()
                                                                   .mapToLong(Long::longValue)
                                                                   .max()
                                                                   .getAsLong();
  }

  /** Get all of the cookie scores. */
  private Set<Long> scores(final List<Rule> rules, final long totalQuantity, final long totalCalories) {
    return scores(rules, new ArrayList<>(), totalQuantity, totalCalories);
  }

  /** Get all of the cookie scores. */
  private Set<Long> scores(final List<Rule> rules, final List<Long> quantities, final long totalQuantity, final long totalCalories) {
    final Set<Long> scores = new HashSet<>();
    // Only one ingredient left: add the remaining quantity and calculate.
    if (rules.size() == quantities.size() + 1) {
      long remainingQuantity = totalQuantity;
      for (final long quantity : quantities) {
        remainingQuantity -= quantity;
      }
      final List<Long> newQuantities = new ArrayList<>(quantities);
      newQuantities.add(Long.valueOf(remainingQuantity));
      long capacity = 0;
      long durability = 0;
      long flavor = 0;
      long texture = 0;
      long calories = 0;
      for (int i = 0; i < rules.size(); ++i) {
        final Rule rule = rules.get(i);
        final long qty = newQuantities.get(i)
                                      .longValue();
        capacity += rule.capacity * qty;
        durability += rule.durability * qty;
        flavor += rule.flavor * qty;
        texture += rule.texture * qty;
        calories += rule.calories * qty;
      }
      if ((capacity > 0) && (durability > 0) && (flavor > 0) && (texture > 0)
        && ((totalCalories < 0) || (totalCalories == calories))) {
        scores.add(Long.valueOf(capacity * durability * flavor * texture));
      }
    }
    // Multiple ingredients left: iterate over possible quantities.
    else {
      long remainingQuantity = totalQuantity;
      for (final long quantity : quantities) {
        remainingQuantity -= quantity;
      }
      for (long i = 0; i <= remainingQuantity; ++i) {
        final List<Long> newQuantities = new ArrayList<>(quantities);
        newQuantities.add(Long.valueOf(i));
        scores.addAll(scores(rules, newQuantities, totalQuantity, totalCalories));
      }
    }
    return scores;
  }

  /** Represents one rule in the input file. */
  private static final record Rule(String ingredient, long capacity, long durability, long flavor, long texture, long calories) {

    private static final Pattern LINE_SPLIT = Pattern.compile(": ");

    private static final Pattern PROP_SPLIT = Pattern.compile(" ");

    static Rule make(final String input) {
      final String[] lineSplit = LINE_SPLIT.split(input);
      final String[] properties = PROP_SPLIT.split(lineSplit[1].replace(",", ""));
      return new Rule(lineSplit[0], Long.parseLong(properties[1]), Long.parseLong(properties[3]), Long.parseLong(properties[5]),
        Long.parseLong(properties[7]), Long.parseLong(properties[9]));
    }

  }

}
