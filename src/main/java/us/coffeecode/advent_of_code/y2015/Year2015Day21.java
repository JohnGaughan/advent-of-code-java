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

import java.util.Set;
import java.util.function.LongBinaryOperator;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.function.BooleanUnaryOperator;

@AdventOfCodeSolution(year = 2015, day = 21, title = "RPG Simulator 20XX")
@Component
public final class Year2015Day21 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return runSimulation(pc, (a, b) -> Math.min(a, b), s -> s);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return runSimulation(pc, (a, b) -> Math.max(a, b), s -> !s);
  }

  /**
   * Run the simulation, using the provided functions to determine which gold value is more desirable and whether the
   * fight was successful.
   */
  private long runSimulation(final PuzzleContext pc, final LongBinaryOperator f, final BooleanUnaryOperator success) {
    // Keep the original boss around so we don't need to keep parsing strings.
    final Actor bossOriginal = new Actor(il.fileAsString(pc));
    long bestGoldAmount = Integer.MIN_VALUE;
    // I don't like four nested loops here, but the inputs are small.
    for (final Item weapon : WEAPONS) {
      for (final Item armor : ARMOR) {
        for (final Item ring1 : RINGS) {
          for (final Item ring2 : RINGS) {
            // Cannot duplicate rings, but no rings is fine.
            if (!"None".equals(ring1.name) && ring1.equals(ring2)) {
              continue;
            }
            final Actor boss = new Actor(bossOriginal);
            final Actor player = new Actor(weapon, armor, ring1, ring2);
            if (success.applyAsBoolean(fight(boss, player))) {
              final long goldSpent = weapon.cost + armor.cost + ring1.cost + ring2.cost;
              if (bestGoldAmount < 0) {
                bestGoldAmount = goldSpent;
              }
              bestGoldAmount = f.applyAsLong(bestGoldAmount, goldSpent);
            }
          }
        }
      }
    }
    return bestGoldAmount;
  }

  /** Let the two actors fight, and return true if the player wins. */
  private boolean fight(final Actor boss, final Actor player) {
    final long bossDamage = Math.max(1, boss.damage - player.armor);
    final long playerDamage = Math.max(1, player.damage - boss.armor);

    // Don't actually need to fight, just figure out how many turns each actor will live.
    long turnsUntilBossDies = boss.hp / playerDamage;
    if (boss.hp % playerDamage > 0) {
      ++turnsUntilBossDies;
    }
    long turnsUntilPlayerDies = player.hp / bossDamage;
    if (player.hp % bossDamage > 0) {
      ++turnsUntilPlayerDies;
    }

    // Player has the tie breaker.
    return turnsUntilPlayerDies >= turnsUntilBossDies;
  }

  /** Represents one actor, either the player or a boss. */
  private static final class Actor {

    private static final Pattern SPLIT_STRING = Pattern.compile("\n");

    private static final Pattern SPLIT_LINE = Pattern.compile(": ");

    long hp = 0;

    long damage = 0;

    long armor = 0;

    Actor(final Item... items) {
      hp = 100;
      for (final Item item : items) {
        damage += item.damage;
        armor += item.armor;
      }
    }

    Actor(final String input) {
      for (final String line : SPLIT_STRING.split(input)) {
        final String[] tokens = SPLIT_LINE.split(line);
        if ("Hit Points".equals(tokens[0])) {
          hp = Long.parseLong(tokens[1]);
        }
        if ("Damage".equals(tokens[0])) {
          damage = Long.parseLong(tokens[1]);
        }
        if ("Armor".equals(tokens[0])) {
          armor = Long.parseLong(tokens[1]);
        }
      }
    }

    Actor(final Actor original) {
      // Object.clone() is garbage
      hp = original.hp;
      damage = original.damage;
      armor = original.armor;
    }

  }

  private static final Set<Item> WEAPONS = Set.of(new Item("Dagger", 8, 4, 0), new Item("Shortsword", 10, 5, 0),
    new Item("Warhammer", 25, 6, 0), new Item("Longsword", 40, 7, 0), new Item("Greataxe", 74, 8, 0));

  private static final Set<Item> ARMOR =
    Set.of(new Item("None", 0, 0, 0), new Item("Leather", 13, 0, 1), new Item("Chainmail", 31, 0, 2),
      new Item("Splintmail", 53, 0, 3), new Item("Bandedmail", 75, 0, 4), new Item("Platemail", 102, 0, 5));

  private static final Set<Item> RINGS = Set.of(new Item("None", 0, 0, 0), new Item("Damage +1", 25, 1, 0),
    new Item("Damage +2", 50, 2, 0), new Item("Damage +3", 100, 3, 0), new Item("Defense +1", 20, 0, 1),
    new Item("Defense +2", 40, 0, 2), new Item("Defense +3", 80, 0, 3));

  /** Represents one item the player can equip. */
  private static record Item(String name, long cost, long damage, long armor) {}
}
