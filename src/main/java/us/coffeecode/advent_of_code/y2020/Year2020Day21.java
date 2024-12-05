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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2020, day = 21, title = "Allergen Assessment")
@Component
public class Year2020Day21 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Collection<Food> foods = il.linesAsObjects(pc, Food::make);

    // Build a mapping of allergens to possible ingredients.
    final Map<String, Set<String>> mapping = mapAllergensToPossibleIngredients(foods);

    // Get all ingredients across all foods.
    final Collection<String> ingredients = new HashSet<>();
    for (final Food food : foods) {
      for (final String ingredient : food.ingredients) {
        ingredients.add(ingredient);
      }
    }

    // Remove all allergen candidates from the ingredients.
    for (final Iterator<String> iter = ingredients.iterator(); iter.hasNext();) {
      final String ingredient = iter.next();
      for (final Set<String> allergenIngredients : mapping.values()) {
        if (allergenIngredients.contains(ingredient)) {
          iter.remove();
          break;
        }
      }
    }

    // Now count how many times the ingredients appear.
    long counter = 0;
    for (final String ingredient : ingredients) {
      counter += foods.stream()
                      .filter(f -> f.ingredients.contains(ingredient))
                      .count();
    }

    return counter;
  }

  @Solver(part = 2)
  public String calculatePart2(final PuzzleContext pc) {
    // Build a mapping of allergens to possible ingredients.
    final Map<String, Set<String>> mapping = mapAllergensToPossibleIngredients(il.linesAsObjects(pc, Food::make));

    // Reduce the map repeatedly. Find an allergen with a single candidate ingredient and save that to the definitive
    // mapping. Remove its ingredient from the set of each other allergen's candidates.
    final SortedMap<String, String> definitive = new TreeMap<>();
    while (!mapping.isEmpty()) {
      for (final Iterator<Map.Entry<String, Set<String>>> iter = mapping.entrySet()
                                                                        .iterator(); iter.hasNext();) {
        final Map.Entry<String, Set<String>> entry = iter.next();
        if (entry.getValue()
                 .size() == 1) {
          final String ingredient = entry.getValue()
                                         .iterator()
                                         .next();
          definitive.put(entry.getKey(), ingredient);
          iter.remove();
          for (final Collection<String> ingredients : mapping.values()) {
            ingredients.remove(ingredient);
          }
        }
      }
    }

    // We are using a sorted map that orders on the allergen, so combine the values with commas and that is the answer.
    return definitive.values()
                     .stream()
                     .collect(Collectors.joining(","));
  }

  /**
   * Given a collection of various foods, figure out which ingredients can possibly map to each allergen. The allergens
   * are potential at this point, and are not a one-to-one mapping.
   */
  private Map<String, Set<String>> mapAllergensToPossibleIngredients(final Iterable<Food> foods) {
    // Build up a mapping of allergens to possible ingredients, and all ingredients.
    final Map<String, Set<String>> mapping = new HashMap<>();
    for (final Food food : foods) {
      for (final String allergen : food.allergens) {
        // If we have seen this allergen before, we can reduce the possible ingredients by computing the intersection of
        // ingredients.
        if (mapping.containsKey(allergen)) {
          mapping.get(allergen)
                 .retainAll(food.ingredients);
        }
        // Otherwise, all ingredients in this food are possible allergens.
        else {
          mapping.put(allergen, new HashSet<>());
          mapping.get(allergen)
                 .addAll(food.ingredients);
        }
      }
    }

    return mapping;
  }

  /**
   * Represents one food in the input file. Each food entry is a set of ingredients that appear in the food along with
   * allergens that appear somewhere in those ingredients. Only one ingredient represents each allergen, but we do not
   * know which one it is to start with. Figuring that out is the goal of this puzzle.
   */
  private static record Food(Set<String> ingredients, Set<String> allergens) {

    private static final Pattern SPLIT_LINE = Pattern.compile(" \\(contains ");

    private static final Pattern SPLIT_INGREDIENTS = Pattern.compile(" ");

    private static final Pattern SPLIT_ALLERGENS = Pattern.compile(", ");

    static Food make(final String input) {
      final String[] parts = SPLIT_LINE.split(input.substring(0, input.length() - 1));
      return new Food(Arrays.stream(SPLIT_INGREDIENTS.split(parts[0]))
                            .collect(Collectors.toSet()),
        Arrays.stream(SPLIT_ALLERGENS.split(parts[1]))
              .collect(Collectors.toSet()));
    }

  }

}
