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

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2015/day/15">Year 2015, day 15</a>. This question asks us to find what proportion
 * of ingredients in a recipe has a maximum score given a scoring algorithm and a maximum quantity of teaspoons of
 * ingredients used. Part one asks for this score, while part two constrains valid ingredient ratios based on the number
 * of calories.
 * </p>
 * <p>
 * There are several ways to go about this, and the more efficient algorithms are based on the fact that there are only
 * four ingredients. Then it would be possible to write some loops based on those assumptions. Instead, I opted for a
 * general-purpose algorithm based on an unknown number of ingredients. This led me to a recursive algorithm that does
 * not perform as well, but produces correct results regardless of the ingredient count.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2015Day15 {

  /** The number of teaspoons worth of ingredients in the recipe. */
  private static final int QUANTITY = 100;

  public int calculatePart1() {
    return scores(getInput(), QUANTITY, -1).stream().mapToInt(Integer::intValue).max().getAsInt();
  }

  public int calculatePart2() {
    return scores(getInput(), QUANTITY, 500).stream().mapToInt(Integer::intValue).max().getAsInt();
  }

  /** Get all of the cookie scores. */
  private Set<Integer> scores(final List<Rule> rules, final int totalQuantity, final int totalCalories) {
    return scores(rules, new ArrayList<>(), totalQuantity, totalCalories);
  }

  /** Get all of the cookie scores. */
  private Set<Integer> scores(final List<Rule> rules, final List<Integer> quantities, final int totalQuantity, final int totalCalories) {
    final Set<Integer> scores = new HashSet<>();
    // Only one ingredient left: add the remaining quantity and calculate.
    if (rules.size() == quantities.size() + 1) {
      int remainingQuantity = totalQuantity;
      for (final int quantity : quantities) {
        remainingQuantity -= quantity;
      }
      final List<Integer> newQuantities = new ArrayList<>(quantities);
      newQuantities.add(remainingQuantity);
      int capacity = 0;
      int durability = 0;
      int flavor = 0;
      int texture = 0;
      int calories = 0;
      for (int i = 0; i < rules.size(); ++i) {
        final Rule rule = rules.get(i);
        final int qty = newQuantities.get(i);
        capacity += rule.capacity * qty;
        durability += rule.durability * qty;
        flavor += rule.flavor * qty;
        texture += rule.texture * qty;
        calories += rule.calories * qty;
      }
      if (capacity > 0 && durability > 0 && flavor > 0 && texture > 0
        && (totalCalories < 0 || totalCalories == calories)) {
        scores.add(Integer.valueOf(capacity * durability * flavor * texture));
      }
    }
    // Multiple ingredients left: iterate over possible quantities.
    else {
      int remainingQuantity = totalQuantity;
      for (final int quantity : quantities) {
        remainingQuantity -= quantity;
      }
      for (int i = 0; i <= remainingQuantity; ++i) {
        final List<Integer> newQuantities = new ArrayList<>(quantities);
        newQuantities.add(i);
        scores.addAll(scores(rules, newQuantities, totalQuantity, totalCalories));
      }
    }
    return scores;
  }

  /** Get the input data for this solution. */
  private List<Rule> getInput() {
    try {
      return Files.readAllLines(Utils.getInput(2015, 15)).stream().map(s -> new Rule(s)).collect(Collectors.toList());
    }
    catch (final RuntimeException ex) {
      throw ex;
    }
    catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  /** Represents one rule in the input file. */
  private static final class Rule {

    private static final Pattern LINE_SPLIT = Pattern.compile(": ");

    private static final Pattern PROP_SPLIT = Pattern.compile(" ");

    final String ingredient;

    final int capacity;

    final int durability;

    final int flavor;

    final int texture;

    final int calories;

    Rule(final String input) {
      final String[] lineSplit = LINE_SPLIT.split(input);
      ingredient = lineSplit[0];
      final String[] properties = PROP_SPLIT.split(lineSplit[1].replace(",", ""));
      capacity = Integer.parseInt(properties[1]);
      durability = Integer.parseInt(properties[3]);
      flavor = Integer.parseInt(properties[5]);
      texture = Integer.parseInt(properties[7]);
      calories = Integer.parseInt(properties[9]);
      Thread.yield();
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
      return "[" + ingredient + ",capacity=" + capacity + ",durability=" + durability + ",flavor=" + flavor
        + ",texture=" + texture + ",calories=" + calories + "]";
    }
  }
}
