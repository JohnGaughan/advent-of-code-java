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

import net.johngaughan.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2020/day/21">Year 2020, day 21</a>. This puzzle requires some basic set operations.
 * Part one asks us to find ingredients that cannot be food allergens, and count how many times they occur in all of the
 * foods. Part two requires us to reduce the potential allergens down and solve which ingredient maps to which allergen,
 * then prove the answer by emitting the problematic ingredients in alphabetic order - of their corresponding allergen
 * names.
 * </p>
 * <p>
 * There is not much to say about this one. It is basic set manipulation, finding intersections of sets then reducing
 * them to make them unique.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public class Year2020Day21 {

  public long calculatePart1() {
    final Collection<Food> foods = getInput();

    // Build a mapping of allergens to possible ingredients.
    final Map<String, Set<String>> mapping = mapAllergensToPossibleIngredients(foods);

    // Get all ingredients across all foods.
    final Set<String> ingredients = new HashSet<>();
    for (Food food : foods) {
      for (String ingredient : food.ingredients) {
        ingredients.add(ingredient);
      }
    }

    // Remove all allergen candidates from the ingredients.
    for (final Iterator<String> iter = ingredients.iterator(); iter.hasNext();) {
      final String ingredient = iter.next();
      for (Set<String> allergenIngredients : mapping.values()) {
        if (allergenIngredients.contains(ingredient)) {
          iter.remove();
          break;
        }
      }
    }

    // Now count how many times the ingredients appear.
    int counter = 0;
    for (String ingredient : ingredients) {
      for (Food food : foods) {
        if (food.ingredients.contains(ingredient)) {
          ++counter;
        }
      }
    }

    return counter;
  }

  public String calculatePart2() {
    // Build a mapping of allergens to possible ingredients.
    final Map<String, Set<String>> mapping = mapAllergensToPossibleIngredients(getInput());

    // Reduce the map repeatedly. Find an allergen with a single candidate ingredient and save that to the definitive
    // mapping. Remove its ingredient from the set of each other allergen's candidates.
    final SortedMap<String, String> definitive = new TreeMap<>();
    while (!mapping.isEmpty()) {
      for (Iterator<Map.Entry<String, Set<String>>> iter = mapping.entrySet().iterator(); iter.hasNext();) {
        final Map.Entry<String, Set<String>> entry = iter.next();
        if (entry.getValue().size() == 1) {
          final String ingredient = entry.getValue().iterator().next();
          definitive.put(entry.getKey(), ingredient);
          iter.remove();
          for (Set<String> ingredients : mapping.values()) {
            ingredients.remove(ingredient);
          }
        }
      }
    }

    // We are using a sorted map that orders on the allergen, so combine the values with commas and that is the answer.
    final StringBuilder str = new StringBuilder(256);
    for (String allergen : definitive.values()) {
      if (str.length() > 0) {
        str.append(",");
      }
      str.append(allergen);
    }
    return str.toString();
  }

  /**
   * Given a collection of various foods, figure out which ingredients can possibly map to each allergen. The allergens
   * are potential at this point, and are not a one-to-one mapping.
   */
  private Map<String, Set<String>> mapAllergensToPossibleIngredients(final Collection<Food> foods) {
    // Build up a mapping of allergens to possible ingredients, and all ingredients.
    final Map<String, Set<String>> mapping = new HashMap<>();
    for (Food food : foods) {
      for (String allergen : food.allergens) {
        // If we have seen this allergen before, we can reduce the possible ingredients by computing the intersection of
        // ingredients.
        if (mapping.containsKey(allergen)) {
          mapping.get(allergen).retainAll(food.ingredients);
        }
        // Otherwise, all ingredients in this food are possible allergens.
        else {
          mapping.put(allergen, new HashSet<>());
          mapping.get(allergen).addAll(food.ingredients);
        }
      }
    }

    return mapping;
  }

  /** Get the input data for this solution. */
  private Collection<Food> getInput() {
    try {
      return Files.readAllLines(Utils.getInput(2020, 21)).stream().map(Food::new).collect(Collectors.toList());
    }
    catch (final RuntimeException ex) {
      throw ex;
    }
    catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Represents one food in the input file. Each food entry is a set of ingredients that appear in the food along with
   * allergens that appear somewhere in those ingredients. Only one ingredient represents each allergen, but we do not
   * know which one it is to start with. Figuring that out is the goal of this puzzle.
   */
  private static final class Food {

    private static final Pattern SPLIT_LINE = Pattern.compile(" \\(contains ");

    private static final Pattern SPLIT_INGREDIENTS = Pattern.compile(" ");

    private static final Pattern SPLIT_ALLERGENS = Pattern.compile(", ");

    final Set<String> ingredients;

    final Set<String> allergens;

    Food(final String input) {
      final String[] parts = SPLIT_LINE.split(input.substring(0, input.length() - 1));
      ingredients = Arrays.stream(SPLIT_INGREDIENTS.split(parts[0])).collect(Collectors.toCollection(HashSet::new));
      allergens = Arrays.stream(SPLIT_ALLERGENS.split(parts[1])).collect(Collectors.toCollection(HashSet::new));
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
      final StringBuilder str = new StringBuilder(1024);
      str.append("Ingredients:");
      for (String s : ingredients) {
        str.append(" ").append(s);
      }
      str.append("\nAllergens:");
      for (String s : allergens) {
        str.append(" ").append(s);
      }
      str.append("\n");
      return str.toString();
    }
  }
}
