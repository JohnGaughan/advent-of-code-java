/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2021  John Gaughan
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
package us.coffeecode.advent_of_code.y2019;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2019, day = 14, title = "Space Stoichiometry")
@Component
public final class Year2019Day14 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Map<String, Reaction> reactions = il.linesAsMap(pc, Reaction::new, r -> r.product, Function.identity());
    return calculate(reactions, 1);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final long target = 1_000_000_000_000L;
    final Map<String, Reaction> reactions = il.linesAsMap(pc, Reaction::new, r -> r.product, Function.identity());

    // Get a reasonable search window. Dividing the target by the minimum ore to create one fuel is the theoretical
    // lower bound that will be less than or equal to the answer and close to it. Double it for an arbitrary upper
    // bound.
    long fuel_low = target / calculate(reactions, 1);
    long fuel_high = (fuel_low << 1);

    // Binary search on values until we find the maximum value under the threshold.
    while (fuel_low < fuel_high - 1) {
      final long fuel_mid = (fuel_low + fuel_high) >> 1;
      final long ore = calculate(reactions, fuel_mid);
      if (ore < target) {
        fuel_low = fuel_mid;
      }
      else if (ore > target) {
        fuel_high = fuel_mid;
      }
    }

    return fuel_low;
  }

  private long calculate(final Map<String, Reaction> reactions, final long fuel) {
    final Map<String, Long> onHand = new HashMap<>();
    final Map<String, Long> need = new HashMap<>();
    need.put("FUEL", Long.valueOf(fuel));
    long ore = 0;
    while (!need.isEmpty()) {
      final String product = need.keySet()
                                 .iterator()
                                 .next();
      final long quantity = need.get(product)
                                .longValue();

      // Remove the product now. It will either be satisfied with what is on hand, or will be replaced with its
      // reactants.
      need.remove(product);

      // See if this chemical can be completely taken care of with existing chemicals on hand.
      if (onHand.containsKey(product) && quantity <= onHand.get(product)
                                                           .longValue()) {
        onHand.put(product, Long.valueOf(onHand.get(product)
                                               .longValue()
          - quantity));
        continue;
      }

      // Get the amount needed in excess of what is on hand.
      final long needed = quantity - (onHand.containsKey(product) ? onHand.get(product)
                                                                          .longValue()
        : 0);

      // Get the amount created by this reaction.
      final long created = reactions.get(product).outQuantity;

      // Determine the reactions needed: this is needed / created, rounding up.
      final long reactionsNeeded = needed / created + (needed % created == 0 ? 0 : 1);

      // Put the remainder of what is reacted into on-hand.
      onHand.put(product, Long.valueOf(reactionsNeeded * created - needed));

      // Iterate over all of the reactants for the chemical reaction that makes the product.
      final Reaction reaction = reactions.get(product);
      for (int i = 0; i < reaction.reactants.length; ++i) {
        // The quantity to add is the number of reactions times the amount produced per reaction.
        final long addQuantity = reaction.reactantQuantities[i] * reactionsNeeded;
        final String reactant = reaction.reactants[i];
        if ("ORE".equals(reactant)) {
          // If this is ore, we hit the end of the chain: add it to the ore counter and do nothing else.
          ore += addQuantity;
        }
        else if (need.containsKey(reactant)) {
          // We already need this reactant: combine it with the existing map entry.
          need.put(reactant, Long.valueOf(need.get(reactant)
                                              .longValue()
            + addQuantity));
        }
        else {
          // New reactant: add a new entry to the map of what is needed.
          need.put(reactant, Long.valueOf(addQuantity));
        }
      }
    }
    return ore;
  }

  private static final class Reaction {

    private static final Pattern SPLIT = Pattern.compile(",|=>");

    private static final Pattern SPLIT_2 = Pattern.compile(" ");

    final String[] reactants;

    final long[] reactantQuantities;

    final String product;

    final long outQuantity;

    Reaction(final String str) {
      final String[] chemicals = SPLIT.split(str.trim());
      final String[][] tokens = new String[chemicals.length][];
      for (int i = 0; i < chemicals.length; ++i) {
        tokens[i] = SPLIT_2.split(chemicals[i].trim());
      }
      reactants = new String[tokens.length - 1];
      reactantQuantities = new long[tokens.length - 1];
      for (int i = 0; i < tokens.length - 1; ++i) {
        reactantQuantities[i] = Long.parseLong(tokens[i][0]);
        reactants[i] = tokens[i][1];
      }
      outQuantity = Long.parseLong(tokens[tokens.length - 1][0]);
      product = tokens[tokens.length - 1][1];
    }

    @Override
    public String toString() {
      final StringBuilder str = new StringBuilder(128);
      for (int i = 0; i < reactants.length; ++i) {
        if (str.length() > 0) {
          str.append(", ");
        }
        str.append(reactantQuantities[i])
           .append(" ")
           .append(reactants[i]);
      }
      str.append(" => ")
         .append(outQuantity)
         .append(" ")
         .append(product);
      return str.toString();
    }
  }

}
